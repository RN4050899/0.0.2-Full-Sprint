package com.example.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public abstract class DrawingShapes implements Drawing{
    double rTopLeftX;
    double rTopLeftY;
    double rWidth;
    double rHeight;
    double rLineWidth;
    Paint strokeColor;
    Paint fillColor;

    double scaledX;
    double scaledY;
    double scaledWidth;
    double scaledHeight;
    double scaledLineWidth;

    public DrawingShapes(double x, double y, double width, double height, double lineWidth, Paint strokeColor, Paint fillColor) {
        rTopLeftX = x;
        rTopLeftY = y;
        rWidth = width;
        rHeight = height;
        rLineWidth = lineWidth;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
    }
    public abstract void draw(GraphicsContext graphicsContext);

    protected void scaleParamsToCanvasSize(GraphicsContext graphicsContext) {
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();

        // get absolute co-ords by multiplying by current dimensions
        scaledX = rTopLeftX * canvasWidth;
        scaledY = rTopLeftY * canvasHeight;
        scaledWidth = rWidth * canvasWidth;
        scaledHeight = rHeight * canvasHeight;
        scaledLineWidth = rLineWidth * canvasWidth;
    }

}


