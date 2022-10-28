package com.example.paint;

import javafx.scene.canvas.GraphicsContext;


public class DrawingCutAndDrag extends LassoDrawing{
    public DrawingCutAndDrag(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);

        // clear space from original selection
        graphicsContext.clearRect(scaledX, scaledY, scaledWidth, scaledHeight);

        // redraw image in new location
        if (imageDrawOperation != null) {
            imageDrawOperation.draw(graphicsContext);
        }
    }
}
