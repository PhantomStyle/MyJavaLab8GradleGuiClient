package sample;

import javafx.scene.shape.Rectangle;

public class Message {
    private Rectangle rectangle;
    private String text;

    public Message(Rectangle rectangle, String text) {
        this.rectangle = rectangle;
        this.text = text;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return rectangle + text;
    }
}
