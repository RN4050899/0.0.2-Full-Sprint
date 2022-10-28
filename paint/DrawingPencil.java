package com.example.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import java.util.ArrayList;

public class DrawingPencil implements Drawing{

    ArrayList<Double> relativeXValues;
    ArrayList<Double> relativeYValues;
    double relativeLineWidth;
    Paint strokeColor;

    double scaledLineWidth;

    public DrawingPencil(ArrayList<Double> xValues, ArrayList<Double> yValues, Paint strokeColor, double lineWidth) {
        relativeXValues = xValues;
        relativeYValues = yValues;
        this.strokeColor = strokeColor;
        this.relativeLineWidth = lineWidth;
    }

    public void draw(GraphicsContext graphicsContext) {

        double width = graphicsContext.getCanvas().getWidth();
        double height = graphicsContext.getCanvas().getHeight();
        scaledLineWidth = relativeLineWidth * width;

        graphicsContext.setLineWidth(scaledLineWidth);
        graphicsContext.setStroke(strokeColor);

        // stroke between all the points
        for (int i = 0; i < relativeXValues.size() - 1; i++) {
            graphicsContext.beginPath();

            // scale to current canvas size
            double startX = relativeXValues.get(i) * width;
            double startY = relativeYValues.get(i) * height;

            double endX = relativeXValues.get(i + 1) * width;
            double endY = relativeYValues.get(i + 1) * height;

            graphicsContext.moveTo(startX, startY);
            graphicsContext.lineTo(endX, endY);
            graphicsContext.stroke();
        }
    }

}
