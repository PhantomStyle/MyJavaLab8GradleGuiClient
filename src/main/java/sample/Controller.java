package sample;

public class Controller {

    private Main main;

    public Controller(){
        main = new Main();
    }


    public void setLoginScene(){
        main.button.setVisible(false);
        main.showingField.setVisible(false);
        main.enteringField.setVisible(false);
        main.check.setVisible(false);
        main.gist.setVisible(false);
        main.r1.setVisible(false);
        main.r2.setVisible(false);
        main.r3.setVisible(false);
        main.r4.setVisible(false);
        main.r5.setVisible(false);
        main.r6.setVisible(false);
        main.r7.setVisible(false);
        main.r8.setVisible(false);
        main.r9.setVisible(false);
        main.r10.setVisible(false);
        main.r11.setVisible(false);
        main.r12.setVisible(false);
        main.greenB.setVisible(false);
        main.blueB.setVisible(false);
        main.blackB.setVisible(false);
        main.redB.setVisible(false);
        main.buttonIp.setVisible(false);
        main.textIp.setVisible(false);
        main.enterIp.setVisible(false);
    }

    public void setIpChoseScene(){
        main.loginField.setVisible(false);
        main.passField.setVisible(false);
        main.buttonLogin.setVisible(false);
        main.text1.setVisible(false);
        main.text2.setVisible(false);
        main.buttonIp.setVisible(true);
        main.textIp.setVisible(true);
        main.enterIp.setVisible(true);
    }

    public void setChatScene(){
        main.check.setVisible(true);
        main.button.setVisible(true);
        main.showingField.setVisible(true);
        main.enteringField.setVisible(true);
        main.greenB.setVisible(true);
        main.blueB.setVisible(true);
        main.blackB.setVisible(true);
        main.redB.setVisible(true);
        main.buttonIp.setVisible(false);
        main.textIp.setVisible(false);
        main.enterIp.setVisible(false);
    }
}
