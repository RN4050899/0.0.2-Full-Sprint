package com.example.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class DrawingPolygon extends DrawingShapes{

    private double[] xPoints;
    private double[] yPoints;

    private final int nPoints;


    public DrawingPolygon(double x, double y, double width, double height, double lineWidth, Paint strokeColor, Paint fillColor, int nPoints) {
        super(x, y, width, height, lineWidth, strokeColor, fillColor);
        this.nPoints = nPoints;
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);
        calculatePolygonPoints();
        graphicsContext.setLineWidth(scaledLineWidth);
        graphicsContext.setStroke(strokeColor);
        graphicsContext.setFill(fillColor);
        graphicsContext.fillPolygon(xPoints, yPoints, nPoints);
        graphicsContext.strokePolygon(xPoints, yPoints, nPoints);
    }

    private void calculatePolygonPoints() {
        xPoints = new double[nPoints];
        yPoints = new double[nPoints];

        // coords of center of polygon
        double scaledCenterX = scaledX + (scaledWidth / 2);
        double scaledCenterY = scaledY + (scaledHeight / 2);

        double radius = scaledWidth / 2;
        double segmentAngle = (2 * Math.PI) / nPoints;

        for (int i = 0; i < nPoints; i++) {
            double theta = i * segmentAngle;
            // r*cos(theta) and r*sin(theta) calculate coords relative to (0,0)
            // have to translate this based on the actual center coords
            double x = radius * Math.cos(theta) + scaledCenterX;
            double y = radius * Math.sin(theta) + scaledCenterY;
            xPoints[i] = x;
            yPoints[i] = y;
        }
    }

}
