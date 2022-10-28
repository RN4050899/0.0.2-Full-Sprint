package com.example.paint;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * This is the main tool that many other general shapes will use
 * * *
 */

public abstract class ToolShape extends ParentPaintTool {

    double startX;
    double startY;

    double relativeTopLeftX;
    double relativeTopLeftY;
    double relativeWidth;
    double relativeHeight;
    double relativeLineWidth;

    public ToolShape() {
        super();
        makeChangesToCanvas = true;
    }

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        startX = e.getX();
        startY = e.getY();
    }

    @Override
    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateRelativeShapeParameters(e.getX(), e.getY(), graphicsContext);
        MainCanvas canvas = (MainCanvas) graphicsContext.getCanvas();
        DrawingShapes operation = createShapeOperation(graphicsContext);
        operation.draw(graphicsContext);
        canvas.pushUndoStack(operation);
    }

    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateRelativeShapeParameters(e.getX(), e.getY(), graphicsContext);
        MainCanvas canvas = (MainCanvas) graphicsContext.getCanvas();
        DrawingShapes operation = createShapeOperation(graphicsContext);
        canvas.drawImageOnCanvas();
        canvas.reDraw();
        operation.draw(graphicsContext);
    }

    protected void calculateRelativeShapeParameters(double endX, double endY, GraphicsContext graphicsContext) {
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();
        double xDifference = endX - startX;
        double yDifference = endY - startY;
        double x = (xDifference > 0) ? startX : endX;
        double y = (yDifference > 0) ? startY : endY;
        relativeTopLeftX = x / canvasWidth;
        relativeTopLeftY = y / canvasHeight;
        relativeWidth = Math.abs(endX - startX) / canvasWidth;
        relativeHeight = Math.abs(endY - startY) / canvasHeight;

        relativeLineWidth = graphicsContext.getLineWidth() / canvasWidth;
    }

        protected abstract DrawingShapes createShapeOperation(GraphicsContext graphicsContext);
}
