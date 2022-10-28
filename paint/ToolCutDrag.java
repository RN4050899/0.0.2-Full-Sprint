package com.example.paint;


public class ToolCutDrag extends ToolLasso{
    protected LassoDrawing createLassoDrawOperation() {
        return new DrawingCutAndDrag(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight);
    }

    public String toString() {
        return "Cut and Drag tool";
    }
}
