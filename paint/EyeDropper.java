package com.example.paint;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class EyeDropper extends ParentPaintTool{

    ColorPicker colorPicker;
    MainManager mainManager;

    public EyeDropper(ColorPicker picker, MainManager mainManager) {
        super();
        colorPicker = picker;
        this.mainManager = mainManager;
    }

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        // take a snapshot of the canvas
        Canvas canvas = graphicsContext.getCanvas();
        WritableImage snap = canvas.snapshot(null, null);
        // read color of pixel at coords
        Color color = snap.getPixelReader().getColor((int)e.getX(), (int)e.getY());
        colorPicker.setValue(color);
        mainManager.setStrokeColor(color);
    }

    public String toString() {
        return "Color Grab tool";
    }
}
