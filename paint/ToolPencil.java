package com.example.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class ToolPencil extends ToolLine {
    protected ArrayList<Double> xValues;
    protected ArrayList<Double> yValues;

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        super.onMousePressed(e, graphicsContext);

        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        xValues.add(startX);
        yValues.add(startY);
    }

    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateScaledLineParameters(e, graphicsContext);
        // add current point to array
        xValues.add(relativeX);
        yValues.add(relativeY);

        // draw the new line segment (using LineTool's operation method for the new segment)
        Drawing segmentOperation = super.createDrawOperation(graphicsContext);
        segmentOperation.draw(graphicsContext);

        // current coords are start of the next segment
        startX = relativeX;
        startY = relativeY;
    }

    @Override
    protected Drawing createDrawOperation(GraphicsContext graphicsContext) {
        Paint color = graphicsContext.getStroke();
        DrawingPencil pencil1 = new DrawingPencil(xValues, yValues, color, relativeLineWidth);
        return pencil1;
    }

    public String toString() {
        return "Pencil tool";
    }

}
