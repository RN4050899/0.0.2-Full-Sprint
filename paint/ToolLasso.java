package com.example.paint;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

abstract public class ToolLasso extends ToolShape {
    protected WritableImage selectedSnapshot;
    protected LassoDrawing lassoOperation;
    private LassoMode currentMode = LassoMode.SELECTION;
    double topLeftX;
    double topLeftY;
    double width;
    double height;

    double deltaX;
    double deltaY;




    private enum LassoMode {
        SELECTION,
        DRAGGING
    }

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {

        if (currentMode == LassoMode.SELECTION) {
            // starting coords for selection box
            startX = e.getX();
            startY = e.getY();
        }else if (currentMode == LassoMode.DRAGGING) {
            // only happens after selection box defined
            MainCanvas canvas = (MainCanvas) graphicsContext.getCanvas();
            // topleft x/y were calculated when selection box was drawn
            deltaX = e.getX() - topLeftX;
            deltaY = e.getY() - topLeftY;

            if (deltaX < 0 || deltaY < 0) {
                // clicked outside of prior selection, user starts selecting a new area
                startX = e.getX();
                startY = e.getY();
                currentMode = LassoMode.SELECTION;
            } else {
                // clicked inside selection
                // start drag process
                // lasso operation and snapshot were created at the end of selection process
                canvas.pushUndoStack(lassoOperation);
                canvas.drawImageOnCanvas();
                canvas.reDraw();
                drawTempImage(e, graphicsContext);
            }
        }
    }



    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateRelativeShapeParameters(e.getX(), e.getY(), graphicsContext);
        calculateAbsoluteShapeParameters(e.getX(), e.getY(), graphicsContext);
        MainCanvas canvas = (MainCanvas) graphicsContext.getCanvas();

        if (currentMode == LassoMode.SELECTION) {
            // draw dashed rect for selection
            DrawingShapes operation = createShapeOperation(graphicsContext);
            canvas.drawImageOnCanvas();
            canvas.reDraw();
            operation.draw(graphicsContext);

        } else if (currentMode == LassoMode.DRAGGING) {
            // clear space behind selected snapshot
            canvas.drawImageOnCanvas();
            canvas.reDraw();

            // redraw snapshot on canvas in new location
            drawTempImage(e, graphicsContext);
        }
    }

    @Override
    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateRelativeShapeParameters(e.getX(), e.getY(), graphicsContext);
        calculateAbsoluteShapeParameters(e.getX(), e.getY(), graphicsContext);
        MainCanvas canvas = (MainCanvas) graphicsContext.getCanvas();

        if (currentMode == LassoMode.SELECTION) {
            // remove dashed rect to take clean snapshot
            canvas.drawImageOnCanvas();
            canvas.reDraw();

            // take snapshot of canvas inside selected area, save for use in dragging mode
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setViewport(getViewportRect(graphicsContext));
            parameters.setFill(Color.TRANSPARENT);
            selectedSnapshot = canvas.snapshot(parameters, null);

            // save the operation of clearing the space left by the snapshot, for later use in dragging mode
            lassoOperation = createLassoDrawOperation();

            // redraw dashed rect
            DrawingShapes dashedRectOperation = createShapeOperation(graphicsContext);
            dashedRectOperation.draw(graphicsContext);

            // toggle mode
            currentMode = LassoMode.DRAGGING;

        } else if (currentMode == LassoMode.DRAGGING) {

            // add image draw operation to lasso operation
            DrawingImage imageDrawOperation = createImageDrawOperation(e, graphicsContext);
            lassoOperation.setImageDrawOperation(imageDrawOperation);

            canvas.drawImageOnCanvas();
            canvas.reDraw();

            // toggle mode
            currentMode = LassoMode.SELECTION;
        }
    }
    abstract protected LassoDrawing createLassoDrawOperation();
    @Override
    protected DrawingShapes createShapeOperation(GraphicsContext graphicsContext) {
        DrawingRectangles rectOp = new DashedRectLasso(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight);
        return rectOp;
    }


    protected DrawingImage createImageDrawOperation(MouseEvent e, GraphicsContext graphicsContext) {
        MainCanvas canvas = (MainCanvas) graphicsContext.getCanvas();
        double relativeSnapshotWidth = selectedSnapshot.getWidth() / canvas.getWidth();
        double relativeSnapshotHeight = selectedSnapshot.getHeight() / canvas.getHeight();
        double relativeX = (e.getX() - deltaX) / canvas.getWidth();
        double relativeY = (e.getY() - deltaY) / canvas.getHeight();
        return new DrawingImage(selectedSnapshot, relativeX, relativeY, relativeSnapshotWidth, relativeSnapshotHeight);
    }

    protected Rectangle2D getViewportRect(GraphicsContext graphicsContext) {
        Bounds canvasBoundsInParent = graphicsContext.getCanvas().getBoundsInParent();

        double topLeftXInParent = topLeftX + canvasBoundsInParent.getMinX();
        double topLeftYInParent = topLeftY + canvasBoundsInParent.getMinY();

        return new Rectangle2D(topLeftXInParent, topLeftYInParent, width, height);
    }


    private void drawTempImage(MouseEvent e, GraphicsContext graphicsContext) {
        if (selectedSnapshot != null) {
            double localX = e.getX() - deltaX;
            double localY = e.getY() - deltaY;
            graphicsContext.drawImage(selectedSnapshot, localX, localY);
        }
    }

    protected void calculateAbsoluteShapeParameters(double endX, double endY, GraphicsContext graphicsContext) {
        double xDifference = endX - startX;
        double yDifference = endY - startY;
        double x = (xDifference > 0) ? startX : endX;
        double y = (yDifference > 0) ? startY : endY;
        topLeftX = x;
        topLeftY = y;

        width = Math.abs(endX - startX);
        height = Math.abs(endY - startY);
    }

    public String toString() {
        return "Lasso tool";
    }



}
