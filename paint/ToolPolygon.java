package com.example.paint;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class ToolPolygon extends ToolShape{

    @Override
    protected DrawingShapes createShapeOperation(GraphicsContext graphicsContext) {
        MainCanvas canvas = (MainCanvas) graphicsContext.getCanvas();
        int nPoints = canvas.getMainManager().getSelectedPolygonSides();
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();

        DrawingPolygon polygonOp = new DrawingPolygon(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor, nPoints);
        return polygonOp;
    }

    public String toString() {
        return "Polygon tool";
    }

}
