package com.example.paint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class MainCanvas extends Canvas{
    private GraphicsContext graphicsContext;
    private MainManager mainManager;
    private ImageView imageView;

    private ArrayList<Drawing> operations = new ArrayList<>();
    private ArrayList<Drawing> redoStack = new ArrayList<>();

    public MainCanvas(double initialWidth, double initialHeight, MainManager mainManager) {
        super(initialWidth, initialHeight);
        this.mainManager = mainManager;
        graphicsContext = this.getGraphicsContext2D();

        graphicsContext.setFill(Color.BLACK);
        setOnClickListeners();
    }

    public ImageView getImageView() {
        return imageView;
    }
    public MainManager getMainManager() { return mainManager; }
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
    public boolean isResizable() {
        return true;
    }

    public void reDraw() {
        System.out.println("Redrawing stack: ");
        for (Drawing operation : operations) {
            System.out.println("Draw " + operation);
            operation.draw(graphicsContext);
        }
    }

    public void drawImageOnCanvas() {
        Image image = imageView.getImage();
        clearGraphicsContext();
        graphicsContext.drawImage(image, 0, 0, this.getWidth(), this.getHeight());
    }
    public void updateGraphicsContext() {
        updateColors();
    }

    public void clearOperationsList() {
        operations.clear();
    }

    //clears canvas

    public void clearGraphicsContext() {
        graphicsContext.clearRect(0, 0, this.getWidth(), this.getHeight());
    }

    //updates colors
    private void updateColors() {
        Color strokeColor = mainManager.getStrokeColor();
        Color fillColor = mainManager.getFillColor();
        graphicsContext.setStroke(strokeColor);
        graphicsContext.setFill(fillColor);
    }


    public void undo() {
        Drawing opToUndo = fromUndoStack();
        if (opToUndo != null) {
            pushRedoStack(opToUndo);
            drawImageOnCanvas();
            reDraw();
        }
    }
    public void redo() {
        Drawing opToRedo = fromRedoStack();
        if (opToRedo != null) {
            pushUndoStack(opToRedo);
            drawImageOnCanvas();
            reDraw();
        }
    }
    public void pushUndoStack(Drawing operation) {
        operations.add(operation);
    }

    public void pushRedoStack(Drawing operation) {
        redoStack.add(operation);
    }

    public Drawing fromUndoStack() {
        return popDrawOperation(operations);
    }

    public Drawing fromRedoStack() {
        return popDrawOperation(redoStack);
    }
    private Drawing popDrawOperation(ArrayList<Drawing> stack) {
        if (stack.size() > 0) {
            int lastIndex = stack.size() - 1;
            Drawing result = stack.get(lastIndex);
            stack.remove(lastIndex);
            return result;
        } else {
            return null;
        }
    }
    private void setOnClickListeners() {
        this.setOnMouseDragged(this::onDrag);
        this.setOnMousePressed(this::onMousePressed);
        this.setOnMouseReleased(this::onMouseReleased);
    }

    //What happens when a certain tool does an action on a canvas

    private void onMouseReleased(MouseEvent e) {
        updateGraphicsContext();
        ParentPaintTool tool = mainManager.getSelectedTool();
        tool.onMouseReleased(e, graphicsContext);
        if (tool.makeChangesToCanvas) {
            mainManager.setUnsavedChangeCurrently(true);
        }
    }

    private void onDrag(MouseEvent e) {
        updateGraphicsContext();
        ParentPaintTool tool = mainManager.getSelectedTool();
        tool.onDrag(e, graphicsContext);
        if (tool.makeChangesToCanvas) {
            mainManager.setUnsavedChangeCurrently(true);
        }
    }

    private void onMousePressed(MouseEvent e) {
        updateGraphicsContext();
        ParentPaintTool tool = mainManager.getSelectedTool();
        tool.onMousePressed(e, graphicsContext);
        if (tool.makeChangesToCanvas) {
            mainManager.setUnsavedChangeCurrently(true);
        }
    }
    public void scaleImage(double newWidth, double newHeight) {
        if (imageView != null) {
            imageView.setFitHeight(newHeight);
            imageView.setFitWidth(newWidth);

            // use the imageView aspect fit properties to determine canvas size
            scaleCanvasToImageSize();
            drawImageOnCanvas();
            reDraw();
        }
    }

    private void scaleCanvasToImageSize() {
        setWidth(getActualImageViewWidth());
        setHeight(getActualImageViewHeight());
    }

    private double getActualImageViewHeight() {
        double aspectRatio = imageView.getImage().getWidth() / imageView.getImage().getHeight();
        double imageViewHeight = Math.min(imageView.getFitHeight(), imageView.getFitWidth() / aspectRatio);
        return imageViewHeight;
    }

    private double getActualImageViewWidth() {
        double aspectRatio = imageView.getImage().getWidth() / imageView.getImage().getHeight();
        double imageViewWidth = Math.min(imageView.getFitWidth(), imageView.getFitHeight() * aspectRatio);
        return imageViewWidth;
    }



}
