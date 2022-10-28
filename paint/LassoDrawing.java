package com.example.paint;

import javafx.scene.paint.Color;

public class LassoDrawing extends DrawingRectangles{

    DrawingImage imageDrawOperation;

    public LassoDrawing(double x, double y, double width, double height) {
        super(x, y, width, height, 0, Color.TRANSPARENT, Color.TRANSPARENT);
    }

    public void setImageDrawOperation(DrawingImage operation) {
        imageDrawOperation = operation;
    }
}
