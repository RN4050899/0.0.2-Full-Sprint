package com.example.paint;

import javafx.scene.control.ScrollBar;


public class ScrollbarPain extends ScrollBar{
    public ScrollbarPain(){
        super();
    }

    public void updateScrollBarBounds(double actualMin, double actualMax, double intendedMin, double intendedMax) {

        double newScrollBarMin = -(intendedMin - actualMin);
        double newScrollBarMax = actualMax - intendedMax;
        setScrollBarBounds(newScrollBarMin, newScrollBarMax);
    }

    private void setScrollBarBounds(double min, double max) {
        setMin(min);
        setMax(max);
        setValue(0);

        // scrollbar thumb takes up half of the current range
        double range = max - min;
        setVisibleAmount(range / 2);
    }
}


