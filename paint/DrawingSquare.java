package com.example.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class DrawingSquare extends DrawingRectangles{
    public DrawingSquare(double x, double y, double width, double height, double lineWidth, Paint strokeColor, Paint fillColor) {
        super(x, y, width, height, lineWidth, strokeColor, fillColor);
    }

    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);
        graphicsContext.setLineWidth(scaledLineWidth);
        graphicsContext.setStroke(strokeColor);
        graphicsContext.setFill(fillColor);
        // use width only to avoid bug involving aspect ratio distortion of square
        graphicsContext.fillRect(scaledX, scaledY, scaledWidth, scaledWidth);
        graphicsContext.strokeRect(scaledX, scaledY, scaledWidth, scaledWidth);
    }

}
