/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.gui;

import djf.AppTemplate;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import metro_map_maker.data.Draggable;
import metro_map_maker.data.DraggableImage;
import metro_map_maker.data.DraggableLine;
import metro_map_maker.data.DraggableStation;
import metro_map_maker.data.DraggableText;
import metro_map_maker.data.M3Data;
import metro_map_maker.data.M3Data.LineComboBoxItem;
import metro_map_maker.data.M3State;
import static metro_map_maker.data.M3State.ADD_STATION_MODE;
import static metro_map_maker.data.M3State.DRAGGING_IMAGE;
import static metro_map_maker.data.M3State.DRAGGING_LINE_END;
import static metro_map_maker.data.M3State.DRAGGING_STATION;
import static metro_map_maker.data.M3State.DRAGGING_TEXT;
import static metro_map_maker.data.M3State.NEUTRAL;
import static metro_map_maker.data.M3State.REMOVE_STATION_MODE;
import static metro_map_maker.data.M3State.SELECTED_TEXT;
import metro_map_maker.transactions.AddStationToLine;
import metro_map_maker.transactions.DragImage;
import metro_map_maker.transactions.DragStation;
import metro_map_maker.transactions.DragText;
import metro_map_maker.transactions.RemoveStationFromLine;

/**
 *
 * @author alexc
 */
public class CanvasController {
    AppTemplate app;
    
    DraggableStation endPoint;
    DraggableText endPointText;
    DraggableLine selectedLine;
    DraggableStation selectedStation;
    DraggableText selectedText;
    
    Node node;
    
    DropShadow dropShadowEffect;
    Effect highlightedEffect;
    
    jTPS_Transaction transaction;
    
    public void setDraggableEndPoint(DraggableStation endPoint) {
        this.endPoint = endPoint;
    }
    public void setDraggableText(DraggableText endPointText) {
        this.endPointText = endPointText;
    } 
    public DraggableLine getSelectedLine() {
        return selectedLine;
    }
    public Node getNode() {
        return node;
    }
    
    public CanvasController(AppTemplate app) {
        this.app = app;
    }
    
    public void processCanvasMousePress(int x, int y) {
        M3Data dataManager = (M3Data) app.getDataComponent();
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        node = dataManager.selectNode(x, y);
        dataManager.setSelectedNode(node);
        dropShadowEffect = new DropShadow();
        dropShadowEffect.setOffsetX(0.0f);
        dropShadowEffect.setOffsetY(0.0f);
        dropShadowEffect.setSpread(1.0);
        dropShadowEffect.setColor(Color.YELLOW);
        dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
        dropShadowEffect.setRadius(10);
        highlightedEffect = dropShadowEffect;
        if(node != null && (node instanceof DraggableImage) && !((DraggableImage)node).isBackground()) {
            node.setEffect(highlightedEffect);
            transaction = new DragImage((Draggable) node, x, y);
            dataManager.setState(M3State.DRAGGING_IMAGE);
            dataManager.setSelectedNode(node);
            workspace.getRemoveElement().setDisable(false);
            app.getGUI().updateToolbarControls(false);
            return;
        }
        if(dataManager.isInState(SELECTED_TEXT) && node == null) {
            for(Node nodes:dataManager.getNodes()) {
                nodes.setEffect(null);
            }
            workspace.getRemoveElement().setDisable(true);
            //dataManager.loadTextColor((DraggableText)node);
            dataManager.setState(NEUTRAL);
        } else if(dataManager.isInState(SELECTED_TEXT) && node != null && node instanceof DraggableText) {
            for(Node nodes:dataManager.getNodes()) {
                nodes.setEffect(null);
            }
            //dataManager.getSelectedNode().setEffect(null);
            node.setEffect(highlightedEffect);
            dataManager.loadTextColor((DraggableText)node);
            dataManager.loadTextStyle((DraggableText)node);
            workspace.getRemoveElement().setDisable(false);
        }
        if(node != null && (node instanceof DraggableText) && ((DraggableText)node).isLabel()) {
            node.setEffect(highlightedEffect);
            transaction = new DragText((Draggable) node, x, y);
            dataManager.setState(M3State.DRAGGING_TEXT);
            dataManager.setSelectedNode(node);
            dataManager.loadTextColor((DraggableText)node);
            dataManager.loadTextStyle((DraggableText)node);
            workspace.getRemoveElement().setDisable(false);
            app.getGUI().updateToolbarControls(false);
            return;
        }
        if(node != null && (node instanceof DraggableText) && ((DraggableText)node).isStationLabel()) {
            selectedText = (DraggableText)node;
            selectedText.setEffect(highlightedEffect);
            dataManager.setState(M3State.SELECTED_TEXT);
            dataManager.setSelectedNode(node);
            dataManager.loadTextColor((DraggableText)node);
            dataManager.loadTextStyle((DraggableText)node);
            //workspace.getRemoveElement().setDisable(false);
            //app.getGUI().updateToolbarControls(false);
            return;
        }
        if(dataManager.isInState(ADD_STATION_MODE)) {
            if(node instanceof DraggableStation) {
                DraggableLine lineToAddTo = ((LineComboBoxItem)workspace.getLinesComboBox().getSelectionModel().getSelectedItem()).getLine();
                transaction = new AddStationToLine(lineToAddTo, (DraggableStation)node, workspace, dataManager);
                //lineToAddTo.addStation((DraggableStation)node);
                if(dataManager.isInState(NEUTRAL))
                    app.getGUI().getPrimaryScene().setCursor(Cursor.DEFAULT);
                return;
            } else {
                app.getGUI().getPrimaryScene().setCursor(Cursor.DEFAULT);
                dataManager.setState(NEUTRAL);
            }
        }
        if(dataManager.isInState(REMOVE_STATION_MODE)) {
            if(node instanceof DraggableStation) {
                DraggableLine lineToRemoveFrom = ((LineComboBoxItem)workspace.getLinesComboBox().getSelectionModel().getSelectedItem()).getLine();
                if(((DraggableStation)node).getLines().contains(lineToRemoveFrom))
                    transaction = new RemoveStationFromLine(lineToRemoveFrom, (DraggableStation)node);
                else {
                    dataManager.setState(NEUTRAL);
                    app.getGUI().getPrimaryScene().setCursor(Cursor.DEFAULT);
                }
                //lineToRemoveFrom.removeStation((DraggableStation)node);
                return;
            } else {
                app.getGUI().getPrimaryScene().setCursor(Cursor.DEFAULT);
                dataManager.setState(NEUTRAL);
            }
        }
        if(node instanceof DraggableText && endPointText == null && !((DraggableText)node).isLabel() && !((DraggableText)node).isStationLabel()) {
            endPointText = (DraggableText)node;
            selectedLine = endPointText.getEndPoint().getLine();
            transaction = new DragStation(endPointText.getEndPoint(), (int)endPointText.getEndPoint().getCenterX(), (int)endPointText.getEndPoint().getCenterY());
            endPointText.getEndPoint().setEffect(highlightedEffect);
            dataManager.getNodes().remove((DraggableText)node);
            dataManager.loadTextColor((DraggableText)node);
            dataManager.loadTextStyle((DraggableText)node);
            dataManager.getNodes().add(endPointText.getEndPoint());
            dataManager.setState(DRAGGING_LINE_END);
        }
        if(node instanceof DraggableStation && !((DraggableStation)node).getIsEndPoint()) {
            workspace.getStationsComboBox().getSelectionModel().select(((DraggableStation)node).getItem());
        }
        if((node instanceof DraggableStation && !((DraggableStation)node).getIsEndPoint()) && !dataManager.isInState(DRAGGING_STATION) && endPointText == null) {
            selectedStation = (DraggableStation)node;
            node.setEffect(highlightedEffect);
            //selectedStation.setDrag();
            dataManager.setSelectedNode(node);
            transaction = new DragStation(selectedStation, x, y);
            dataManager.setState(DRAGGING_STATION);
        }
        if((node instanceof DraggableStation && !((DraggableStation)node).getIsEndPoint()) && dataManager.isInState(DRAGGING_LINE_END)) {
            endPointText.getEndPoint().getLine().removeEndPoint(endPointText.getEndPoint(), endPointText.getEndPoint().getName());
            endPointText = null;
            selectedStation = (DraggableStation)node;
            node.setEffect(highlightedEffect);
            //selectedStation.setDrag();
            dataManager.setSelectedNode(node);
            transaction = new DragStation(selectedStation, x, y);
            dataManager.setState(DRAGGING_STATION);
            return;
        }
        if(!(node instanceof DraggableStation) && dataManager.isInState(DRAGGING_STATION) && selectedStation != null) {
            //selectedStation.removeDrag();
            selectedStation.setEffect(null);
            dataManager.setState(NEUTRAL);
            return;
        }
        if(node instanceof DraggableStation && dataManager.isInState(DRAGGING_STATION) && !selectedStation.equals((DraggableStation)node)) {
            //selectedStation.removeDrag();
            selectedStation.setEffect(null);
            selectedStation = (DraggableStation)node;
            node.setEffect(highlightedEffect);
            //selectedStation.setDrag();
            dataManager.setSelectedNode(node);
            dataManager.loadTextColor(((DraggableStation)node).getLine().getNameStart());
            dataManager.loadTextStyle(((DraggableStation)node).getLine().getNameStart());
            transaction = new DragStation(selectedStation, x, y);
            return;
        }
        if(node instanceof DraggableText && dataManager.isInState(DRAGGING_LINE_END) && selectedStation != null) {
            //selectedStation.removeDrag();
            selectedStation = null;
        }
        if(dataManager.isInState(DRAGGING_LINE_END)) {
            if((!(node instanceof DraggableStation && ((DraggableStation)node).getIsEndPoint()) && !(node instanceof DraggableText))&& (endPointText != null) ) {
                if(endPointText.getEndPoint().getLine().getStations().indexOf(endPointText.getEndPoint()) == 0)
                    endPointText.getEndPoint().getLine().removeEndPoint(endPointText.getEndPoint().getLine().getMoveTo(), endPointText.getEndPoint(), endPointText.getEndPoint().getName());
                else
                    endPointText.getEndPoint().getLine().removeEndPoint(endPointText.getEndPoint().getLine().getLineTo(), endPointText.getEndPoint(), endPointText.getEndPoint().getName());
                endPointText = null;
                dataManager.setState(NEUTRAL);
            }
            if(node instanceof DraggableText && endPointText != null && !((DraggableText)node).isLabel() && !((DraggableText)node).isStationLabel()) {
                if(!((DraggableText)node).equals(endPointText)) {
                    if(endPointText.getEndPoint().getLine().getStations().indexOf(endPointText.getEndPoint()) == 0)
                        ((DraggableText)node).getEndPoint().getLine().removeEndPoint(endPointText.getEndPoint().getLine().getMoveTo(), endPointText.getEndPoint(), endPointText.getEndPoint().getName());
                    else
                        ((DraggableText)node).getEndPoint().getLine().removeEndPoint(endPointText.getEndPoint().getLine().getLineTo(), endPointText.getEndPoint(), endPointText.getEndPoint().getName());
                        //endPointText.getEndPoint().getLine().removeEndPoint(endPointText.getEndPoint(), endPointText.getEndPoint().getName());
                        //dataManager.getNodes().add(endPointText);
                    endPointText = (DraggableText)node;
                    dataManager.getNodes().remove((DraggableText)node);
                    dataManager.getNodes().add(endPointText.getEndPoint());
                    endPointText.getEndPoint().setEffect(highlightedEffect);
                    selectedLine = endPointText.getEndPoint().getLine();
                    dataManager.getNodes().remove((DraggableText)node);
                }
            }
        }
        if(node != null)
            app.getGUI().updateToolbarControls(false);
        if(node == null)
            workspace.getRemoveElement().setDisable(true);
    }
    
    public void processCanvasMouseDragged(int x, int y) {
        M3Data dataManager = (M3Data) app.getDataComponent();
        if (dataManager.isInState(DRAGGING_IMAGE)) {
            Draggable selectedDraggableShape = (Draggable) dataManager.getSelectedNode();
            selectedDraggableShape.drag(x, y);
            app.getGUI().updateToolbarControls(false);
        } else if(dataManager.isInState(DRAGGING_TEXT)) {
            Draggable selectedDraggableShape = (Draggable) dataManager.getSelectedNode();
            selectedDraggableShape.drag(x, y);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(DRAGGING_STATION)) {
            Draggable selectedDraggableShape = (Draggable) dataManager.getSelectedNode();
            selectedDraggableShape.drag(x, y);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(DRAGGING_LINE_END)) {
            Draggable selectedDraggableShape = (Draggable) dataManager.getSelectedNode();
            selectedDraggableShape.drag(x, y);
            app.getGUI().updateToolbarControls(false);
        }
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
    }
    
    public void processCanvasMouseReleased(int x, int y) {
        M3Data dataManager = (M3Data) app.getDataComponent();
        M3Workspace workspace = (M3Workspace)app.getWorkspaceComponent();
        jTPS jTPS = dataManager.getJTPS();
        if (dataManager.isInState(M3State.DRAGGING_IMAGE)) {
            node.setEffect(null);
            ((DragImage)transaction).setEndX(x);
            ((DragImage)transaction).setEndY(y);
            jTPS.addTransaction(transaction);
            workspace.getUndoButton().setDisable(false);
            if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
                workspace.getRedoButton().setDisable(true);
            dataManager.setState(NEUTRAL);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(DRAGGING_TEXT)) {
            //node.setEffect(null);
            ((DragText)transaction).setEndX(x);
            ((DragText)transaction).setEndY(y);
            jTPS.addTransaction(transaction);
            workspace.getUndoButton().setDisable(false);
            if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
                workspace.getRedoButton().setDisable(true);
            dataManager.setState(SELECTED_TEXT);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(DRAGGING_STATION)) {
            ((DragStation)transaction).setEndX(x);
            ((DragStation)transaction).setEndY(y);
            selectedStation.setEffect(null);
            jTPS.addTransaction(transaction);
            workspace.getUndoButton().setDisable(false);
            if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
                workspace.getRedoButton().setDisable(true);
            dataManager.setState(NEUTRAL);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(DRAGGING_LINE_END)) {
            ((DragStation)transaction).setEndX(x);
            ((DragStation)transaction).setEndY(y);
            if(selectedStation != null)
                selectedStation.setEffect(null);
            jTPS.addTransaction(transaction);
            workspace.getUndoButton().setDisable(false);
            if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
                workspace.getRedoButton().setDisable(true);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(ADD_STATION_MODE)) {
            jTPS.addTransaction(transaction);
            workspace.getUndoButton().setDisable(false);
            if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
                workspace.getRedoButton().setDisable(true);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(REMOVE_STATION_MODE)) {
            jTPS.addTransaction(transaction);
            workspace.getUndoButton().setDisable(false);
            if (jTPS.getTransactionsCount() == jTPS.getTransactionsMax());
                workspace.getRedoButton().setDisable(true);
            app.getGUI().updateToolbarControls(false);
        }
    }
}
