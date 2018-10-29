package sample;

import dao.CredentialsDao;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    @FXML
    private TableColumn<Message, Rectangle> rectColumn;

    @FXML
    private TableColumn<Message, String> messageColumn;

    @FXML
    TextField enterIp;

    @FXML
    Button buttonIp;

    @FXML
    Text textIp;

    @FXML
    TableView<Message> showingField;

    @FXML
    Rectangle r2;

    @FXML
    Rectangle r3;

    @FXML
    Rectangle r4;

    @FXML
    Rectangle r5;

    @FXML
    Button buttonLogin;

    @FXML
    Rectangle r6;

    @FXML
    Rectangle r7;

    @FXML
    Rectangle r8;

    @FXML
    Rectangle r9;

    @FXML
    Button button;

    @FXML
    Rectangle r10;

    @FXML
    PasswordField passField;

    @FXML
    Rectangle r12;

    @FXML
    Rectangle r11;

    @FXML
    TextField enteringField;

    @FXML
    Button redB;

    @FXML
    Button greenB;

    @FXML
    Button check;

//    @FXML
//    BarChart<?, ?> gist;

    @FXML
    Text text1;

    @FXML
    Text text2;

    @FXML
    Button blackB;

    @FXML
    TextField loginField;

    @FXML
    Button blueB;

    @FXML
    Rectangle r1;

    private Controller controller;

    private String clientCommand = "";

    private static Stage stage;

    private String login;
    private String pass;

    private static Map<Integer, Rectangle> mapOfRectangles = new HashMap<>();


    private static Map<Integer, String> mapOfMessages = new HashMap<>();
    private static int idOfMessage = 0;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

//    private static Map<String, String> db = new HashMap<>();

//    static {
//        db.put("root", "root");
//        db.put("kek", "kek");
//        db.put("user", "user");
//    }

//    private static Map<String, Color> colorMap = new HashMap<>();

//    static {
//        colorMap.put("root", Color.BLACK);
//        colorMap.put("kek", Color.RED);
//    }

    private String name = "";

    BufferedWriter writerForButtons = null;

    private boolean flag = true;

    private final BufferedWriter[] oos = {null};
    private final DataInputStream[] ois = {null};
    private final BufferedReader[] reader = {null};
    private final Socket[] socket = {null};

    private ObservableList<Message> observableList = FXCollections.observableArrayList();

    private final CredentialsDao credentialsDao = new CredentialsDao();
    private static Connection conn;

    static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

    @FXML
    void onLoginButton() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, "postgres", "12345");
        credentialsDao.nullingAmountOfMessages(conn);
        String login = loginField.getText();
        String pass = passField.getText();
        this.login = login;
        this.pass = pass;
        try {
            if (credentialsDao.checkUser(conn, login, pass)) {

                setIpChoseScene();

                name = login;
            }
        } catch (Exception e) {
            Stage st = new Stage();
            st.initModality(Modality.APPLICATION_MODAL);
            st.initOwner(stage);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("Wrong credentials"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            st.setScene(dialogScene);
            st.show();
            loginField.clear();
            passField.clear();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Start client");
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    @FXML
    public void initialize() throws SQLException {
        controller = new Controller();
        setLoginScene();
//
//        statMap.put("kek", 0);
//        statMap.put("root", 0);


        buttonIp.setOnAction(event -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String host = enterIp.getText();
            try {
                socket[0] = new Socket(host, 3345);
                oos[0] = new BufferedWriter(new OutputStreamWriter(socket[0].getOutputStream()));
                ois[0] = new DataInputStream(socket[0].getInputStream());
                reader[0] = new BufferedReader(new InputStreamReader(socket[0].getInputStream()));
                writerForButtons = oos[0];
                runAll(socket[0], oos[0], ois[0], reader[0]);


                setChatScene();

            } catch (IOException e) {
                e.printStackTrace();
                Stage st = new Stage();
                st.initModality(Modality.APPLICATION_MODAL);
                st.initOwner(stage);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("Wrong ip"));
                Scene dialogScene = new Scene(dialogVbox, 300, 200);
                st.setScene(dialogScene);
                st.show();
            }
        });
    }

    private synchronized void runSender(Socket socket, BufferedWriter oos, DataInputStream ois) {
        new Thread() {

            @Override
            public void run() {
                logger.info("Start sender");
                System.out.println(1);
                while (!socket.isOutputShutdown()) {

                    try {
                        Thread.sleep(1000);
                        if (!clientCommand.equals("")) {

                            logger.info("Client start writing in channel...");

                            clientCommand = name + ": " + clientCommand;
                            oos.write(clientCommand + "\n");
                            oos.flush();

                            if (clientCommand.equalsIgnoreCase("quit")) {

                                logger.info("Client kill connections");

                                if (ois.read() > -1) {
                                    logger.info("reading...");
                                    String in = ois.readUTF();
                                    System.out.println(in);
                                }


                                break;
                            }
                            clientCommand = "";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private synchronized void runPrinter(BufferedReader reader) {
        new Thread() {
            @Override
            public void run() {
                logger.info("Start printer");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        Thread.sleep(1500);
                        logger.info("reading...");
                        String in = reader.readLine();
                        if (!in.startsWith("$$")) {
                            logger.info("After reading");
                            mapOfMessages.put(idOfMessage, in);
                            String nameOfSender = in.split(":")[0];
                            Rectangle rect = new Rectangle(54, 32 + 23 * idOfMessage, 18, 23);

                            mapOfRectangles.put(idOfMessage, rect);
                            mapOfRectangles.get(idOfMessage).setFill(credentialsDao.getColor(conn, nameOfSender));
                            mapOfRectangles.get(idOfMessage).setVisible(true);
                            observableList.add(new Message(mapOfRectangles.get(idOfMessage), mapOfMessages.get(idOfMessage)));
                            rectColumn.setCellValueFactory(new PropertyValueFactory<Message, Rectangle>("rectangle"));
                            messageColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("text"));
                            showingField.setItems(observableList);
                            idOfMessage++;
                            String tempName = in.split(":")[0].trim();
                            credentialsDao.incrementMessagesAmount(conn, tempName);
                        } else {
                            logger.info("Changing color");
                            String nameOfSender = in.split(" ")[1].trim();
                            switch (in.split(" ")[2].trim()) {
                                case "Black":
                                    for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
                                        if (entry.getValue().split(":")[0].trim().contains(nameOfSender)) {
                                            mapOfRectangles.get(entry.getKey()).setFill(Color.BLACK);
//                                            observableList.remove(idOfMessage);
//                                            observableList.add(new Message(mapOfRectangles.get(idOfMessage), mapOfMessages.get(idOfMessage)));
//                                            mapOfRectangles.get(entry.getKey()).setVisible(true);
                                            credentialsDao.updateColor(conn, nameOfSender, Color.BLACK);
                                        }
                                    }
                                    break;
                                case "Green":
                                    for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
                                        if (entry.getValue().split(":")[0].trim().contains(nameOfSender)) {
                                            mapOfRectangles.get(entry.getKey()).setFill(Color.GREEN);
//                                            observableList.remove(idOfMessage);
//                                            observableList.add(new Message(mapOfRectangles.get(idOfMessage), mapOfMessages.get(idOfMessage)));
//                                            mapOfRectangles.get(entry.getKey()).setVisible(true);
                                            credentialsDao.updateColor(conn, nameOfSender, Color.GREEN);
                                        }
                                    }
                                    break;
                                case "Blue":
                                    for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
                                        if (entry.getValue().split(":")[0].trim().contains(nameOfSender)) {
                                            mapOfRectangles.get(entry.getKey()).setFill(Color.BLUE);
//                                            observableList.remove(idOfMessage);
//                                            observableList.add(new Message(mapOfRectangles.get(idOfMessage), mapOfMessages.get(idOfMessage)));
//                                            mapOfRectangles.get(entry.getKey()).setVisible(true);
                                            credentialsDao.updateColor(conn, nameOfSender, Color.BLUE);
                                        }
                                    }
                                    break;
                                case "Red":
                                    for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
                                        if (entry.getValue().split(":")[0].trim().contains(nameOfSender)) {
                                            mapOfRectangles.get(entry.getKey()).setFill(Color.RED);
//                                            observableList.remove(idOfMessage);
//                                            observableList.add(new Message(mapOfRectangles.get(idOfMessage), mapOfMessages.get(idOfMessage)));
//                                            mapOfRectangles.get(entry.getKey()).setVisible(true);
                                            credentialsDao.updateColor(conn, nameOfSender, Color.RED);
                                        }
                                    }
                                    break;
                            }
                        }


//                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private synchronized void runAll(Socket socket, BufferedWriter oos, DataInputStream ois, BufferedReader reader) {
        runSender(socket, oos, ois);
        runPrinter(reader);
    }


    @FXML
    private void handleButton1Action() {
        logger.info("Send button pressed");
        clientCommand = enteringField.getText();
        enteringField.clear();

    }

    @FXML
    private void onCheckAction() throws SQLException {
        logger.info("Statistic button pressed");
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Users");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Messages amount");

        BarChart gist = new BarChart(xAxis, yAxis);
        XYChart.Series dataSeries1 = new XYChart.Series();
        for (Map.Entry<String, Integer> entry : credentialsDao.getMessagesStatistic(conn).entrySet()) {
            dataSeries1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }
        gist.getData().setAll(dataSeries1);
        Stage stage = new Stage();
        stage.setTitle("");
        stage.setWidth(500);
        stage.setHeight(500);
        Scene scene = new Scene(new Group());

        VBox root = new VBox();


        root.getChildren().addAll(gist);
        scene.setRoot(root);

        stage.setScene(scene);
        stage.show();
        flag = false;

    }

    @FXML
    private void onBlue() throws IOException {
        logger.info("Changing color on blue of " + name);
        flag = true;
        for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
            if ((mapOfRectangles.get(entry.getKey()).getFill()).equals(Color.BLUE)) {
                showColorIsUsedMessage();
            }
        }
        if (flag) {
            writerForButtons.write("$$ " + name + " Blue\n");
            writerForButtons.flush();
        }
    }

    @FXML
    private void onRed() throws IOException {
        logger.info("Changing color on red of " + name);
        flag = true;
        for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
            if ((mapOfRectangles.get(entry.getKey()).getFill()).equals(Color.RED)) {
                showColorIsUsedMessage();
            }
        }
        if (flag) {
            writerForButtons.write("$$ " + name + " Red\n");
            writerForButtons.flush();
        }
    }

    @FXML
    private void onBlack() throws IOException {
        logger.info("Changing color on black of " + name);
        flag = true;
        for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
            if ((mapOfRectangles.get(entry.getKey()).getFill()).equals(Color.BLACK)) {
                showColorIsUsedMessage();
            }
        }
        if (flag) {
            writerForButtons.write("$$ " + name + " Black\n");
            writerForButtons.flush();
        }
    }

    @FXML
    private void onGreen() throws IOException {
        logger.info("Changing color on green of " + name);
        flag = true;
        for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
            if ((mapOfRectangles.get(entry.getKey()).getFill()).equals(Color.GREEN)) {
                showColorIsUsedMessage();
            }
        }
        if (flag) {
            writerForButtons.write("$$ " + name + " Green\n");
            writerForButtons.flush();
        }
    }

    private void showColorIsUsedMessage() {
        logger.info("Showing color is used message");
        Stage st = new Stage();
        st.initModality(Modality.APPLICATION_MODAL);
        st.initOwner(stage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("Color is used"));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        st.setScene(dialogScene);
        st.show();
        flag = false;
    }

    public void setLoginScene() {
        logger.info("Setting login scene");
        button.setVisible(false);
        showingField.setVisible(false);
        enteringField.setVisible(false);
        check.setVisible(false);
        r1.setVisible(false);
        r2.setVisible(false);
        r3.setVisible(false);
        r4.setVisible(false);
        r5.setVisible(false);
        r6.setVisible(false);
        r7.setVisible(false);
        r8.setVisible(false);
        r9.setVisible(false);
        r10.setVisible(false);
        r11.setVisible(false);
        r12.setVisible(false);
        greenB.setVisible(false);
        blueB.setVisible(false);
        blackB.setVisible(false);
        redB.setVisible(false);
        buttonIp.setVisible(false);
        textIp.setVisible(false);
        enterIp.setVisible(false);
    }

    public void setIpChoseScene() {
        logger.info("Setting ip chose scene");
        loginField.setVisible(false);
        passField.setVisible(false);
        buttonLogin.setVisible(false);
        text1.setVisible(false);
        text2.setVisible(false);
        buttonIp.setVisible(true);
        textIp.setVisible(true);
        enterIp.setVisible(true);
    }

    public void setChatScene() {
        logger.info("Set chat scene");
        check.setVisible(true);
        button.setVisible(true);
        showingField.setVisible(true);
        enteringField.setVisible(true);
        greenB.setVisible(true);
        blueB.setVisible(true);
        blackB.setVisible(true);
        redB.setVisible(true);
        buttonIp.setVisible(false);
        textIp.setVisible(false);
        enterIp.setVisible(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
