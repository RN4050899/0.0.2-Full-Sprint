package com.example.paint;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolShapeTest {
    @Test
    void calculateRelativeShapeParameters(){
        ToolShape tool = new ToolRect();
        Canvas canvas = new Canvas(200,200);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        tool.startX = 100;
        tool.startY = 100;

        tool.calculateRelativeShapeParameters(50, 50, gc);
        assertEquals(50.0 / 200.0, tool.relativeTopLeftX);
        assertEquals(50.0 / 200.0, tool.relativeTopLeftY);


    }

}