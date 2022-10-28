package com.example.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class DrawingRoundedRect extends DrawingRectangles{
    public DrawingRoundedRect(double x, double y, double width, double height, double lineWidth, Paint strokeColor, Paint fillColor) {
        super(x, y, width, height, lineWidth, strokeColor, fillColor);
    }

    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);
        graphicsContext.setLineWidth(scaledLineWidth);
        graphicsContext.setStroke(strokeColor);
        graphicsContext.setFill(fillColor);
        graphicsContext.fillRoundRect(scaledX, scaledY, scaledWidth, scaledHeight, scaledWidth/2, scaledHeight/2);
        graphicsContext.strokeRoundRect(scaledX, scaledY, scaledWidth, scaledHeight, scaledWidth/2, scaledHeight/2);
    }

}
