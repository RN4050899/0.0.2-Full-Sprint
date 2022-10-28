package com.example.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class ToolRoundedRect extends ToolRect{

    @Override
    protected DrawingShapes createShapeOperation(GraphicsContext graphicsContext) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();
        DrawingRectangles rectOp = new DrawingRoundedRect(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor);
        return rectOp;
    }

    public String toString() {
        return "Rounded Rectangle tool";
    }
}
