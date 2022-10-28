package com.example.paint;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolbarPainTest {
    private JFXPanel panel = new JFXPanel();

    @Test
    void getTimerString() {
        MainManager stateManager = new MainManager();
        ToolbarPain toolbar = new ToolbarPain(stateManager);

        String result = toolbar.getTimerString(122);
        assertEquals("2:02", result);

        result = toolbar.getTimerString(131);
        assertEquals("2:11", result);

        result = toolbar.getTimerString(0);
        assertEquals("0:00", result);

        result = toolbar.getTimerString(119);
        assertEquals("1:59", result);
    }

}