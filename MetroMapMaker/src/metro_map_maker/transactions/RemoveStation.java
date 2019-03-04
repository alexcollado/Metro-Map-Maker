/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.LineTo;
import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableLine;
import metro_map_maker.data.DraggableStation;
import metro_map_maker.data.M3Data.StationComboBoxItem;
import metro_map_maker.data.M3State;
import metro_map_maker.gui.M3Workspace;

/**
 *
 * @author alexc
 */
public class RemoveStation implements jTPS_Transaction {
    private ObservableList<Node> nodes;
    private DraggableStation station;
    StationComboBoxItem item;
    private M3Workspace workspace;
    
    public RemoveStation(DraggableStation station, ObservableList<Node> nodes, StationComboBoxItem item, M3Workspace workspace) {
        this.station = station;
        this.nodes = nodes;
        this.item = item;
        this.workspace = workspace;
    }

    @Override
    public void doTransaction() {
        LineTo lineTo = new LineTo(station.getCenterX(), station.getCenterY());
        station.centerXProperty().unbindBidirectional(lineTo.xProperty());
        station.centerYProperty().unbindBidirectional(lineTo.yProperty());
        
        for(Node line:nodes) {
            if(line instanceof DraggableLine) {
                if(((DraggableLine)line).getStations().indexOf(station) != -1) {
                    ((DraggableLine)line).getElements().remove(((DraggableLine)line).getStations().indexOf(station));
                    ((DraggableLine)line).getStations().remove(station);
                }
            }
        }
        nodes.remove(station);
        station.removeLabel();
        workspace.getStationsComboBox().getItems().remove(item);
        if(!workspace.getStationsComboBox().getItems().isEmpty())
            workspace.getStationsComboBox().getSelectionModel().selectNext();
    }

    @Override
    public void undoTransaction() {
        nodes.add(station);
        if(station.getLines().isEmpty())
            station.addLabel();
        for(DraggableLine line:station.getLines()) {
            line.reAddStation(station);
        }
        workspace.getStationsComboBox().getItems().add(item);
        workspace.getStationsComboBox().setValue(item);
    }
    
}
