package com.example.paint;

public class CopyDrag extends ToolLasso {
    protected LassoDrawing createLassoDrawOperation(){
        return new DrawingCopyPaste(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight);
    }
    public String toString() {
        return "Copy and Drag tool";
    }
}
