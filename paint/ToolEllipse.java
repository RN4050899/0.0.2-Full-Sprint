package com.example.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class ToolEllipse extends ToolShape {
    @Override
    protected DrawingShapes createShapeOperation(GraphicsContext graphicsContext) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();
        DrawingEllipse ovalOp = new DrawingEllipse(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor);
        return ovalOp;
    }

    public String toString() {
        return "Oval tool";
    }
}
