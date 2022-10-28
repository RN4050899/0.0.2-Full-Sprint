package com.example.paint;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileManager {
    private Stage stage;
    private MainManager MainManager;

    //Image filters

    private final FileChooser.ExtensionFilter imageExtensionFilter = new FileChooser.ExtensionFilter("Image files",
            "*.png", "*.PNG", "*.jpg", "*.jpeg", "*.bmp");
    private final FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter(".png",
            "*.png", "*.PNG");
    private final FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter(".jpg",
            "*.jpg", "*.jpeg");
    private final FileChooser.ExtensionFilter bmpFilter = new FileChooser.ExtensionFilter(".bmp",
            "*.bmp");

    public FileManager(Stage stage, MainManager MainManager) {
        this.stage = stage;
        this.MainManager = MainManager;
    }


    public String getImageFilePathFromUser(Stage stage) {
        String path = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().add(imageExtensionFilter);
        fileChooser.getExtensionFilters().add(pngFilter);
        fileChooser.getExtensionFilters().add(jpgFilter);
        fileChooser.getExtensionFilters().add(bmpFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            path = file.getAbsolutePath();
        }
        return path;
    }

    public void saveImageAs(WritableImage image) {
        String filePath = getSaveAsFilePathFromUser();
        if (filePath != null) {
            saveImageToFilePath(image, filePath);
            MainManager.setSaveAsFilePathForCurrentTab(filePath);
        }
    }

    public void saveImage(WritableImage image) {
        String filePath = MainManager.getSaveAsFilePathForCurrentTab();
        if (filePath != null) {
            saveImageToFilePath(image, filePath);
        } else {
            saveImageAs(image);
        }
    }


    private String getSaveAsFilePathFromUser() {
        String path = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().add(pngFilter);
        fileChooser.getExtensionFilters().add(jpgFilter);
        fileChooser.getExtensionFilters().add(bmpFilter);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            path = file.getAbsolutePath();
        }
        return path;
    }



    private void saveImageToFilePath(WritableImage image, String filePath) {
        File outFile = new File(filePath);
        try {
            if (filePath.endsWith("png")) {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null),
                        "png", outFile);
            } else {
                // have to convert file formats to save as JPG or BMP
                ImageManager imageManager = new ImageManager();
                BufferedImage convertedImage = imageManager.getBufferedImageForJPG(image);

                if (filePath.endsWith("jpg")) {
                    ImageIO.write(convertedImage, "jpg", outFile);
                } else if (filePath.endsWith("bmp")) {
                    ImageIO.write(convertedImage, "bmp", outFile);
                }
            }
            MainManager.setUnsavedChangeCurrently(false);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
