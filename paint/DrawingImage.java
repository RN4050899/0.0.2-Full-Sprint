package com.example.paint;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


public class DrawingImage extends DrawingShapes{
    WritableImage image;

    public DrawingImage(WritableImage image, double x, double y, double width, double height) {
        super(x, y, width, height, 0, Color.TRANSPARENT, Color.TRANSPARENT);
        this.image = image;
    }

    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);
        graphicsContext.drawImage(image, scaledX, scaledY, scaledWidth, scaledHeight);
    }

}
