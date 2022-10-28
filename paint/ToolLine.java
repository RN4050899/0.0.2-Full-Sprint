package com.example.paint;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

public class ToolLine extends ParentPaintTool{
    double startX;
    double startY;

    double relativeX;
    double relativeY;
    double relativeLineWidth;

    public ToolLine() {
        super();
        makeChangesToCanvas = true;
    }

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        // get the x/y co-ords scaled to the dimensions of canvas
        startX = e.getX() / graphicsContext.getCanvas().getWidth();
        startY = e.getY() / graphicsContext.getCanvas().getHeight();
    }

    @Override
    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        MainCanvas canvas = (MainCanvas) graphicsContext.getCanvas();
        calculateScaledLineParameters(e, graphicsContext);
        Drawing operation = createDrawOperation(graphicsContext);
        // add operation to array for undo/redo and scaling
        canvas.pushUndoStack(operation);
        canvas.drawImageOnCanvas();
        canvas.reDraw();
    }
    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateScaledLineParameters(e, graphicsContext);
        Drawing operation = createDrawOperation(graphicsContext);
        MainCanvas canvas = (MainCanvas) graphicsContext.getCanvas();

        // draw but don't save to operations array (drawing not final until mouse release)
        canvas.drawImageOnCanvas();
        canvas.reDraw();
        operation.draw(graphicsContext);
    }

    protected void calculateScaledLineParameters(MouseEvent e, GraphicsContext graphicsContext) {
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();

        // get the x/y co-ords and line width scaled to the dimensions of canvas
        relativeX = e.getX() / canvasWidth;
        relativeY = e.getY() / canvasHeight;
        relativeLineWidth = graphicsContext.getLineWidth() / canvasWidth;
    }

    protected Drawing createDrawOperation(GraphicsContext graphicsContext) {
        Paint color = graphicsContext.getStroke();
        DrawingLine lineOp = new DrawingLine(startX, startY, relativeX, relativeY, color, relativeLineWidth);
        return lineOp;
    }

    public String toString() {
        return "Line tool";
    }


}
