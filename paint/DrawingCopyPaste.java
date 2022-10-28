package com.example.paint;

import javafx.scene.canvas.GraphicsContext;

public class DrawingCopyPaste extends LassoDrawing{
    public DrawingCopyPaste(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);

        // redraw image in new location
        if (imageDrawOperation != null) {
            imageDrawOperation.draw(graphicsContext);
        }
    }
}
