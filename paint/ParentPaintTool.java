package com.example.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 *the parent that many other tools will extend off of *
 */

public class ParentPaintTool {

    //The parent tool that other classes will be extending from

    Boolean  makeChangesToCanvas = false;

    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) { }
    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) { }
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) { }

    public String toString() {
        return "PaintFx tool";
    }

}
