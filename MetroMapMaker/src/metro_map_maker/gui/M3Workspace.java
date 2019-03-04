/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.gui;

import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import static djf.settings.AppPropertyType.SAVE_UNSAVED_WORK_MESSAGE;
import static djf.settings.AppPropertyType.SAVE_UNSAVED_WORK_TITLE;
import djf.ui.AppGUI;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppYesNoCancelDialogSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.WindowEvent;
import static metro_map_maker.css.M3Style.*;
import static metro_map_maker.M3LanguageProperty.*;
import metro_map_maker.data.DraggableImage;
import metro_map_maker.data.DraggableStation;
import metro_map_maker.data.DraggableText;
import metro_map_maker.data.M3Data;
import metro_map_maker.data.M3Data.LineComboBoxItem;
import metro_map_maker.data.M3Data.StationComboBoxItem;
import properties_manager.PropertiesManager;

/**
 *
 * @author alexc
 */
public class M3Workspace extends AppWorkspaceComponent {
    
    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

    // THIS WILL CONTAIN THE CUT/COPY/PASTE BUTTONS
    private HBox fileToolbarPane;
    
    // THIS WILL CONTAIN THE UNDO/REDO BUTTONS
    private HBox undoRedoToolbarPane;
    
    // THIS WILL CONTAIN THE ABOUT BUTTON
    private HBox aboutToolbarPane;
    
    // THESE ARE THE UNDO AND REDO BUTTONS
    protected Button undoButton;
    protected Button redoButton;
    
    // THIS IS THE ABOUT BUTTON
    protected Button aboutButton;

    // HAS ALL THE CONTROLS FOR EDITING
    VBox editToolbar;
    VBox editToolbarContainer;
    
    ScrollPane editScrollPane;
    
    VBox row1VBox;
    
    HBox row1HBoxA;
    Label linesLabel;
    ComboBox linesComboBox;
    Circle lineEdit;
    Text lineColor;
    
    HBox row1HBoxB;
    Button lineAdd;
    Button lineRemove;
    Button addStationToLine;
    Button removeStationFromLine;
    Button listLines;
    
    HBox row1HBoxC;
    Slider lineThickness;
    
    VBox row2VBox;
    
    HBox row2HBoxA;
    Label stationsLabel;
    ComboBox stationsComboBox;
    Circle stationEdit;
    Text stationColor;
    
    HBox row2HBoxB;
    Button stationAdd;
    Button stationRemove;
    Button snapToGrid;
    Button moveLabel;
    Button rotateLabel;
    
    HBox row2HBoxC;
    Slider stationRadius;
    
    HBox row3HBox;
    
    HBox row3HBoxA;
    VBox row3VBoxA;
    ComboBox stationStart;
    ComboBox stationEnd;
    
    HBox row3HBoxB;
    Button bestRoute;
    
    VBox row4VBox;
    
    HBox row4HBoxA;
    Label decorLabel;
    //ColorPicker decorColor;
    Circle backgroundEdit;
    Text backgroundColor;
    
    HBox row4HBoxB;
    Button setImageBackground;
    Button addImage;
    Button addLabel;
    Button removeElement;
    
    VBox row5VBox;
    
    HBox row5HBoxA;
    Label fontLabel;
    ColorPicker fontColor;
    
    HBox row5HBoxB;
    Button boldText;
    Button italicText;
    ComboBox fontSize;
    ComboBox fontFamily;
    
    VBox row6VBox;
    
    HBox row6HBoxA;
    Label navigationLabel;
    CheckBox gridBox;
    
    HBox row6HBoxB;
    Button zoomIn;
    Button zoomOut;
    Button increaseMapSize;
    Button decreaseMapSize;
    
    Region filler1;
    Region filler2;
    Region filler3;
    Region filler4;
    Region filler5;
    Region filler6;
    Region filler7;
    
    HBox border1;
    HBox border2;
    HBox border3;
    
    Pane canvas;
    StackPane stackPane;
    ScrollPane scallablePane;
    Group initGroup;
    Group group;
    Scale scale;
    
    
    CanvasController canvasController;
    
    MapEditController mapEditController;
    
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    Text debugText;
    
    PropertiesManager props = PropertiesManager.getPropertiesManager();
    
    public Pane getCanvas() {
        return canvas;
    }
    public StackPane getStackPane() {
        return stackPane;
    }
    public ScrollPane getScallableCanvas() {
        return scallablePane;
    }
    
    public MapEditController getMapEditController() {
        return mapEditController;
    }
    
    public EventHandler<ActionEvent> fontStyleHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if(canvasController.getNode() instanceof DraggableText)
                mapEditController.processChangeFontStyle((DraggableText)canvasController.getNode());
        }
    };

    public M3Workspace(AppTemplate initApp) {
        
        // KEEP THIS FOR LATER
	app = initApp;
        
        // KEEP THE GUI FOR LATER
	gui = app.getGUI();
        
        undoRedoToolbar();
        
        aboutToolbar();
        
        // LAYOUT THE APP
        initLayout();
        
        // HOOK UP THE CONTROLLERS
        initControllers();
        
        // AND INIT THE STYLE FOR THE WORKSPACE
        initStyle(); 
        
    }
    
    private void undoRedoToolbar() {
        // KEEP TOOLBAR
        border2 = new HBox();
        border2.getStyleClass().add(CLASS_FILE_TOOLBAR);
        gui.getFileToolbar().getChildren().add(border2);
        undoRedoToolbarPane = new HBox(5);
    
        // ADD UNDO AND REDO BUTTONS
        undoButton = gui.initChildButton(undoRedoToolbarPane, "UNDO_ICON", "UNDO_TOOLTIP", true);
        redoButton = gui.initChildButton(undoRedoToolbarPane, "REDO_ICON", "REDO_TOOLTIP", true);
    
        redoButton.getStyleClass().add(CLASS_BUTTON);
        undoButton.getStyleClass().add(CLASS_BUTTON);
        
        // BUTTON EVENT HANDLERS
        undoButton.setOnAction(e ->  { 
            mapEditController.processUndoRequest();
        });
        
        redoButton.setOnAction(e ->  { 
            mapEditController.processRedoRequest();
        });
        
        gui.getFileToolbar().getChildren().add(undoRedoToolbarPane);
        
        border3 = new HBox();
        border3.getStyleClass().add(CLASS_FILE_TOOLBAR);
        
        gui.getFileToolbar().getChildren().add(border3);
    }
    
    private void aboutToolbar() {
        aboutToolbarPane = new HBox(5);
        
        aboutButton = gui.initChildButton(aboutToolbarPane, ABOUT_ICON.toString(), ABOUT_TOOLTIP.toString(), false);
        
        aboutButton.setOnAction(e ->  { 
            mapEditController.processAboutRequest();
        });
        
        aboutButton.getStyleClass().add(CLASS_BUTTON);
        
        gui.getFileToolbar().getChildren().add(aboutToolbarPane);
    }
    
    private void initLayout() {
        
        editToolbar = new VBox();
        editToolbarContainer = new VBox();
        
        filler1 = new Region();
        HBox.setHgrow(filler1, Priority.ALWAYS);
        filler2 = new Region();
        HBox.setHgrow(filler2, Priority.ALWAYS);
        filler3 = new Region();
        HBox.setHgrow(filler3, Priority.ALWAYS);
        filler4 = new Region();
        HBox.setHgrow(filler4, Priority.ALWAYS);
        filler5 = new Region();
        HBox.setHgrow(filler5, Priority.ALWAYS);
        filler6 = new Region();
        HBox.setHgrow(filler6, Priority.ALWAYS);
        filler7 = new Region();
        HBox.setHgrow(filler7, Priority.ALWAYS);
        
        // ROW 1
        row1VBox = new VBox();
        row1HBoxA = new HBox();
        row1HBoxB = new HBox();
        row1HBoxC = new HBox();
        
        row1HBoxB.setSpacing(10);
        
        linesLabel = new Label(props.getProperty(LINE_LABEL.toString()));
        linesComboBox = new ComboBox();
        linesComboBox.setMinWidth(150);
        lineEdit = new Circle(20);
        lineEdit.setStroke(Paint.valueOf("#000000"));
        lineEdit.setFill(Paint.valueOf("#ffffff"));
        lineColor = new Text();
        lineColor.xProperty().bind(lineEdit.centerXProperty());
        lineColor.yProperty().bind(lineEdit.centerYProperty());
        StackPane stack = new StackPane();
        stack.getChildren().addAll(lineEdit, lineColor);
        
        
        Tooltip linesComboBoxTooltip = new Tooltip(props.getProperty(LINE_TOOLTIP.toString()));
        Tooltip linesColorPickerTooltip = new Tooltip(props.getProperty(LINE_COLOR_TOOLTIP.toString()));
        
        linesComboBox.setTooltip(linesComboBoxTooltip);
        linesComboBox.setPromptText("Metro Lines");
        //lineEdit.setTooltip(linesColorPickerTooltip);
        
        row1HBoxA.getChildren().addAll(linesLabel, filler1, linesComboBox, filler2, stack);
        
        lineAdd = gui.initChildButton(row1HBoxB, LINE_ADD_ICON.toString(), LINE_ADD_TOOLTIP.toString(), false);
        lineRemove = gui.initChildButton(row1HBoxB, LINE_REMOVE_ICON.toString(), LINE_REMOVE_TOOLTIP.toString(), true);
        addStationToLine = gui.initChildButton(row1HBoxB, "", ADD_STATION_TOOLTIP.toString(), true);
        removeStationFromLine = gui.initChildButton(row1HBoxB, "", REMOVE_STATION_TOOLTIP.toString(), true);
        listLines = gui.initChildButton(row1HBoxB, EDIT_LINE_ICON.toString(), EDIT_LINE_TOOLTIP.toString(), true);
        
        addStationToLine.setText("Add" + '\n' + "Station");
        removeStationFromLine.setText("Remove" + '\n' + "Station");
        
        lineAdd.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        lineRemove.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        addStationToLine.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        removeStationFromLine.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        //editLine.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        
        lineThickness = new Slider();
        lineThickness.setShowTickMarks(true);
        lineThickness.setMinWidth(250);
        lineThickness.setMin(5);
        lineThickness.setMax(20);
        lineThickness.setValue(8);
        
        row1HBoxC.getChildren().add(lineThickness);
        
        row1VBox.getChildren().addAll(row1HBoxA, row1HBoxB, row1HBoxC);
        
        // ROW 2
        row2VBox = new VBox();
        
        row2HBoxA = new HBox();
        row2HBoxB = new HBox();
        row2HBoxC = new HBox();
        
        row2HBoxB.setSpacing(10);
        row2HBoxC.setSpacing(10);
        
        stationsLabel = new Label(props.getProperty(STATION_LABEL.toString()));
        stationsComboBox = new ComboBox();
        stationsComboBox.setPromptText("Metro Stations");
        stationEdit = new Circle(20);
        stationEdit.setStroke(Paint.valueOf("#000000"));
        stationEdit.setFill(Paint.valueOf("#ffffff"));
        stationsComboBox.setMinWidth(150);
        stationColor = new Text();
        stationColor.xProperty().bind(stationEdit.centerXProperty());
        stationColor.yProperty().bind(stationEdit.centerYProperty());
        StackPane stackStation = new StackPane();
        stackStation.getChildren().addAll(stationEdit, stationColor);
        
        row2HBoxA.getChildren().addAll(stationsLabel, filler3, stationsComboBox, filler4, stackStation);
        
        Tooltip stationsComboBoxTooltip = new Tooltip(props.getProperty(STATION_TOOLTIP.toString()));
        Tooltip stationsColorPickerTooltip = new Tooltip(props.getProperty(STATION_COLOR_TOOLTIP.toString()));
        
        stationsComboBox.setTooltip(stationsComboBoxTooltip);
        //linesColorPicker.setTooltip(stationsColorPickerTooltip);

        stationAdd = gui.initChildButton(row2HBoxB, STATION_ADD_ICON.toString(), STATION_ADD_TOOLTIP.toString(), false);
        stationRemove = gui.initChildButton(row2HBoxB, STATION_REMOVE_ICON.toString(), STATION_REMOVE_TOOLTIP.toString(), true);
        snapToGrid = gui.initChildButton(row2HBoxB, "", SNAP_TOOLTIP.toString(), false);
        moveLabel = gui.initChildButton(row2HBoxB, "", MOVE_LABEL_TOOLTIP.toString(), false);
        rotateLabel = gui.initChildButton(row2HBoxB, ROTATE_ICON.toString(), ROTATE_TOOLTIP.toString(), false);
        
        snapToGrid.setText("Snap");
        moveLabel.setText("Move" + '\n' + "Label");
        
        stationAdd.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        stationRemove.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        snapToGrid.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        moveLabel.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        rotateLabel.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        
        stationRadius = new Slider();
        stationRadius.setShowTickMarks(true);
        stationRadius.setMin(3);
        stationRadius.setMax(15);
        stationRadius.setMinWidth(250);
        stationRadius.setValue(10);
        
        row2HBoxC.getChildren().add(stationRadius);
        
        row2VBox.getChildren().addAll(row2HBoxA, row2HBoxB, row2HBoxC);
        
        // ROW 3
        row3HBox = new HBox();
    
        row3HBoxA = new HBox();
        row3VBoxA = new VBox();
        row3VBoxA.setSpacing(10);
        stationStart = new ComboBox();
        stationStart.setPromptText("Start");
        stationStart.setMinWidth(200);
        stationEnd = new ComboBox();
        stationEnd.setPromptText("Destination");
        stationEnd.setMinWidth(200);
        
        row3VBoxA.getChildren().addAll(stationStart, stationEnd);
        row3HBoxA.getChildren().add(row3VBoxA);

        row3HBoxB = new HBox();
        bestRoute = gui.initChildButton(row3HBoxB, BEST_ROUTE_ICON.toString(), BEST_ROUTE_TOOLTIP.toString(), false);
        bestRoute.setMinSize(75, 65);
        
        row3HBox.getChildren().addAll(row3HBoxA, row3HBoxB);
        
        // ROW 4
        row4VBox = new VBox();
        row4HBoxA = new HBox();
        row4HBoxB = new HBox();

        row4HBoxB.setSpacing(10);
        
        decorLabel = new Label(props.getProperty(DECOR_LABEL.toString()));
        //decorColor = new ColorPicker();
        backgroundEdit = new Circle(20);
        backgroundEdit.setStroke(Paint.valueOf("#000000"));
        backgroundEdit.setFill(Paint.valueOf("#ffffff"));
        backgroundColor = new Text();
        backgroundColor.xProperty().bind(lineEdit.centerXProperty());
        backgroundColor.yProperty().bind(lineEdit.centerYProperty());
        StackPane stackBackground = new StackPane();
        stackBackground.getChildren().addAll(backgroundEdit, backgroundColor);
        
        row4HBoxA.getChildren().addAll(decorLabel, filler5, stackBackground);

        setImageBackground = gui.initChildButton(row4HBoxB, "", IMAGE_BACKGROUND_TOOLTIP.toString(), false);
        addImage = gui.initChildButton(row4HBoxB, "", ADD_IMAGE_TOOLTIP.toString(), false);
        addLabel = gui.initChildButton(row4HBoxB, "", ADD_LABEL_TOOLTIP.toString(), false);
        removeElement = gui.initChildButton(row4HBoxB, "", REMOVE_ELEMENT_TOOLTIP.toString(), true);
        setImageBackground.setText("Set Image" + '\n' + "Background");
        addImage.setText("Add" + '\n' + "Image");
        addLabel.setText("Add" + '\n' + "Label");
        removeElement.setText("Remove" + '\n' + "Element");
        
        setImageBackground.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        addImage.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        addLabel.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        removeElement.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        
        row4VBox.getChildren().addAll(row4HBoxA, row4HBoxB);
        
        // ROW 5
        row5VBox = new VBox();
        row5HBoxA = new HBox();
        row5HBoxB = new HBox();
        
        //row5HBoxA.setSpacing(225);
        row5HBoxB.setSpacing(10);
        
        fontLabel = new Label(props.getProperty(FONT_LABEL.toString()));
        fontColor = new ColorPicker();
        
        row5HBoxA.getChildren().addAll(fontLabel, filler6, fontColor);

        boldText = gui.initChildButton(row5HBoxB, BOLD_ICON.toString(), BOLD_TOOLTIP.toString(), false);
        italicText = gui.initChildButton(row5HBoxB, ITALIC_ICON.toString(), ITALIC_TOOLTIP.toString(), false);
        
        ObservableList<Double> sizes = FXCollections.observableArrayList(
            8.0,
            9.0,
            10.0,
            11.0,
            12.0,
            13.0,
            14.0,
            15.0,
            16.0,
            17.0,
            18.0,
            19.0,
            20.0,
            22.0,
            24.0,
            28.0,
            32.0,
            40.0,
            48.0,
            60.0
        );

        fontSize = new ComboBox<>();
        fontSize.setItems(sizes);
        fontSize.setValue(sizes.get(5));
        fontSize.setMaxWidth(100);
        
        ObservableList<String> types = FXCollections.observableArrayList(
            "Arial",
            "Courier",
            "Georgia",
            "Helvetica",
            "Verdana"
        );
        
        fontFamily = new ComboBox<>();
        fontFamily.setItems(types);
        fontFamily.setValue(types.get(0));
        fontFamily.setMaxWidth(130);
        
        row5HBoxB.getChildren().addAll(fontSize, fontFamily);
        
        row5VBox.getChildren().addAll(row5HBoxA, row5HBoxB);
        
        // ROW 6
        row6VBox = new VBox();
        row6HBoxA = new HBox();
        row6HBoxB = new HBox();
        
        row6HBoxB.setSpacing(10);
        
        navigationLabel = new Label(props.getProperty(NAVIGATION_LABEL.toString()));
        gridBox = new CheckBox("Show Grid");
        
        row6HBoxA.getChildren().addAll(navigationLabel, filler7, gridBox);

        zoomIn = gui.initChildButton(row6HBoxB, ZOOM_IN_ICON.toString(), ZOOM_IN_TOOLTIP.toString(), false);
        zoomOut = gui.initChildButton(row6HBoxB, ZOOM_OUT_ICON.toString(), ZOOM_OUT_TOOLTIP.toString(), false);
        increaseMapSize = gui.initChildButton(row6HBoxB, INCREASE_MAP_ICON.toString(), INCREASE_MAP_TOOLTIP.toString(), false);
        decreaseMapSize = gui.initChildButton(row6HBoxB, DECREASE_MAP_ICON.toString(), DECREASE_MAP_TOOLTIP.toString(), false);
        
        row6VBox.getChildren().addAll(row6HBoxA, row6HBoxB);
        
        editToolbar.setMinWidth(350);
        
        editToolbar.getChildren().addAll(row1VBox, row2VBox, row3HBox, row4VBox, row5VBox, row6VBox);
        
        editScrollPane = new ScrollPane(editToolbar);
        editScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        editScrollPane.setFitToWidth(true);
        editScrollPane.setFitToHeight(true);
        editScrollPane.setPrefViewportWidth(editToolbar.getMinWidth());
	
	// WE'LL RENDER OUR STUFF HERE IN THE CANVAS
	canvas = new Pane();
        stackPane = new StackPane();
        scallablePane = new ScrollPane();
        initGroup = new Group();
        group = new Group();
        scale = new Scale();
        
        //initGroup.getChildren().add(canvas);
        //group.getChildren().add(initGroup);
        //initGroup.getTransforms().add(scale);
        canvas.getTransforms().add(scale);
        
        scallablePane.setContent(canvas);
        
        canvas.setMinSize(200, 200);
        canvas.setPrefSize(910, 690);
        
        //canvas.getChildren().add(stackPane);
        canvas.setBackground(new Background(new BackgroundFill(Color.valueOf("#ffffff"), CornerRadii.EMPTY, Insets.EMPTY)));
	debugText = new Text();
	canvas.getChildren().add(debugText);
	debugText.setX(100);
	debugText.setY(100);
	
	// AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
	M3Data data = (M3Data)app.getDataComponent();
	data.setNodes(canvas.getChildren());

	// AND NOW SETUP THE WORKSPACE
	workspace = new BorderPane();
	((BorderPane)workspace).setLeft(editScrollPane);
	((BorderPane)workspace).setCenter(scallablePane);
    }
    
    public ComboBox getLinesComboBox() {
        return linesComboBox;
    }
    public ComboBox getStationsComboBox() {
        return stationsComboBox;
    }
    public Circle getLineEditor() {
        return lineEdit;
    }
    public Circle getStationEditor() {
        return stationEdit;
    }
    public Text getLineColor() {
        return lineColor;
    }
    public Text getStationColor() {
        return stationColor;
    }
    public Button getLineRemoveButton() {
        return lineRemove;
    }
    public Button getUndoButton() {
        return undoButton;
    }
    public Button getRedoButton() {
        return redoButton;
    }
    public Button getLineAdd() {
        return lineAdd;
    }
    public Button getLineRemove() {
        return lineRemove;
    }
    public Button getAddStationToLine() {
        return addStationToLine;
    }
    public Button getRemoveStationFromLine() {
        return removeStationFromLine;
    }
    public Button getListLines() {
        return listLines;
    }
    public Slider getLineThickness() {
        return lineThickness;
    }
    public Button getStationAdd() {
        return stationAdd;
    }
    public Button getStationRemove() {
        return stationRemove;
    }
    public Button getSnap() {
        return snapToGrid;
    }
    public Button getMoveLabel() {
        return moveLabel;
    }
    public Button getRotateLabel() {
        return rotateLabel;
    }
    public Slider getStationRadius() {
        return stationRadius;
    }
    public Text getBackgroundColor() {
        return backgroundColor;
    }
    public Circle getBackgroundEditor() {
        return backgroundEdit;
    }
    public ColorPicker getFontColor() {
        return fontColor;
    }
    public ComboBox getFontSize() {
        return fontSize;
    }
    public ComboBox getFontFamily() {
        return fontFamily;
    }
    public Button getZoomIn() {
        return zoomIn;
    }
    public Button getZoomOut() {
        return zoomOut;
    }
    public Button getIncreaseMapSize() {
        return increaseMapSize;
    }
    public Button getDecreaseMapSize() {
        return decreaseMapSize;
    }
    public Scale getScale() {
        return scale;
    }
    public Button getRemoveElement() {
        return removeElement;
    }
    public ComboBox getStationStart() {
        return stationStart;
    }
    public ComboBox getStationEnd() {
        return stationEnd;
    }
    
    private void initControllers() {
        // MAKE THE EDIT CONTROLLER
	mapEditController = new MapEditController(app);
        canvasController = new CanvasController(app);
        
        gui.getExportButton().setOnAction(e -> {
            mapEditController.processExportRequest();
        });
        
        lineAdd.setOnAction(e -> {
            mapEditController.processAddLine();
        });
        lineRemove.setOnAction(e -> {
            mapEditController.processRemoveLine(canvasController);
        });
        lineEdit.setOnMousePressed(e -> {
            if((LineComboBoxItem)linesComboBox.getValue() != null)
                mapEditController.processEditLine((LineComboBoxItem)linesComboBox.getValue());
        });
        lineColor.setOnMousePressed(e -> {
            if((LineComboBoxItem)linesComboBox.getValue() != null)
                mapEditController.processEditLine((LineComboBoxItem)linesComboBox.getValue());
        });
        linesComboBox.setOnAction(e -> {
            mapEditController.processSelectLine();
        });
        addStationToLine.setOnAction(e -> {
            mapEditController.processAddStationToLine();
        });
        removeStationFromLine.setOnAction(e -> {
            mapEditController.processRemoveStationFromLine();
        });
        listLines.setOnAction(e -> {
            if((LineComboBoxItem)linesComboBox.getValue() != null)
                mapEditController.processListStationsInLine((LineComboBoxItem)linesComboBox.getValue());
        });
        lineThickness.valueProperty().addListener(e-> {
	    mapEditController.processChangeLineThickness();
	});
        stationsComboBox.setOnAction(e -> {
            mapEditController.processSelectStation();
        });
        stationEdit.setOnMousePressed(e -> {
            if((StationComboBoxItem)stationsComboBox.getValue() != null)
                mapEditController.processEditStation((StationComboBoxItem)stationsComboBox.getValue());
        });
        stationColor.setOnMousePressed(e -> {
            if((StationComboBoxItem)stationsComboBox.getValue() != null)
                mapEditController.processEditStation((StationComboBoxItem)stationsComboBox.getValue());
        });
        stationAdd.setOnAction(e -> {
            mapEditController.processAddStation();
        });
        stationRemove.setOnAction(e -> {
            mapEditController.processRemoveStation();
        });
        snapToGrid.setOnAction(e -> {
            if(canvasController.getNode() instanceof DraggableStation)
                mapEditController.processSnapToGrid((DraggableStation)canvasController.getNode());
        });
        moveLabel.setOnAction(e -> {
            if((StationComboBoxItem)stationsComboBox.getValue() != null)
                mapEditController.processMoveStationLabel((StationComboBoxItem)stationsComboBox.getValue());
        });
        rotateLabel.setOnAction(e -> {
            mapEditController.processRotateStationLabel((StationComboBoxItem)stationsComboBox.getValue());
        });
        stationRadius.valueProperty().addListener(e-> {
	    mapEditController.processChangeStationRadius();
	});
        bestRoute.setOnAction(e -> {
            if(!stationStart.getItems().isEmpty() && !stationStart.getItems().isEmpty())
                mapEditController.processFindRoute(((StationComboBoxItem)stationStart.getSelectionModel().getSelectedItem()).getStation(), ((StationComboBoxItem)stationEnd.getSelectionModel().getSelectedItem()).getStation(), ((StationComboBoxItem)stationStart.getSelectionModel().getSelectedItem()).getStation().getLines().get(0).getStations());
        });
        backgroundEdit.setOnMousePressed(e -> {
            mapEditController.processEditBackgroundColor();
        });
        backgroundColor.setOnMousePressed(e -> {
            mapEditController.processEditBackgroundColor();
        });
        setImageBackground.setOnAction(e -> {
            mapEditController.processSetImageBackground();
        });
        addImage.setOnAction(e -> {
            mapEditController.processAddImage();
        });
        addLabel.setOnAction(e -> {
            mapEditController.processAddLabel();
        });
        fontColor.setOnAction(e -> {
            if(canvasController.getNode() instanceof DraggableText)
                mapEditController.processChangeFontColor((DraggableText)canvasController.getNode());
        });
        fontSize.setOnAction(e -> {
            if(canvasController.getNode() instanceof DraggableText)
                mapEditController.processChangeFontStyle((DraggableText)canvasController.getNode());
        });
        fontFamily.setOnAction(e -> {
            if(canvasController.getNode() instanceof DraggableText)
                mapEditController.processChangeFontStyle((DraggableText)canvasController.getNode());
        });
        boldText.setOnAction(e -> {
            if(canvasController.getNode() instanceof DraggableText)
                mapEditController.processBoldFont((DraggableText)canvasController.getNode());
        });
        italicText.setOnAction(e -> {
            if(canvasController.getNode() instanceof DraggableText)
                mapEditController.processItalicFont((DraggableText)canvasController.getNode());
        });
        zoomIn.setOnAction(e -> {
            mapEditController.processZoomIn();
        });
        zoomOut.setOnAction(e -> {
            mapEditController.processZoomOut();
        });
        increaseMapSize.setOnAction(e -> {
            mapEditController.processIncreaseMapSize();
        });
        decreaseMapSize.setOnAction(e -> {
            mapEditController.processDecreaseMapSize();
        });
        gridBox.setOnAction(e -> {
            mapEditController.processGridToggle(!gridBox.isSelected());
        });
        removeElement.setOnAction(e -> {
            if((canvasController.getNode() instanceof DraggableText && ((DraggableText)canvasController.getNode()).isLabel()) || ((canvasController.getNode() instanceof DraggableImage) && !((DraggableImage)canvasController.getNode()).isBackground()))
                mapEditController.processRemoveElement(canvasController.getNode());
        });
        
	canvas.setOnMousePressed(e -> {
	    canvasController.processCanvasMousePress((int)e.getX(), (int)e.getY());
	});
        canvas.setOnMouseDragged(e -> {
            canvasController.processCanvasMouseDragged((int)e.getX(), (int)e.getY());
        });
        canvas.setOnMouseReleased(e -> {
            canvasController.processCanvasMouseReleased((int)e.getX(), (int)e.getY());
        });
        
        gui.getPrimaryScene().setOnKeyPressed(e -> {
            if(null != e.getCode()) switch (e.getCode()) {
                case W:
                    scallablePane.setVvalue(scallablePane.getVvalue() - 0.5);
                    break;
                case S:
                    scallablePane.setVvalue(scallablePane.getVvalue() + 0.5);
                    break;
                case A:
                    scallablePane.setHvalue(scallablePane.getHvalue() - 0.5);
                    break;
                case D:
                    scallablePane.setHvalue(scallablePane.getHvalue() + 0.5);
                    break;
                default:
                    break;
            }
        });
        
        gui.getWindow().setOnCloseRequest((WindowEvent event) -> {
            PropertiesManager props1 = PropertiesManager.getPropertiesManager();
            if (mapEditController.currentWorkFile != null && !mapEditController.saved) {
                AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
                yesNoDialog.show(props1.getProperty(SAVE_UNSAVED_WORK_TITLE), props1.getProperty(SAVE_UNSAVED_WORK_MESSAGE));
                String selection = yesNoDialog.getSelection();
                if (selection.equals(AppYesNoCancelDialogSingleton.YES))
                    mapEditController.processExitRequest();
                else if (selection.equals(AppYesNoCancelDialogSingleton.CANCEL))
                    event.consume();    
            }
        });
    }
    
    public void initStyle() {
        editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
        row1VBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row2VBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row3HBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row4VBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row5VBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row6VBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
    }
    
    @Override
    public void resetWorkspace() {
        M3Data data = (M3Data)app.getDataComponent();
        linesComboBox.getItems().clear();
        stationsComboBox.getItems().clear();
        data.getJTPS().clear();
        data.getNodes().clear();
        data.disableUndoRedo();
    }

    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {

    }  
}
