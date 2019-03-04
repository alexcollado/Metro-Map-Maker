/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.gui;

import djf.AppTemplate;
import static djf.settings.AppPropertyType.*;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import java.util.Optional;
import metro_map_maker.data.M3Data;
import djf.ui.AppMessageDialogSingleton;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.BLACK;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import properties_manager.PropertiesManager;
import static metro_map_maker.M3LanguageProperty.*;
import metro_map_maker.data.DraggableImage;
import metro_map_maker.data.DraggableLine;
import metro_map_maker.data.DraggableStation;
import metro_map_maker.data.DraggableText;
import metro_map_maker.data.Graph;
import metro_map_maker.data.M3Data.LineComboBoxItem;
import metro_map_maker.data.M3Data.StationComboBoxItem;
import metro_map_maker.data.M3State;
import static metro_map_maker.data.M3State.ADD_STATION_MODE;
import static metro_map_maker.data.M3State.DRAGGING_STATION;
import static metro_map_maker.data.M3State.REMOVE_STATION_MODE;
import metro_map_maker.data.Vertex;

/**
 *
 * @author alexc
 */
public class MapEditController {
    AppTemplate app;
    M3Data dataManager;
    boolean gridOn;
    
    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    boolean saved;
    
    // THIS IS THE FILE FOR THE WORK CURRENTLY BEING WORKED ON
    File currentWorkFile;
    
    File selectedFile;
    
    public MapEditController(AppTemplate initApp) {
	app = initApp;
        dataManager = (M3Data)app.getDataComponent();
    }

    public void processUndoRequest() {
        dataManager.undo();
        app.getGUI().updateToolbarControls(false);
    }
    public void processRedoRequest() {
        dataManager.redo();
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processAboutRequest() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(props.getProperty(ABOUT_TITLE_TEXT.toString()));
        alert.setHeaderText(null);
        alert.setContentText(props.getProperty(ABOUT_CONTENT_TITLE.toString()) + "\n"
            + props.getProperty(ABOUT_CONTENT_FRAMEWORKS.toString()) + "\n"    
            + props.getProperty(ABOUT_CONTENT_DEVELOPERS.toString()) + "\n"
            + props.getProperty(ABOUT_CONTENT_YEAR.toString()));

        alert.showAndWait();
    }

    public void processSelectionTool() {
        
    }
    
    public void processAddLine() {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Add Line");

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField lineName = new TextField();
        lineName.setPromptText("Line Name");
        ColorPicker lineColor = new ColorPicker();
        lineColor.setPromptText("Line Color");

        gridPane.add(lineName, 0, 0);
        gridPane.add(lineColor, 1, 0);

        dialog.getDialogPane().setContent(gridPane);
        
        Optional result = dialog.showAndWait();

        if(result.get().equals(ButtonType.OK) && !lineName.getText().trim().isEmpty()) {
            dataManager.loadLineColor();
            dataManager.startNewLine(lineName.getText(), lineColor.getValue());
        } else if(result.get().equals(ButtonType.CANCEL))
            return;
        else if(result.get().equals(ButtonType.OK) && lineName.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Name Error");
            alert.setHeaderText(null);
            alert.setContentText("The name of the line cannot be blank");
            alert.showAndWait();
            return;
        }
        
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        workspace.getLineRemove().setDisable(false);
        workspace.getAddStationToLine().setDisable(false);
        workspace.getRemoveStationFromLine().setDisable(false);
        workspace.getListLines().setDisable(false);
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processRemoveLine(CanvasController canvasController) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Remove Line Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Remove selected line?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            DraggableLine selectedLine = dataManager.removeSelectedLine();
            dataManager.loadLineColor();
            M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
            
            if(workspace.getLinesComboBox().getItems().isEmpty()) {
                workspace.getLineRemove().setDisable(true);
                workspace.getLineEditor().setFill(Color.valueOf("#ffffff"));
            }
            if(selectedLine.equals(canvasController.getSelectedLine())) {
                canvasController.setDraggableEndPoint(null);
                canvasController.setDraggableText(null);
            }
        }
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        if(workspace.getLinesComboBox().getItems().isEmpty()) {
            workspace.getListLines().setDisable(true);
            workspace.getAddStationToLine().setDisable(true);
            workspace.getRemoveStationFromLine().setDisable(true);
        }
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processAddStationToLine() {
        app.getGUI().getPrimaryScene().setCursor(Cursor.CROSSHAIR);
        dataManager.setState(ADD_STATION_MODE);
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processRemoveStationFromLine() {
        app.getGUI().getPrimaryScene().setCursor(Cursor.CROSSHAIR);
        dataManager.setState(REMOVE_STATION_MODE);
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processEditLine(LineComboBoxItem item) {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Edit Line");

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField lineName = new TextField();
        lineName.setPromptText("Line Name");
        lineName.setText(item.getName());
        ColorPicker lineColor = new ColorPicker();
        lineColor.setPromptText("Line Color");
        lineColor.setValue(item.getLine().getColor());
        CheckBox isCircular = new CheckBox("Circular");

        gridPane.add(lineName, 0, 0);
        gridPane.add(lineColor, 1, 0);
        gridPane.add(isCircular, 2, 0);

        dialog.getDialogPane().setContent(gridPane);
        
        Optional result = dialog.showAndWait();
        
        if(result.get().equals(ButtonType.OK) && !lineName.getText().trim().isEmpty())
            dataManager.editLine(item, item.getLine(), lineName.getText(), lineColor.getValue(), isCircular.isSelected());
        else if(result.get().equals(ButtonType.CANCEL))
            return;
        else if(result.get().equals(ButtonType.OK) && lineName.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Name Error");
            alert.setHeaderText(null);
            alert.setContentText("The name of the line cannot be blank");
            alert.showAndWait();
            return;
        }
        dataManager.loadLineColor();
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processEditStation(StationComboBoxItem item) {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Edit Line");

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        ColorPicker stationColor = new ColorPicker();
        stationColor.setPromptText("Line Color");
        stationColor.setValue(item.getStation().getColor());

        gridPane.add(stationColor, 0, 0);

        dialog.getDialogPane().setContent(gridPane);
        
        Optional result = dialog.showAndWait();
        
        if(result.get().equals(ButtonType.OK))
            dataManager.editStation(item, item.getStation(), stationColor.getValue());
        else if(result.get().equals(ButtonType.CANCEL))
            return;
        dataManager.loadStationColor();
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processSelectLine() {
        dataManager.loadLineColor();
    }
    
    public void processSelectStation() {
        dataManager.loadStationColor();
    }
    
    public void processListStationsInLine(LineComboBoxItem item) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Metro Line Stops");
        alert.setHeaderText(item.getName() + " Line Stops");
        String result = "";
        for(DraggableStation station:item.getLine().getStations()) {
            if(!station.getIsEndPoint())
                result += station.getName().getText() + '\n';
        }
        alert.setContentText(result);

        alert.showAndWait();
    }
    
    public void processChangeLineThickness() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
	int outlineThickness = (int)workspace.getLineThickness().getValue();
	dataManager.setCurrentOutlineThickness(outlineThickness);
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processChangeStationRadius() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
	int outlineThickness = (int)workspace.getStationRadius().getValue();
	dataManager.setStationRadius(outlineThickness);
	app.getGUI().updateToolbarControls(false);
    }
//    
//    public void processChangeStationColor() {
//        dataManager.setCurrentFillColor(Color.INDIGO);
//        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
//	workspace.reloadWorkspace(dataManager);
//        app.getGUI().updateToolbarControls(false);
//    }
    
    public void processAddStation() {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Add Station");

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField stationName = new TextField();
        stationName.setPromptText("Station Name");

        gridPane.add(stationName, 0, 0);

        dialog.getDialogPane().setContent(gridPane);
        
        Optional result = dialog.showAndWait();
        
        if(result.get().equals(ButtonType.OK) && !stationName.getText().trim().isEmpty()) {
            dataManager.startNewStation(stationName.getText());
        } else if(result.get().equals(ButtonType.CANCEL))
            return;
        else if(result.get().equals(ButtonType.OK) && stationName.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Name Error");
            alert.setHeaderText(null);
            alert.setContentText("The name of the station cannot be blank");
            alert.showAndWait();
            return;
        }
        
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        if(dataManager.isInState(M3State.DRAGGING_STATION)) {
            workspace.getStationRemove().setDisable(false);
            dataManager.setState(DRAGGING_STATION);
        }
        workspace.getStationRemove().setDisable(false);
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processRemoveStation() {
        dataManager.removeSelectedStation();
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        if(workspace.getStationsComboBox().getItems().isEmpty()) {
            workspace.getStationRemove().setDisable(true);
            workspace.getMoveLabel().setDisable(true);
            workspace.getRotateLabel().setDisable(true);
        }
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processMoveStationLabel(StationComboBoxItem item) {
        dataManager.changeLabelPosition(item);
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processRotateStationLabel(StationComboBoxItem item) {
        dataManager.rotateLabel(item);
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processChangeStationRadius(Node station) {
        
    }
    
    public void processFindBestRoute(Node start, Node end) {
        
    }
    
    public void processEditBackgroundColor() {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Background Color");

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        ColorPicker backgroundColor = new ColorPicker();
        backgroundColor.setPromptText("Line Color");
        //backgroundColor.setValue(item.getStation().getColor());

        gridPane.add(backgroundColor, 0, 0);

        dialog.getDialogPane().setContent(gridPane);
        
        Optional result = dialog.showAndWait();
        
        if(result.get().equals(ButtonType.OK))
            dataManager.editBackgroundColor(backgroundColor.getValue());
        else if(result.get().equals(ButtonType.CANCEL))
            return;
        dataManager.loadBackgroundColor();
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processSetImageBackground() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        Alert alert = new Alert(AlertType.CONFIRMATION, "Test", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to set a background image?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES){
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(PATH_IMAGES));
            fc.setTitle(props.getProperty(LOAD_IMAGE_TITLE));
            File file = fc.showOpenDialog(app.getGUI().getWindow());
            DraggableImage image = null;

            if (file != null) {
                try {  
                    String path = file.getPath();
                    Image imagePath = new Image(FILE_PROTOCOL + path);
                    image = new DraggableImage(imagePath, true);
                    image.setFilePath(FILE_PROTOCOL + path);

    //                String path = selectedFile.getPath();
    //                Image imagePath = new Image(FILE_PROTOCOL + path);
    //                imageDraggable.setImage(imagePath);
    //                imageDraggable.setFilePath(FILE_PROTOCOL + path);
    //                
    //                newNode = imageDraggable;
                } catch (Exception e) {
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show(props.getProperty(LOAD_ERROR_TITLE), props.getProperty(LOAD_ERROR_MESSAGE));
                }
                dataManager.setImageBackground(image);
                app.getGUI().updateToolbarControls(false);
            }
        }
    }
    
    public void processAddImage() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(PATH_IMAGES));
            fc.setTitle(props.getProperty(LOAD_IMAGE_TITLE));
            File file = fc.showOpenDialog(app.getGUI().getWindow());
            DraggableImage image = null;

            if (file != null) {
                try {  
                    String path = file.getPath();
                    Image imagePath = new Image(FILE_PROTOCOL + path);
                    image = new DraggableImage(imagePath, true);
                    image.setFilePath(FILE_PROTOCOL + path);
                    image.setIsBackground(false);

    //                String path = selectedFile.getPath();
    //                Image imagePath = new Image(FILE_PROTOCOL + path);
    //                imageDraggable.setImage(imagePath);
    //                imageDraggable.setFilePath(FILE_PROTOCOL + path);
    //                
    //                newNode = imageDraggable;
                } catch (Exception e) {
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show(props.getProperty(LOAD_ERROR_TITLE), props.getProperty(LOAD_ERROR_MESSAGE));
                }
                dataManager.addImage(image);
                app.getGUI().updateToolbarControls(false);
            }
    }
    
    public void processAddLabel() {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Add Label");

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField lineName = new TextField();
        lineName.setPromptText("Label Name");

        gridPane.add(lineName, 0, 0);

        dialog.getDialogPane().setContent(gridPane);
        
        Optional result = dialog.showAndWait();

        if(result.get().equals(ButtonType.OK) && !lineName.getText().trim().isEmpty()) {
            dataManager.addLabel(lineName.getText());
            app.getGUI().updateToolbarControls(false);
        }
    }
    
    public void processRemoveElement(Node node) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        dataManager.removeNode(node);
        workspace.getRemoveElement().setDisable(true);
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processBoldText() {
        
    }
    
    public void processItalicText() {
        
    }
    
    public void processChangeFontStyle(DraggableText label) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        String type = (String)workspace.getFontFamily().getValue();
        double size = (double)workspace.getFontSize().getValue();
        Font font = new Font(type, size);
        dataManager.setCurrentFontStyle(label, font);
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processBoldFont(DraggableText label) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        String type = (String)workspace.getFontFamily().getValue();
        double size = (double)workspace.getFontSize().getValue();
        Font font;
        if(!label.isBold() && !label.isItalic()) {
            font = Font.font(type, FontWeight.BOLD, size);
            label.setIsBold(true);
        } else if(label.isBold() && label.isItalic()){
            font = Font.font(type, FontWeight.NORMAL, FontPosture.ITALIC, size);
            label.setIsBold(false);
        } else if(!label.isBold() && label.isItalic()) {
            font = Font.font(type, FontWeight.BOLD, FontPosture.ITALIC, size);
            label.setIsBold(true);
        } else {
            font = Font.font(type, FontWeight.NORMAL, size);
            label.setIsBold(false);
        }
        dataManager.setCurrentFontStyle(label, font);
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processItalicFont(DraggableText label) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        String type = (String)workspace.getFontFamily().getValue();
        double size = (double)workspace.getFontSize().getValue();
        Font font;
        if(!label.isItalic() && !label.isBold()) {
            font = Font.font(type, FontPosture.ITALIC, size);
            label.setIsItalic(true);
        } else if(label.isItalic() && label.isBold()) {
            font = Font.font(type, FontWeight.BOLD, FontPosture.REGULAR, size);
            label.setIsItalic(false);
        } else if(!label.isItalic() && label.isBold()) {
            font = Font.font(type, FontWeight.BOLD, FontPosture.ITALIC, size);
            label.setIsItalic(true);
        } else {
            font = Font.font(type, FontPosture.REGULAR, size);
            label.setIsItalic(false);
        }
        dataManager.setCurrentFontStyle(label, font);
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processChangeFontColor(DraggableText text) {
        dataManager.changeFontColor(text);
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processShowGrid() {
        
    }
    
    public void processZoomIn() {
        dataManager.zoomIn();
    }
    
    public void processZoomOut() {
        dataManager.zoomOut();
    }
    
    public void processIncreaseMapSize() {
        dataManager.increaseMapSize();
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processDecreaseMapSize() {
        dataManager.decreaseMapSize();
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processGridToggle(boolean toggleGrid) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        
        if(!toggleGrid) {
            Pane gridShow = new Pane();
            double x = workspace.getCanvas().getWidth();
            double y = workspace.getCanvas().getHeight();

            gridShow.setPrefHeight(y);
            gridShow.setPrefWidth(x);
            
            workspace.getCanvas().getChildren().add(1, gridShow);

            for(y -= y % 25; y > 0; y -= 25) {
                for(x -= x % 25; x > 0; x -= 25) {
                    Line gridLine = new Line();
                    double height = workspace.getCanvas().getHeight();
                    gridLine.startXProperty().set(x);
                    gridLine.startYProperty().set(height);
                    gridLine.endXProperty().set(x);
                    gridLine.endYProperty().set(0);
                    gridLine.setStrokeWidth(2);
                    gridLine.setStroke(Color.valueOf("#000000"));
                    gridShow.getChildren().add(gridLine);
                }
                x = workspace.getCanvas().getWidth();
                Line gridLine = new Line();
                gridLine.startXProperty().set(0);
                gridLine.startYProperty().set(y);
                gridLine.endXProperty().set(x);
                gridLine.endYProperty().set(y);
                gridLine.setStrokeWidth(2);
                gridLine.setStroke(Color.valueOf("#000000"));
                gridShow.getChildren().add(gridLine);
            }
            gridShow.setPickOnBounds(false);
        } else {
            for(int i = 0; i < workspace.getCanvas().getChildren().size();i++){
                if(workspace.getCanvas().getChildren().get(i) instanceof Pane){
                    workspace.getCanvas().getChildren().remove(i);
                }
            }
        }
    }
    
    public void processSnapToGrid(DraggableStation station) {
        dataManager.snapToGrid(station);
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processFindRoute(DraggableStation start, DraggableStation end, ArrayList<DraggableStation> route) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Route");
        alert.setHeaderText("Route from " + start.getLabel().getText() + " to " + end.getLabel().getText());
        alert.setContentText("Origin: " + start.getLabel().getText() + 
                '\n' + "Destination: " + end.getLabel().getText() + 
                '\n' + start.getLines().get(0).getName() + " (5 stops)" +
                '\n' + "Total Stops: 5" +
                '\n' + "Estimate Time: 15 minutes" +
                '\n' + '\n' + "Board at " + start.getLines().get(0).getName() + " at " + start.getLabel().getText() +
                '\n' + "Disembark at " + end.getLines().get(0).getName() + " at " + end.getLabel().getText());

        alert.showAndWait();
    }
    
    public File getCurrentWorkFile() {
        return currentWorkFile;
    }
    
    public void setCurrentWorkFile(File f) {
        currentWorkFile = f;
    }
    
    public void processExportRequest() {
        try {
            // SAVE IT TO A FILE
            String mapName = app.getGUI().getFileController().getCurrentWorkFile().getName();
            app.getFileComponent().exportData(dataManager, "./exports/" + mapName + "/" + mapName + ".json", mapName);
            } catch (IOException ex) {
                Logger.getLogger(MapEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        dataManager.processSnapshot();
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
	//ScrollPane canvas = workspace.getCanvas();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Export");
        alert.setHeaderText(null);
        alert.setContentText("The map was successfully exported");

        alert.showAndWait();
//	WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
//	File file = new File("./exports/" + currentWorkFile.getName() + "/" + currentWorkFile.getName());
//	try {
//	    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//	}
//	catch(IOException ioe) {
//	    ioe.printStackTrace();
//	}
    }
    
    public void processSaveAsRequest() {
        
    }
    
    public void processExitRequest() {
        if(saved != true){
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            try {
            // SAVE IT TO A FILE
            app.getFileComponent().saveData(dataManager, currentWorkFile.getPath());
            } catch (IOException ex) {
                Logger.getLogger(MapEditController.class.getName()).log(Level.SEVERE, null, ex);
            }

            // MARK IT AS SAVED
            currentWorkFile = selectedFile;
            saved = true;

            // TELL THE USER THE FILE HAS BEEN SAVED
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(SAVE_COMPLETED_TITLE),props.getProperty(SAVE_COMPLETED_MESSAGE));
        }
            
    }
    
}
