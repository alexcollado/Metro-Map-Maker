/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableStation;
import metro_map_maker.data.M3Data.StationComboBoxItem;
import metro_map_maker.gui.M3Workspace;

/**
 *
 * @author alexc
 */
public class AddStation implements jTPS_Transaction {
    private ObservableList<Node> nodes;
    private DraggableStation station;
    StationComboBoxItem item;
    private M3Workspace workspace;
    
    public AddStation(DraggableStation station, ObservableList<Node> nodes, StationComboBoxItem item, M3Workspace workspace) {
        this.station = station;
        this.nodes = nodes;
        this.item = item;
        this.workspace = workspace;
    }
    
    @Override
    public void doTransaction() {
        nodes.add(station);
        station.addLabel();
        workspace.getStationsComboBox().getItems().add(item);
        workspace.getStationsComboBox().setValue(item);
    }

    @Override
    public void undoTransaction() {
        nodes.remove(station);
        station.removeLabel();
        workspace.getStationsComboBox().getItems().remove(item);
    }
}
