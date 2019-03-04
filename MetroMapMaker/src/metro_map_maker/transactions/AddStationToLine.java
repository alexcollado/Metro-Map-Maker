/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableLine;
import metro_map_maker.data.DraggableStation;
import metro_map_maker.data.M3Data;
import metro_map_maker.data.M3Data.StationComboBoxItem;
import metro_map_maker.gui.M3Workspace;

/**
 *
 * @author alexc
 */
public class AddStationToLine implements jTPS_Transaction {
    DraggableLine line;
    DraggableStation station;
    M3Workspace workspace;
    M3Data dataManager;
    
    public AddStationToLine(DraggableLine line, DraggableStation station, M3Workspace workspace, M3Data dataManager) {
        this.line = line;
        this.station = station;
        this.workspace = workspace;
        this.dataManager = dataManager;
    }

    @Override
    public void doTransaction() {
        line.addStation(station);
        //StationComboBoxItem item = dataManager.new StationComboBoxItem(station.getLabel().getText(), station);
        if(workspace.getStationStart().getItems().contains(station.getItem()))
            return;
        workspace.getStationStart().getItems().add(station.getItem());
        workspace.getStationEnd().getItems().add(station.getItem());
    }

    @Override
    public void undoTransaction() {
        line.removeStation(station);
        workspace.getStationStart().getItems().remove(station.getItem());
        workspace.getStationEnd().getItems().remove(station.getItem());
    }
    
}
