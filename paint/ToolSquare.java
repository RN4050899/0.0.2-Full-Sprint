package com.example.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class ToolSquare extends ToolRect{
    @Override
    protected DrawingShapes createShapeOperation(GraphicsContext graphicsContext) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();
        double sideLength = Math.max(relativeWidth, relativeHeight);
        DrawingSquare squareOp = new DrawingSquare(relativeTopLeftX, relativeTopLeftY, sideLength, sideLength,
                relativeLineWidth, strokeColor, fillColor);
        return squareOp;
    }
    public String toString() {
        return "Square tool";
    }

}
