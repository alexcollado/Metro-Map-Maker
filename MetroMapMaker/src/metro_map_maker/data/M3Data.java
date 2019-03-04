/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.data;

import djf.AppTemplate;
import djf.components.AppDataComponent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import metro_map_maker.gui.M3Workspace;
import metro_map_maker.transactions.AddImage;
import metro_map_maker.transactions.AddLabel;
import metro_map_maker.transactions.AddLine;
import metro_map_maker.transactions.AddStation;
import metro_map_maker.transactions.ChangeFontColor;
import metro_map_maker.transactions.ChangeFontStyle;
import metro_map_maker.transactions.ChangeLabelPosition;
import metro_map_maker.transactions.DecreaseMapSize;
import metro_map_maker.transactions.EditBackgroundColor;
import metro_map_maker.transactions.EditLine;
import metro_map_maker.transactions.EditStation;
import metro_map_maker.transactions.IncreaseMapSize;
import metro_map_maker.transactions.RemoveLine;
import metro_map_maker.transactions.RemoveMapElement;
import metro_map_maker.transactions.RemoveStation;
import metro_map_maker.transactions.RotateLabel;
import metro_map_maker.transactions.SetImageBackground;
import metro_map_maker.transactions.SetLineThickness;
import metro_map_maker.transactions.SetStationRadius;
import metro_map_maker.transactions.SnapToGrid;

/**
 *
 * @author alexc
 */
public class M3Data implements AppDataComponent {
    public jTPS jTPS = new jTPS();
    
    // THESE ARE THE SHAPES TO DRAW
    ObservableList<Node> nodes;
    
    // THE BACKGROUND COLOR
    Color backgroundColor;
    
    DraggableImage backgroundImage;
    
    // AND NOW THE EDITING DATA

    // THIS IS THE SHAPE CURRENTLY BEING SIZED BUT NOT YET ADDED
    Node newNode;

    // THIS IS THE SHAPE CURRENTLY SELECTED
    Node selectedNode;
    
    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
     // FOR FILL AND OUTLINE
    Color currentFillColor;
    //Color currentOutlineColor;
    //double currentBorderWidth;

    // CURRENT STATE OF THE APP
    M3State state;
    
    // USE THIS WHEN THE SHAPE IS SELECTED
    Effect highlightedEffect;

    public static final String WHITE_HEX = "#FFFFFF";
    public static final String BLACK_HEX = "#000000";
    public static final String YELLOW_HEX = "#EEEE00";
    public static final Paint DEFAULT_BACKGROUND_COLOR = Paint.valueOf(WHITE_HEX);
    public static final Paint HIGHLIGHTED_COLOR = Paint.valueOf(YELLOW_HEX);
    public static final int HIGHLIGHTED_STROKE_THICKNESS = 3;

    public M3Data(AppTemplate initApp) {
        // KEEP THE APP FOR LATER
	app = initApp;

	// NO SHAPE STARTS OUT AS SELECTED
	newNode = null;
	selectedNode = null;

//	// INIT THE COLORS
	currentFillColor = Color.web(WHITE_HEX);
	//currentOutlineColor = Color.web(BLACK_HEX);
	//currentBorderWidth = 1;
	
	// THIS IS FOR THE SELECTED SHAPE
	DropShadow dropShadowEffect = new DropShadow();
	dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(1.0);
	dropShadowEffect.setColor(Color.YELLOW);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(15);
	highlightedEffect = dropShadowEffect;
    }

    @Override
    public void resetData() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setNodes(ObservableList<Node> initNodes) {
	nodes = initNodes;
    }
    
    public ObservableList<Node> getNodes() {
        return nodes;
    }
    
    public void setCurrentFontFamily(Font fontFamily) {
        
    }
    
    public void setCurrentFontSize(double fontSize) {
        
    }
    
    public void boldText(Font font) {
        
    }
    
    public void italicText(Font font) {
        
    }
    
    public Pane getCanvas() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        return workspace.getCanvas();
    }
    
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    public void setCurrentFillColor(Color initColor) {
        
    }
    
    public void setCurrentOutlineThickness(int initBorderWidth) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
	if (workspace.getLinesComboBox().getValue() != null) {
            LineComboBoxItem item = (LineComboBoxItem)workspace.getLinesComboBox().getSelectionModel().getSelectedItem();
            DraggableLine line = item.line;
            double prevLineThickness = line.getStrokeWidth();
            double currentLineThickness = initBorderWidth;
            jTPS_Transaction transaction = new SetLineThickness(line, currentLineThickness, prevLineThickness, workspace);
            jTPS.addTransaction(transaction);
            workspace.getUndoButton().setDisable(false);
            if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
                workspace.getRedoButton().setDisable(true);
	}
    }
    
    public void setStationRadius(int radius) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
	if (workspace.getStationsComboBox().getValue() != null) {
            StationComboBoxItem item = (StationComboBoxItem)workspace.getStationsComboBox().getSelectionModel().getSelectedItem();
            DraggableStation station = item.station;
            double prevStationRadius = station.getRadius();
            double currentStationRadius = radius;
            jTPS_Transaction transaction = new SetStationRadius(station, currentStationRadius, prevStationRadius, workspace);
            jTPS.addTransaction(transaction);
            workspace.getUndoButton().setDisable(false);
            if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
                workspace.getRedoButton().setDisable(true);
	}
    }
    
    public class LineComboBoxItem {
        String name;
        DraggableLine line;
        
        public LineComboBoxItem(String name, DraggableLine line) {
            this.name = name;
            this.line = line;
        }
        
        @Override
        public String toString() {
            return name;
        }
        
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public DraggableLine getLine() {
            return line;
        }
        public void setLine(DraggableLine line) {
            this.line = line;
        }
    }
    public class StationComboBoxItem {
        String name;
        DraggableStation station;
        
        public StationComboBoxItem(String name, DraggableStation station) {
            this.name = name;
            this.station = station;
        }
        
        @Override
        public String toString() {
            return name;
        }
        
        public DraggableStation getStation() {
            return station;
        }
    }
    
    public DraggableLine removeSelectedLine() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        DraggableLine selectedLine = ((LineComboBoxItem)workspace.getLinesComboBox().getSelectionModel().getSelectedItem()).line;
        
        jTPS_Transaction transaction = new RemoveLine(selectedLine, nodes, selectedLine.getItem(), workspace);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
        selectedNode = null;
        return selectedLine;
    }
    
    public void removeSelectedStation() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        DraggableStation selectedStation = ((StationComboBoxItem)workspace.getStationsComboBox().getSelectionModel().getSelectedItem()).station;
        
        jTPS_Transaction transaction = new RemoveStation(selectedStation, nodes, selectedStation.getItem(), workspace);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
        selectedNode = null;
    }
    
    public void selectNode() {
        
    }
    
    public void highlightNode(Node node) {
        
    }
    
    public void unhighlightNode(Node node) {
        
    }
    
    public void startNewStation(String text) {
        DraggableText label = new DraggableText(text);
        label.setIsStationLabel(true);
        
        DraggableStation newStation = new DraggableStation(label, this);
        StationComboBoxItem item = new StationComboBoxItem(text, newStation);
        newStation.setItem(item);
	newNode = newStation;
        
        initNewStation();
    }
    
    public void startNewLine(String text, Color color) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        DraggableText nameStart = new DraggableText(text);
        DraggableText nameEnd = new DraggableText(text);
        nameEnd.setText(text);
        
        DraggableLine newLine = new DraggableLine(nameStart, nameEnd, color, this);
        LineComboBoxItem item = new LineComboBoxItem(text, newLine);
        newLine.setItem(item);
	newNode = newLine;
        
        workspace.getLineEditor().setFill(color);
        workspace.getLineColor().setText("#" + color.toString().substring(2, 7));
        workspace.getLineColor().setFont(Font.font(10));
        workspace.getLineColor().setFill(Color.valueOf("#ffffff"));
        workspace.getLineColor().setStrokeType(StrokeType.OUTSIDE);
        workspace.getLineColor().setStroke(Color.valueOf("#000000"));
        
	initNewLine();
    }
    
    public void initNewStation() {
        if (selectedNode != null) {
	    unhighlightNode(selectedNode);
	    selectedNode = null;
	}
        
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        jTPS_Transaction transaction = new AddStation((DraggableStation) newNode, nodes, ((DraggableStation) newNode).getItem(), workspace);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public void initNewLine() {
	if (selectedNode != null) {
	    unhighlightNode(selectedNode);
	    selectedNode = null;
	}

	M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        jTPS_Transaction transaction = new AddLine((DraggableLine) newNode, nodes, ((DraggableLine) newNode).getItem(), workspace);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public void loadLineColor() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        LineComboBoxItem item = (LineComboBoxItem)workspace.getLinesComboBox().getSelectionModel().getSelectedItem();
        if(item != null) {
            workspace.getLineEditor().setFill(item.line.getColor());
            workspace.getLineColor().setText("#" + item.line.getColor().toString().substring(2, 7));
        } else {
            workspace.getLineEditor().setFill(Color.valueOf("#ffffff"));
            workspace.getLineColor().setText("");
        }
    }
    
    public void loadStationColor() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        StationComboBoxItem item = (StationComboBoxItem)workspace.getStationsComboBox().getSelectionModel().getSelectedItem();
        if(item != null) {
            workspace.getStationEditor().setFill(item.station.getColor());
            workspace.getStationColor().setText("#" + item.station.getColor().toString().substring(2, 7));
        } else {
            workspace.getStationEditor().setFill(Color.valueOf("#ffffff"));
            workspace.getStationColor().setText("");
        }
    }
    
    public void loadBackgroundColor() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        Color color = (Color)workspace.getCanvas().getBackground().getFills().get(0).getFill();
        workspace.getBackgroundEditor().setFill(color);
        workspace.getBackgroundColor().setText("#" + color.toString().substring(2, 7));
    }
    
    public void editLine(LineComboBoxItem item, DraggableLine line, String text, Color color, boolean isCircular) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        LineComboBoxItem prevItem = line.item;
        String prevText = line.getName();
        Color prevColor = line.color;
        boolean prevCircular = line.circular;
        jTPS_Transaction transaction = new EditLine(item, line, text, color, isCircular, prevItem, prevText, prevColor, prevCircular, workspace);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public void editStation(StationComboBoxItem item, DraggableStation station, Color color) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        Color prevColor = station.getColor();
        jTPS_Transaction transaction = new EditStation(item, station, color, prevColor, workspace);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public void editBackgroundColor(Color currentColor) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        backgroundColor = currentColor;
        Color prevColor = (Color)workspace.getCanvas().getBackground().getFills().get(0).getFill();
        jTPS_Transaction transaction = new EditBackgroundColor(currentColor, prevColor, workspace);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public void changeLabelPosition(StationComboBoxItem item) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        DraggableStation station = item.station;
        jTPS_Transaction transaction = new ChangeLabelPosition(station);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public void rotateLabel(StationComboBoxItem item) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        DraggableStation station = item.station;
        jTPS_Transaction transaction = new RotateLabel(station);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public void setImageBackground(DraggableImage image) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        DraggableImage prevImage = null;
        backgroundImage = image;
        if(!nodes.isEmpty() && nodes.get(0) instanceof DraggableImage && ((DraggableImage)nodes.get(0)).isBackground)
            prevImage = (DraggableImage)nodes.get(0);
        jTPS_Transaction transaction = new SetImageBackground(image, prevImage, nodes);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public DraggableImage getImageBackground() {
        return backgroundImage;
    }
    
    public void addLabel(String text) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        DraggableText label = new DraggableText(text, true);
        jTPS_Transaction transaction = new AddLabel(label, nodes);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public void loadTextColor(DraggableText label) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        workspace.getFontColor().setValue(label.getColor());
    }
    
    public void loadTextStyle(DraggableText label) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        workspace.getFontFamily().setOnAction((EventHandler)null);
        workspace.getFontSize().setOnAction((EventHandler)null);
        workspace.getFontFamily().setValue(label.getFont().getName());
        workspace.getFontSize().setValue(label.getFont().getSize());
        workspace.getFontFamily().setOnAction((EventHandler)workspace.fontStyleHandler);
        workspace.getFontSize().setOnAction((EventHandler)workspace.fontStyleHandler);
    }
    
    public void changeFontColor(DraggableText label) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        Color currentColor = workspace.getFontColor().getValue();
        jTPS_Transaction transaction = new ChangeFontColor(label, currentColor, label.getColor());
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public void setCurrentFontStyle(DraggableText label, Font font) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        Font prevFont = label.getFont();
        jTPS_Transaction transaction = new ChangeFontStyle(label, font, prevFont);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public void zoomIn() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        workspace.getScale().setX(workspace.getScale().getX() * 1.1);
        workspace.getScale().setY(workspace.getScale().getY() * 1.1);
    }
    
    public void zoomOut() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        workspace.getScale().setX(workspace.getScale().getX() * 0.9);
        workspace.getScale().setY(workspace.getScale().getY() * 0.9);
    }
    
    public void increaseMapSize() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        jTPS_Transaction transaction = new IncreaseMapSize(workspace);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
        //workspace.getCanvas().setMinSize(workspace.getCanvas().getWidth() * 1.1, workspace.getCanvas().getHeight() * 1.1);
    }
    
    public void decreaseMapSize() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        jTPS_Transaction transaction = new DecreaseMapSize(workspace);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
        //workspace.getCanvas().setMinSize(workspace.getCanvas().getWidth() * 1.1, workspace.getCanvas().getHeight() * 1.1);
    }
    
    public void snapToGrid(DraggableStation station) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        jTPS_Transaction transaction = new SnapToGrid(station);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public void insertNewImage() {
        
    }
    
    public void insertNewStationLabel(Text text) {
        
    }
    
    public Node selectNode(int x, int y) {
        Node element = getNode(x, y);
        selectedNode = element;
        if (element != null && (element instanceof DraggableImage || element instanceof DraggableText))
	    ((Draggable)element).start(x, y);
        return element;
    }
    
    public Node getNode(int x, int y) {
        for (int i = nodes.size() - 1; i >= 0; i--) {
	    Node node = nodes.get(i);
	    if (node.contains(x, y)) {
		return node;
	    }
	}
	return null;
    }
    
    public void addStation(DraggableStation stationToAdd) {
	nodes.add(stationToAdd);
    }
    
    public void addLine(DraggableLine lineToAdd) {
        nodes.add(lineToAdd);
    }
    
    public void addText(DraggableText textToAdd) {
        nodes.add(textToAdd);
    }
    
    public void addImage(DraggableImage imageToAdd) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        jTPS_Transaction transaction = new AddImage(imageToAdd, nodes, workspace);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public void undo() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        jTPS.undoTransaction();
        workspace.getRedoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == 0)
            workspace.getUndoButton().setDisable(true);
    }
    public void redo() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        jTPS.doTransaction();
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax())
            workspace.getRedoButton().setDisable(true);
    }
    
    public jTPS getJTPS() {
        return jTPS;
    }
    
    public void disableUndoRedo() {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        workspace.getRedoButton().setDisable(true);
        workspace.getUndoButton().setDisable(true);
    }
    
    public void addNode(Node node) {
        
    }
    
    public void removeNode(Node node) {
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        jTPS_Transaction transaction = new RemoveMapElement(nodes, node);
        jTPS.addTransaction(transaction);
        workspace.getUndoButton().setDisable(false);
        if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
            workspace.getRedoButton().setDisable(true);
    }
    
    public Node getSelectedNode() {
	return selectedNode;
    }

    public void setSelectedNode(Node initSelectedNode) {
	selectedNode = initSelectedNode;
    }
    
    public boolean isInState(M3State testState) {
        return state == testState;
    }
    
    public void setState(M3State initState) {
	state = initState;
    }
    
    public void processSnapshot() {
	M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        //dataManager.clearSnapshotHighlight();
	Pane canvas = workspace.getCanvas();
	WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
	File file = new File("./exports/" + app.getGUI().getFileController().getCurrentWorkFile().getName() + "/" + app.getGUI().getFileController().getCurrentWorkFile().getName() + ".png");
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(M3Data.class.getName()).log(Level.SEVERE, null, ex);
        }
	try {
	    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	}
	catch(IOException ioe) {
	    ioe.printStackTrace();
	}
    }
}
