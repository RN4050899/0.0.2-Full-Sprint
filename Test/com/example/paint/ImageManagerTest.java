package com.example.paint;

import org.junit.jupiter.api.Test;
import javafx.scene.image.Image;


import static org.junit.jupiter.api.Assertions.*;

class ImageManagerTest {
    @Test
    void getLogoImage() {
        ImageManager imageManager = new ImageManager();
        Image logo = imageManager.getLogoImage();
        assertNotNull(logo);
    }

}