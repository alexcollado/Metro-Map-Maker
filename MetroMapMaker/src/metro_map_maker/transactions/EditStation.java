/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableStation;
import metro_map_maker.data.M3Data.StationComboBoxItem;
import metro_map_maker.gui.M3Workspace;

/**
 *
 * @author alexc
 */
public class EditStation implements jTPS_Transaction {
    StationComboBoxItem item;
    DraggableStation station;
    Color currentColor;
    Color prevColor;
    M3Workspace workspace;
    
    public EditStation(StationComboBoxItem item, DraggableStation station, Color currentColor, Color prevColor, M3Workspace workspace) {
        this.item = item;
        this.station = station;
        this.currentColor = currentColor;
        this.prevColor = prevColor;
        this.workspace = workspace;
    }

    @Override
    public void doTransaction() {
        station.setColor(currentColor);
        station.setFill(currentColor);
        workspace.getStationsComboBox().getSelectionModel().clearSelection();
        workspace.getStationsComboBox().setValue(item);
        workspace.getStationsComboBox().getSelectionModel().select(item);
    }

    @Override
    public void undoTransaction() {
        station.setColor(prevColor);
        station.setFill(prevColor);
        workspace.getStationsComboBox().getSelectionModel().clearSelection();
        workspace.getStationsComboBox().setValue(item);
        workspace.getStationsComboBox().getSelectionModel().select(item);
    }
    
}
