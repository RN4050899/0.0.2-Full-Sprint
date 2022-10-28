package com.example.paint;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Eraser extends ToolPencil {
    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateScaledLineParameters(e, graphicsContext);
        // add current point to array
        xValues.add(relativeX);
        yValues.add(relativeY);

        // draw the new line segment
        Drawing segmentOperation = createEraseSegmentOperation(graphicsContext);
        segmentOperation.draw(graphicsContext);

        // current coords are start of the next segment
        startX = relativeX;
        startY = relativeY;
    }

        @Override
        protected Drawing createDrawOperation(GraphicsContext graphicsContext) {
            Paint color = Color.WHITE;
            relativeLineWidth = 10 / graphicsContext.getCanvas().getWidth();
            DrawingPencil pencilOp = new DrawingPencil(xValues, yValues, color, relativeLineWidth);
            return pencilOp;
        }

        protected Drawing createEraseSegmentOperation(GraphicsContext graphicsContext) {
            Paint color = Color.WHITE;
            relativeLineWidth = 10 / graphicsContext.getCanvas().getWidth();
            DrawingLine lineOp = new DrawingLine(startX, startY, relativeX, relativeY, color, relativeLineWidth);
            return lineOp;
        }

        public String toString() {
            return "Eraser tool";
        }
    }
