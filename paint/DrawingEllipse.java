package com.example.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class DrawingEllipse extends DrawingShapes {

    public DrawingEllipse(double x, double y, double width, double height, double lineWidth, Paint strokeColor, Paint fillColor) {
        super(x, y, width, height, lineWidth, strokeColor, fillColor);
    }

    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);
        graphicsContext.setLineWidth(scaledLineWidth);
        graphicsContext.setStroke(strokeColor);
        graphicsContext.setFill(fillColor);
        graphicsContext.fillOval(scaledX, scaledY, scaledWidth, scaledHeight);
        graphicsContext.strokeOval(scaledX, scaledY, scaledWidth, scaledHeight);
    }
}
