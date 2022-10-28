package com.example.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class ToolRect extends ToolShape {

    @Override
    protected DrawingShapes createShapeOperation(GraphicsContext graphicsContext) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();
        DrawingRectangles rect1 = new DrawingRectangles(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor);
        return rect1;
    }
}
