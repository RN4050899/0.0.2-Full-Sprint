package com.example.paint;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ImageManager {
    public Image getLogoImage() {
        return getImageFromAssets(LogoImages.assetPath);
    }

    public Image getImageFromAssets(String pathToAsset) {
        Image image = null;
        try {
            image = new Image(pathToAsset);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return image;
    }

    public Image getImageFromFilePath(String file) {
        Image image = null;
        try {
            image = new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return image;
    }

    public WritableImage getSnapshotImageToSave(MainCanvas canvas) {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        //Take snapshot of the scene

        WritableImage writableImage = canvas.snapshot(params, null);
        return  writableImage;
    }


    public BufferedImage getBufferedImageForJPG(WritableImage image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        BufferedImage convertedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        convertedImage.getGraphics().drawImage(bufferedImage, 0, 0, null);
        return convertedImage;
    }
}
