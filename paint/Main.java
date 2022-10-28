package com.example.paint;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Optional;


import java.io.IOException;

public class Main extends Application {
    private Group root;
    private BorderPane borderPane;
    private TabPane tabPane;
    private Scene scene;

    public FileManager fileManager;
    public ImageManager imageManager;
    public MainManager mainManager;

    private ScrollbarPain horizontalScrollBar;
    private ScrollbarPain verticalScrollBar;
    private ToolBar toolBar;
    private MenuBar menuBar;

    private Boolean commandIsDown = false;
    private Boolean shiftIsDown = false;

    private Double imageWidthOffset = 178.0;
    private Double imageHeightOffset = 84.0;



    public void start(Stage stage) throws IOException {
        borderPane = new BorderPane();
        root = new Group();
        scene = new Scene(root, LogoImages.defaultSceneWidth, LogoImages.defaultSceneHeight);
        root.getChildren().add(borderPane);

        imageManager = new ImageManager();
        fileManager = new FileManager(stage, mainManager);
        mainManager = new MainManager();


        initTabPane();
        initNewTabWithCanvas();
        initMenuBar();
        initToolBar();
        initScrollBars();
        initKeyboardListeners();
        initTimerListener();

        ChangeListener<Number> sceneSizeListener = (observable, oldValue, newValue) -> scaleBorderPaneToSceneSize();
        scene.widthProperty().addListener(sceneSizeListener);
        scene.heightProperty().addListener(sceneSizeListener);

        stage.setTitle("This has to be illegal");
        stage.setScene(scene);
        stage.show();

        imageWidthOffset = toolBar.getWidth() + verticalScrollBar.getWidth() + (LogoImages.imageInsetValue);
        imageHeightOffset = menuBar.getHeight() + horizontalScrollBar.getHeight() + tabPane.getTabMaxHeight() + (LogoImages.imageInsetValue) ;

        displayMainImageInCurrentTab();
        scaleBorderPaneToSceneSize();
    }
    public static void main(String[] args) {
        launch();
    }
    private void quitApp(){
        for(int i = 0; i < tabPane.getTabs().size(); i++){
            if(mainManager.hasUnsavedChangesAtIndex(i)){
                SmartSave(i);
            }
        }
        System.exit(0);

    }
    private void SmartSave(int index) {
        ButtonType save = new ButtonType("Save Changes", ButtonBar.ButtonData.OK_DONE);
        ButtonType quit = new ButtonType("Exit Without Saving", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.WARNING, "You have unsaved changes in tab " + (index + 1), save, quit);
        alert.setTitle("Unsaved changes!");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == save) {
            MainCanvas canvas = getCanvasAtIndex(index);
            if (canvas != null) {
                fileManager.saveImage(imageManager.getSnapshotImageToSave(canvas));
            }
        }
    }
    private void initMenuBar(){
        menuBar = new MenuBar();
        initPaintMenu();
        initImageMenu();
        initViewMenu();
        initHelpMenu();
        borderPane.setTop(menuBar);
    }

    private void initToolBar(){
        toolBar = new ToolbarPain(mainManager);
        borderPane.setLeft(toolBar);
    }

    private void initScrollBars() {
        horizontalScrollBar = new ScrollbarPain();
        verticalScrollBar = new ScrollbarPain();
        verticalScrollBar.setOrientation(Orientation.VERTICAL);
        borderPane.setBottom(horizontalScrollBar);
        borderPane.setRight(verticalScrollBar);

        // listeners for scrollbar value change
        // translate image in x or y direction based on scrollbar value
        ChangeListener<Number> horizontalValueChangeListener = (ov, oldVal, newVal) -> {
            int tabIndex = mainManager.getSelectedTabIndex();
            Node stackPane = tabPane.getTabs().get(tabIndex).getContent();
            stackPane.setTranslateX(-newVal.doubleValue());
        };
        ChangeListener<Number> verticalValueChangeListener = (ov, oldVal, newVal) -> {
            int tabIndex = mainManager.getSelectedTabIndex();
            Node stackPane = tabPane.getTabs().get(tabIndex).getContent();
            stackPane.setTranslateY(-newVal.doubleValue());
        };

        horizontalScrollBar.valueProperty().addListener(horizontalValueChangeListener);
        verticalScrollBar.valueProperty().addListener(verticalValueChangeListener);
    }

    private void initTabPane() {
        tabPane = new TabPane();
        borderPane.setCenter(tabPane);

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        int index = tabPane.getSelectionModel().getSelectedIndex();
                        mainManager.setSelectedTabIndex(index);
                    }
                }
        );
    }
    void initNewTabWithCanvas() {
        int newTabIndex = tabPane.getTabs().size();
        Tab tab = new Tab();
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        tab.setText("Tab " + (newTabIndex + 1));
        tab.setContent(stackPane);
        tabPane.getTabs().add(tab);
        initCanvasInTab(newTabIndex);
        mainManager.addTabToUnsavedChangedArray();
    }

    void initCanvasInTab(int tabIndex) {
        Tab tab = tabPane.getTabs().get(tabIndex);
        StackPane stackPane = (StackPane) tab.getContent();

        double width = LogoImages.defaultSceneWidth - imageWidthOffset;
        double height = LogoImages.defaultSceneHeight - imageHeightOffset;
        stackPane.getChildren().add(new MainCanvas(width, height, mainManager));
    }

    private void initPaintMenu() {
        Menu mainMenu = new Menu("Pain(t)");
        MenuItem quit = new MenuItem("Quit Pain(t)");
        quit.setOnAction(e -> quitApp());
        mainMenu.getItems().add(quit);
        menuBar.getMenus().add(mainMenu);
    }

    private void initImageMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem load = new MenuItem("Load New Image");
        MenuItem loadAutoSave = new MenuItem("Load Auto-Saved Image");
        MenuItem loadInNewTab = new MenuItem("Load Image In New Tab");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem save = new MenuItem("Save");
        MenuItem restore = new MenuItem("Restore main logo image");
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");



        load.setOnAction(e -> selectNewImageForCurrentTab());
        loadAutoSave.setOnAction(e -> loadAutoSavedImageInCurrentTab());
        restore.setOnAction(e -> displayDefaultLogoImageInCurrentTab());
        // take snapshot of canvas, send to fileManager to save
        saveAs.setOnAction(e -> fileManager.saveImageAs(imageManager.getSnapshotImageToSave(getCurrentCanvas())));
        save.setOnAction(e -> fileManager.saveImage(imageManager.getSnapshotImageToSave(getCurrentCanvas())));
        loadInNewTab.setOnAction(e -> {
            initNewTabWithCanvas();
            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            selectionModel.select(tabPane.getTabs().size() - 1);
            selectNewImageForCurrentTab();
        });
        undo.setOnAction(e -> getCurrentCanvas().undo());
        redo.setOnAction(e -> getCurrentCanvas().redo());

        fileMenu.getItems().add(restore);
        fileMenu.getItems().add(save);
        fileMenu.getItems().add(saveAs);
        fileMenu.getItems().add(load);
        fileMenu.getItems().add(loadAutoSave);
        fileMenu.getItems().add(loadInNewTab);
        fileMenu.getItems().add(undo);
        fileMenu.getItems().add(redo);

        menuBar.getMenus().add(fileMenu);
    }

    private void initViewMenu() {
        Menu viewMenu = new Menu("View");
        MenuItem zoomIn = new MenuItem("Zoom +");
        MenuItem zoomOut = new MenuItem("Zoom -");
        MenuItem autoScale = new MenuItem("Auto-Scale Image");

        autoScale.setOnAction(e -> scaleCurrentImageToSceneSize());
        zoomIn.setOnAction(e -> zoom(1.1));
        zoomOut.setOnAction(e -> zoom(0.9));
        viewMenu.getItems().add(zoomIn);
        viewMenu.getItems().add(zoomOut);
        viewMenu.getItems().add(autoScale);

        menuBar.getMenus().add(viewMenu);
    }
    private void initHelpMenu() {
        Menu helpMenu = new Menu("Help");
        Alert helpMenuAlert = new Alert(Alert.AlertType.INFORMATION);
        MenuItem about = new MenuItem("About");
        MenuItem help = new MenuItem("Help");

        about.setOnAction(e -> {
            helpMenuAlert.setTitle(LogoImages.aboutAlertTitle);
            helpMenuAlert.setHeaderText(LogoImages.aboutAlertTitle);
            helpMenuAlert.setContentText(LogoImages.aboutinfo);
            helpMenuAlert.show();
        });
        help.setOnAction(e -> {
            helpMenuAlert.setTitle(LogoImages.helpAlertTitle);
            helpMenuAlert.setHeaderText(LogoImages.helpAlertTitle);
            helpMenuAlert.setContentText(LogoImages.helpAlertText);
            helpMenuAlert.show();
        });

        helpMenu.getItems().add(about);
        helpMenu.getItems().add(help);
        menuBar.getMenus().add(helpMenu);
    }
    private void initKeyboardListeners() {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.COMMAND) {
                commandIsDown = true;
                System.out.println("Command is down");
            }
            if (e.getCode() == KeyCode.SHIFT) {
                shiftIsDown = true;
                System.out.println("Shift is down");
            }
            if (e.getCode() == KeyCode.S && commandIsDown) { fileManager.saveImage(imageManager.getSnapshotImageToSave(getCurrentCanvas())); }
            if (e.getCode() == KeyCode.EQUALS && commandIsDown) { zoom(1.1);}
            if (e.getCode() == KeyCode.MINUS && commandIsDown) { zoom(0.9);}
            if (e.getCode() == KeyCode.Q && commandIsDown) { quitApp(); }
            if (e.getCode() == KeyCode.Z && commandIsDown && !shiftIsDown) { getCurrentCanvas().undo(); }
            if (e.getCode() == KeyCode.Z && commandIsDown && shiftIsDown) { getCurrentCanvas().redo(); }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.COMMAND) {
                commandIsDown = false;
            }
            if (e.getCode() == KeyCode.SHIFT) {
                shiftIsDown = false;
            }
        });
    }
    private void initTimerListener() {
        mainManager.autoSaveCounterProperty().addListener((o, oldValue, newValue) -> {
            if (newValue.equals(0)) {
                System.out.println("Auto-saving image");
                mainManager.setAutoSaveImageForCurrentTab(imageManager.getSnapshotImageToSave(getCurrentCanvas()));
            }
        });
    }

    private void selectNewImageForCurrentTab() {
        String filePath = fileManager.getImageFilePathFromUser((Stage)scene.getWindow());
        if (filePath != null) {
            try {
                Image newImage = imageManager.getImageFromFilePath(filePath);
                mainManager.setMainImageInCurrentTab(newImage);
                displayMainImageInCurrentTab();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void loadAutoSavedImageInCurrentTab() {
        Image autoSavedImage = mainManager.getAutoSaveImageForCurrentTab();
        if (autoSavedImage != null) {
            mainManager.setMainImageInCurrentTab(autoSavedImage);
            displayMainImageInCurrentTab();
        }
    }
    private void displayDefaultLogoImageInCurrentTab() {
        Image logo = imageManager.getLogoImage();
        mainManager.setMainImageInCurrentTab(logo);
        displayMainImageInCurrentTab();
    }

    private void displayMainImageInCurrentTab() {
        Image image = mainManager.getImageFromCurrentTab();

        if (image != null) {
            // new image view, clear any drawing from canvas
            MainCanvas canvas = getCurrentCanvas();
            canvas.setImageView(new ImageView(image));
            canvas.clearGraphicsContext();
            canvas.clearOperationsList();

            canvas.getImageView().setX(0);
            canvas.getImageView().setY(0);
            canvas.getImageView().setPreserveRatio(true);

            scaleCurrentImageToSceneSize();
            canvas.drawImageOnCanvas();
        } else {
            System.out.println("Unable to display main image - mainImage.image is null!");
        }
    }

    //region Image and Pane Scaling

    private void scaleCurrentImageToSceneSize() {
        double newWidth = scene.getWidth() - imageWidthOffset;
        double newHeight = scene.getHeight() - imageHeightOffset;
        scaleCurrentImage(newWidth, newHeight);
    }

    private void scaleCurrentImage(double newWidth, double newHeight) {
        getCurrentCanvas().scaleImage(newWidth, newHeight);
        updateScrollBars();
    }

    private void scaleAllImagesToSceneSize() {
        double newWidth = scene.getWidth() - imageWidthOffset;
        double newHeight = scene.getHeight() - imageHeightOffset;
        scaleAllImages(newWidth, newHeight);
    }

    private void scaleAllImages(double newWidth, double newHeight) {
        for (Tab tab : tabPane.getTabs()) {
            StackPane stackPane = (StackPane) tab.getContent();
            for (Node child : stackPane.getChildren()) {
                if (child instanceof MainCanvas) {
                    MainCanvas canvas = (MainCanvas) child;
                    canvas.scaleImage(newWidth, newHeight);
                }
            }
        }
        updateScrollBars();
    }

    private void zoom(double factor) {
        ImageView imageView = getCurrentCanvas().getImageView();
        double currentWidth = imageView.getFitWidth();
        double currentHeight = imageView.getFitHeight();

        // need a boundary, otherwise the app can crash if zoomed in too far. The 3 is just a ballpark number that seems to work.
        boolean zoomedInTooFar = (currentWidth >= 3 * scene.getWidth()) || (currentHeight >= 3 * scene.getHeight());

        if (!zoomedInTooFar || factor < 1) {
            // if too far zoomed in, can still zoom back out
            scaleCurrentImage(currentWidth * factor, currentHeight * factor);
            System.out.println("Horizontal scrollbar: " + horizontalScrollBar.getValue());
            System.out.println("Vertical scrollbar: " + verticalScrollBar.getValue());
        }
    }

    private void scaleBorderPaneToSceneSize() {
        borderPane.setMinWidth(scene.getWidth());
        borderPane.setMinHeight(scene.getHeight());
        borderPane.setMaxWidth(scene.getWidth());
        borderPane.setMaxHeight(scene.getHeight());

        scaleAllImagesToSceneSize();
        updateScrollBars();
    }
    private void updateScrollBars() {
        Node stackPane = tabPane.getTabs().get(mainManager.getSelectedTabIndex()).getContent();
        Bounds imageBoundsInScene = stackPane.localToScene(stackPane.getBoundsInLocal());

        // actual image y values
        double imageTopY = imageBoundsInScene.getMinY();
        double imageBottomY = imageBoundsInScene.getMaxY();

        // actual image x values
        double imageLeftX = imageBoundsInScene.getMinX();
        double imageRightX = imageBoundsInScene.getMaxX();

        // Y values where the edges of the image should be if the image is in frame
        double verticalMin = menuBar.getHeight() + tabPane.getTabMaxHeight() + (LogoImages.imageInsetValue / 2.0);
        double verticalMax = scene.getHeight() - horizontalScrollBar.getHeight() - (LogoImages.imageInsetValue / 2.0);

        // x values where the edges of the image should be if the image is in frame
        double horizontalMin = toolBar.getWidth() + (LogoImages.imageInsetValue / 2.0);
        double horizontalMax = scene.getWidth() - verticalScrollBar.getWidth() - (LogoImages.imageInsetValue / 2.0);

        horizontalScrollBar.updateScrollBarBounds(imageLeftX, imageRightX, horizontalMin, horizontalMax);
        verticalScrollBar.updateScrollBarBounds(imageTopY, imageBottomY, verticalMin, verticalMax);
    }
    private MainCanvas getCurrentCanvas() {
        MainCanvas canvas = null;
        Tab currentTab = tabPane.getTabs().get(getSelectedTabIndex());
        StackPane stackPane = (StackPane) currentTab.getContent();
        for (Node child : stackPane.getChildren()) {
            if (child instanceof MainCanvas) {
                canvas = (MainCanvas) child;
            }
        }
        return canvas;
    }
    private MainCanvas getCanvasAtIndex(int index) {
        MainCanvas canvas = null;
        if (tabPane.getTabs().get(index) != null) {
            Tab selectedTab = tabPane.getTabs().get(index);
            StackPane stackPane = (StackPane) selectedTab.getContent();
            for (Node child : stackPane.getChildren()) {
                if (child instanceof MainCanvas) {
                    canvas = (MainCanvas) child;
                }
            }
        }
        return canvas;
    }

    private int getSelectedTabIndex() {
        return tabPane.getSelectionModel().getSelectedIndex();
    }

}