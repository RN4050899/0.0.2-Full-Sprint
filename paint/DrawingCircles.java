package com.example.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;


public class DrawingCircles extends DrawingEllipse{
    public DrawingCircles(double x, double y, double width, double height, double lineWidth, Paint strokeColor, Paint fillColor) {
        super(x, y, width, height, lineWidth, strokeColor, fillColor);
    }

    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);
        graphicsContext.setLineWidth(scaledLineWidth);
        graphicsContext.setStroke(strokeColor);
        graphicsContext.setFill(fillColor);
        // only use width to solve bug with aspect ratio distortion of circle
        graphicsContext.fillOval(scaledX, scaledY, scaledWidth, scaledWidth);
        graphicsContext.strokeOval(scaledX, scaledY, scaledWidth, scaledWidth);
    }

}
