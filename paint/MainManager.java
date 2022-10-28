package com.example.paint;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;

public class MainManager {


    public enum StrokeWidth {
        THIN,
        MEDIUM,
        WIDE
    }

    private ArrayList<Image> imageArray;

    private HashMap<Integer, Image> autoSaveImagesMap;
    private ParentPaintTool selectedTool;
    private StrokeWidth selectedStrokeWidth;
    private int selectedPolygonSides;
    private Color strokeColor;
    private Color fillColor;
    private ArrayList<Boolean> unsavedChangesArray;
    private int selectedTabIndex;
    private HashMap<Integer, String> saveAsFilePathMap;
    private Preferences preferences;
    public IntegerProperty autoSaveCounter;
    private int autoSaveCounterMax;

    public MainManager() {
        ImageManager imageManager = new ImageManager();
        preferences = Preferences.userRoot().node(this.getClass().getName());
        selectedTool = new ToolMouse();
        selectedStrokeWidth = StrokeWidth.THIN;
        strokeColor = Color.BLACK;
        fillColor = Color.BLACK;
        unsavedChangesArray = new ArrayList<>();
        unsavedChangesArray.add(false);
        saveAsFilePathMap = new HashMap<>();
        autoSaveImagesMap = new HashMap<>();
        selectedTabIndex = 0;
        selectedPolygonSides = 5;

        autoSaveCounterMax = getCounterValueFromPreferences();
        autoSaveCounter = new SimpleIntegerProperty();
        autoSaveCounter.setValue(autoSaveCounterMax);

        configureTimer();

        imageArray = new ArrayList<>();
        imageArray.add(selectedTabIndex, imageManager.getLogoImage());
    }

    //getters and setters

    public void setMainImageInCurrentTab(Image image) {
        imageArray.add(selectedTabIndex, image);
    }

    public void setSelectedTool(ParentPaintTool tool) {
        selectedTool = tool;

    }

    public void setStrokeColor(Color color) {
        strokeColor = color;
    }

    public void setFillColor(Color color) {
        fillColor = color;
    }

    public void setSelectedStrokeWidth(StrokeWidth width) {
        selectedStrokeWidth = width;
    }

    public void setSelectedPolygonSides(int sides) {
        selectedPolygonSides = sides;
    }

    public void setSaveAsFilePathForCurrentTab(String filePath) {
        saveAsFilePathMap.put(selectedTabIndex, filePath);
    }

    public void setAutoSaveImageForCurrentTab(Image image) {
        autoSaveImagesMap.put(selectedTabIndex, image);
    }

    public void setSelectedTabIndex(int index) {
        selectedTabIndex = index;
    }
    public void setAutoSaveCounterMax(int newValue) {
        autoSaveCounterMax = newValue;
        autoSaveCounter.setValue(autoSaveCounterMax);
        saveCounterToPreferences();
    }


    public final void setAutoSaveCounter(int value) { autoSaveCounter.set(value); }

    public void setUnsavedChangeCurrently(boolean hasUnsavedChanges) {
        unsavedChangesArray.set(selectedTabIndex, hasUnsavedChanges);
    }


    public void addTabToUnsavedChangedArray() {
        unsavedChangesArray.add(false);
    }

    public Image getImageFromCurrentTab() {
        return imageArray.get(selectedTabIndex);
    }

    public ParentPaintTool getSelectedTool() {
        return selectedTool;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public StrokeWidth getSelectedStrokeWidth() {
        return selectedStrokeWidth;
    }

    public int getSelectedPolygonSides() {
        return selectedPolygonSides;
    }

    public String getSaveAsFilePathForCurrentTab() {
        return saveAsFilePathMap.get(selectedTabIndex);
    }

    public Image getAutoSaveImageForCurrentTab() {
        return autoSaveImagesMap.get(selectedTabIndex);
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }

    public final int getAutoSaveCounter() {
        return autoSaveCounter.get();
    }

    public int getAutoSaveCounterMax() {
        return autoSaveCounterMax;
    }

    public IntegerProperty autoSaveCounterProperty() {
        return autoSaveCounter;
    }

    public boolean hasUnsavedChangesAtIndex(int index) {
        return unsavedChangesArray.get(index);
    }

    private void configureTimer() {
        // decrement counter every 1 second
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                event -> decrementCounter()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void decrementCounter() {
        if (autoSaveCounter.getValue() == 0) {
            setAutoSaveCounter(autoSaveCounterMax);
        } else {
            setAutoSaveCounter(autoSaveCounter.getValue() - 1);
        }
    }

    private void saveCounterToPreferences() {
        preferences.putInt(LogoImages.TIMER_PREFS_KEY, autoSaveCounterMax);
    }


    private int getCounterValueFromPreferences() {
        return preferences.getInt(LogoImages.TIMER_PREFS_KEY, 120);
    }
}
