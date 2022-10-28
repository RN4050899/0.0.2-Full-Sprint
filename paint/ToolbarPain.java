package com.example.paint;

import javafx.scene.control.ToolBar;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.HashMap;

public class ToolbarPain extends ToolBar {
    private MainManager mainManager;
    private VBox toolVbox;
    private ColorPicker strokeColorPicker;
    private ColorPicker fillColorPicker;
    private Slider polygonSlider;
    private Slider timerSlider;
    private HBox timerLabelHBox;
    private Label timerLabel;
    private Label autoSaveLabel;
    private boolean timerLabelHidden = false;

    private ToolShape selectedShapeTool;
    private ComboBox shapeToolComboBox;
    private HashMap<String, ToolShape> shapeToolsMap;

    private ToolShape selectedLassoTool;
    private ComboBox lassoToolComboBox;
    private HashMap<String, ToolLasso> lassoToolsMap;

    ToggleGroup toggleGroup;
    ToggleButton shapeToolButton;
    ToggleButton lassoButton;

    public ToolbarPain(MainManager mainManager){
        super();
        this.mainManager = mainManager;
        initilize();

    }
    private void initilize() {
        // container for toolbar elements
        // constructor param is default space between elements
        toolVbox = new VBox(5);
        this.getItems().add(toolVbox);
        this.setOrientation(Orientation.VERTICAL);
        configShapeToolMap();
        configLassoToolMap();
        configPolygonSlider();
        configShapeToolComboBox();
        configLassoToolComboBox();
        configToggleButtons();
        configStrokeWidthComboBox();
        configColorPickers();
        configTimerSection();
        configTimerListener();

        // initial value
        selectedShapeTool = shapeToolsMap.get(shapeToolComboBox.getValue());
        selectedLassoTool = lassoToolsMap.get(lassoToolComboBox.getValue());
    }
    private void configShapeToolMap() {
        shapeToolsMap = new HashMap<>();
        shapeToolsMap.put(LogoImages.Square, new ToolSquare());
        shapeToolsMap.put(LogoImages.Rectangle, new ToolRect());
        shapeToolsMap.put(LogoImages.RoundedRect, new ToolRoundedRect());
        shapeToolsMap.put(LogoImages.Circle, new ToolCircle());
        shapeToolsMap.put(LogoImages.Ellipse, new ToolEllipse());
        shapeToolsMap.put(LogoImages.Polygon, new ToolPolygon());
    }

    private void configLassoToolMap() {
        lassoToolsMap = new HashMap<>();
        lassoToolsMap.put(LogoImages.CutAndDrag, new ToolCutDrag());
        lassoToolsMap.put(LogoImages.CopyAndDrag, new CopyDrag());
    }

    private void configPolygonSlider() {
        polygonSlider = new Slider();
        polygonSlider.setMin(3);
        polygonSlider.setMax(13);
        polygonSlider.setValue(5);
        polygonSlider.setMajorTickUnit(2);
        polygonSlider.setMinorTickCount(1);
        polygonSlider.setShowTickLabels(true);
        polygonSlider.setShowTickMarks(true);
        polygonSlider.setSnapToTicks(true);
        polygonSlider.valueProperty().addListener((options, oldValue, newValue) -> mainManager.setSelectedPolygonSides((int)polygonSlider.getValue()));
    }

    private ImageView getButtonIcon(String filepath) {
        ImageManager imageManager = new ImageManager();
        Image buttonImage = imageManager.getImageFromAssets(filepath);
        ImageView buttonImageView = new ImageView(buttonImage);
        buttonImageView.setPreserveRatio(true);
        buttonImageView.setFitHeight(LogoImages.IconHeight);
        return buttonImageView;
    }

    private void configToggleButtons() {
        // toggle group allows selection of only one button at a time
        toggleGroup = new ToggleGroup();
        ToggleButton mouseButton = new ToggleButton();
        ToggleButton pencilButton = new ToggleButton();
        ToggleButton eraseButton = new ToggleButton();
        ToggleButton lineButton = new ToggleButton();
        shapeToolButton = new ToggleButton();
        ToggleButton textButton = new ToggleButton();
        lassoButton = new ToggleButton();
        ToggleButton colorGrabButton = new ToggleButton();

        // set icons
        mouseButton.setGraphic(getButtonIcon(LogoImages.CursorIcon));
        pencilButton.setGraphic(getButtonIcon(LogoImages.pencilIcon));
        eraseButton.setGraphic(getButtonIcon(LogoImages.eraserIcon));
        lineButton.setGraphic(getButtonIcon(LogoImages.lineIcon));
        shapeToolButton.setGraphic(getButtonIcon(LogoImages.shapeIcon));
        lassoButton.setGraphic(getButtonIcon(LogoImages.lassoIcon));
        colorGrabButton.setGraphic(getButtonIcon(LogoImages.eyeDropperIcon));

        // set tooltips
        mouseButton.setTooltip(new Tooltip("Mouse Tool (default)"));
        pencilButton.setTooltip(new Tooltip("Pencil Tool"));
        eraseButton.setTooltip(new Tooltip("Eraser Tool"));
        lineButton.setTooltip(new Tooltip("Line Tool"));
        shapeToolButton.setTooltip(new Tooltip("Shape Tool"));
        textButton.setTooltip(new Tooltip("Text Tool"));
        lassoButton.setTooltip(new Tooltip("Select and Drag Tool"));
        colorGrabButton.setTooltip(new Tooltip("Color Grab Tool"));

        // all buttons will be the width of the VBox
        mouseButton.setMaxWidth(Double.MAX_VALUE);
        pencilButton.setMaxWidth(Double.MAX_VALUE);
        eraseButton.setMaxWidth(Double.MAX_VALUE);
        lineButton.setMaxWidth(Double.MAX_VALUE);
        shapeToolButton.setMaxWidth(Double.MAX_VALUE);
        colorGrabButton.setMaxWidth(Double.MAX_VALUE);
        textButton.setMaxWidth(Double.MAX_VALUE);
        lassoButton.setMaxWidth(Double.MAX_VALUE);

        // set button actions
        mouseButton.setOnAction(e -> mainManager.setSelectedTool(new ToolMouse()));
        pencilButton.setOnAction(e -> mainManager.setSelectedTool(new ToolPencil()));
        eraseButton.setOnAction(e -> mainManager.setSelectedTool(new Eraser()));
        lineButton.setOnAction(e -> mainManager.setSelectedTool(new ToolLine()));
        shapeToolButton.setOnAction(e -> mainManager.setSelectedTool(selectedShapeTool));
        lassoButton.setOnAction(e -> mainManager.setSelectedTool(selectedLassoTool));
        colorGrabButton.setOnAction(e -> mainManager.setSelectedTool(new EyeDropper(strokeColorPicker, mainManager)));

        // add to toggle group
        toggleGroup.getToggles().add(mouseButton);
        toggleGroup.getToggles().add(pencilButton);
        toggleGroup.getToggles().add(eraseButton);
        toggleGroup.getToggles().add(lineButton);
        toggleGroup.getToggles().add(shapeToolButton);
        toggleGroup.getToggles().add(textButton);
        toggleGroup.getToggles().add(lassoButton);
        toggleGroup.getToggles().add(colorGrabButton);

        toggleGroup.selectToggle(mouseButton);

        // layout in vbox
        toolVbox.getChildren().add(new Label(" "));
        toolVbox.getChildren().add(mouseButton);
        toolVbox.getChildren().add(new Label(" "));
        toolVbox.getChildren().add(pencilButton);
        toolVbox.getChildren().add(lineButton);
        toolVbox.getChildren().add(eraseButton);
        toolVbox.getChildren().add(shapeToolButton);
        toolVbox.getChildren().add(shapeToolComboBox);
        toolVbox.getChildren().add(new Label(" "));
        toolVbox.getChildren().add(textButton);
        toolVbox.getChildren().add(colorGrabButton);
        toolVbox.getChildren().add(lassoButton);
        toolVbox.getChildren().add(lassoToolComboBox);
        toolVbox.getChildren().add(new Label(" "));
        toolVbox.getChildren().add(getCenteredTextLabel("Polygon sides"));
        toolVbox.getChildren().add(polygonSlider);
    }

    private void configShapeToolComboBox() {
        shapeToolComboBox = new ComboBox<String>();
        shapeToolComboBox.setMaxWidth(Double.MAX_VALUE);
        shapeToolComboBox.getItems().addAll(
                LogoImages.Square,
                LogoImages.Rectangle,
                LogoImages.RoundedRect,
                LogoImages.Circle,
                LogoImages.Ellipse,
                LogoImages.Polygon
        );
        shapeToolComboBox.setValue(LogoImages.Square);
        shapeToolComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            selectedShapeTool = shapeToolsMap.get(newValue);
            mainManager.setSelectedTool(selectedShapeTool);
            toggleGroup.selectToggle(shapeToolButton);
        });
    }
    private void configLassoToolComboBox() {
        lassoToolComboBox = new ComboBox<String>();
        lassoToolComboBox.setMaxWidth(Double.MAX_VALUE);
        lassoToolComboBox.getItems().addAll (
                LogoImages.CutAndDrag,
                LogoImages.CopyAndDrag
        );
        lassoToolComboBox.setValue(LogoImages.CutAndDrag);
        lassoToolComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            selectedLassoTool = lassoToolsMap.get(newValue);
            mainManager.setSelectedTool(selectedLassoTool);
            toggleGroup.selectToggle(lassoButton);
        });
    }

    private void configStrokeWidthComboBox() {
        ComboBox strokeWidthComboBox = new ComboBox();
        strokeWidthComboBox.setMaxWidth(Double.MAX_VALUE);
        strokeWidthComboBox.getItems().addAll (
                MainManager.StrokeWidth.THIN,
                MainManager.StrokeWidth.MEDIUM,
                MainManager.StrokeWidth.WIDE);
        strokeWidthComboBox.setValue(MainManager.StrokeWidth.THIN);
        strokeWidthComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            mainManager.setSelectedStrokeWidth((MainManager.StrokeWidth) newValue);
        });
        toolVbox.getChildren().add(getCenteredTextLabel("Stroke Width"));
        toolVbox.getChildren().add(strokeWidthComboBox);
    }

    private void configColorPickers() {
        strokeColorPicker = new ColorPicker();
        fillColorPicker = new ColorPicker();

        strokeColorPicker.setMaxWidth(Double.MAX_VALUE);
        fillColorPicker.setMaxWidth(Double.MAX_VALUE);

        strokeColorPicker.getStyleClass().add("button");
        fillColorPicker.getStyleClass().add("button");

        strokeColorPicker.setValue(Color.BLACK);
        fillColorPicker.setValue(Color.BLACK);

        strokeColorPicker.setOnAction(colorEvent -> {
            Color chosenColor = strokeColorPicker.getValue();
            mainManager.setStrokeColor(chosenColor);
        });
        fillColorPicker.setOnAction(colorEvent -> {
            Color chosenColor = fillColorPicker.getValue();
            mainManager.setFillColor(chosenColor);
        });

        toolVbox.getChildren().add(getCenteredTextLabel("Stroke Color"));
        toolVbox.getChildren().add(strokeColorPicker);

        toolVbox.getChildren().add(getCenteredTextLabel("Fill Color"));
        toolVbox.getChildren().add(fillColorPicker);
    }

    private HBox getCenteredTextLabel(String text) {
        HBox textBox = new HBox();
        textBox.setMaxWidth(Double.MAX_VALUE);
        textBox.setAlignment(Pos.CENTER);
        textBox.getChildren().add(new Label(text));
        return  textBox;
    }

    private void configTimerSection() {
        autoSaveLabel = new Label("Auto-Save in:    ");
        timerLabel = new Label(getTimerString(mainManager.getAutoSaveCounter()));
        timerLabelHBox = new HBox();
        timerLabelHBox.setMaxWidth(Double.MAX_VALUE);
        timerLabelHBox.setAlignment(Pos.CENTER);
        timerLabelHBox.getChildren().add(autoSaveLabel);
        timerLabelHBox.getChildren().add(timerLabel);
        configureTimerSlider();

        Button showHideButton = new Button("Show/Hide Timer");
        showHideButton.setMaxWidth(Double.MAX_VALUE);
        showHideButton.setOnAction(e -> toggleTimerLabelHidden());

        toolVbox.getChildren().add(new Label(""));
        toolVbox.getChildren().add(timerLabelHBox);
        toolVbox.getChildren().add(timerSlider);
        toolVbox.getChildren().add(showHideButton);
    }
    private void toggleTimerLabelHidden() {
        timerLabelHBox.getChildren().remove(autoSaveLabel);
        timerLabelHBox.getChildren().remove(timerLabel);

        // toggle value
        timerLabelHidden = !timerLabelHidden;

        // act on new value
        if (timerLabelHidden) {
            // add spacer label
            timerLabelHBox.getChildren().add(new Label(""));
        } else {
            timerLabelHBox.getChildren().add(autoSaveLabel);
            timerLabelHBox.getChildren().add(timerLabel);
        }
    }

    private void configureTimerSlider() {
        int startingValue = mainManager.getAutoSaveCounterMax() / 60;
        timerSlider = new Slider();
        timerSlider.setMin(2);
        timerSlider.setMax(5);
        timerSlider.setValue(startingValue);
        timerSlider.setMajorTickUnit(.5);
        timerSlider.setMinorTickCount(0);
        timerSlider.setShowTickLabels(true);
        timerSlider.setShowTickMarks(true);
        timerSlider.setSnapToTicks(true);

        // update timer after selection is done
        timerSlider.setOnMouseReleased(event -> {
            System.out.println("User selected " + timerSlider.getValue());
            int seconds = (int)(timerSlider.getValue() * 60);
            mainManager.setAutoSaveCounterMax(seconds);
        });
    }

    private void configTimerListener() {
        mainManager.autoSaveCounterProperty().addListener((o, oldValue, newValue) -> {
            timerLabel.setText(getTimerString(mainManager.getAutoSaveCounter()));
        });
    }

    public String getTimerString(int counter) {
        int remainder = counter % 60;
        int minutes = counter / 60;
        String minuteString = String.valueOf(minutes);
        String secondsString = (remainder < 10) ? ("0" + remainder) : String.valueOf(remainder);
        return minuteString + ":" + secondsString;
    }






}
