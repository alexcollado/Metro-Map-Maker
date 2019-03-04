/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableStation;
import metro_map_maker.gui.M3Workspace;

/**
 *
 * @author alexc
 */
public class SetStationRadius implements jTPS_Transaction {
    DraggableStation station;
    double currentStationRadius;
    double prevStationRadius;
    M3Workspace workspace;
    
    public SetStationRadius(DraggableStation station, double currentStationRadius, double prevStationRadius, M3Workspace workspace) {
        this.station = station;
        this.currentStationRadius = currentStationRadius;
        this.prevStationRadius = prevStationRadius;
        this.workspace = workspace;
    }

    @Override
    public void doTransaction() {
        station.setRadius(currentStationRadius);
    }

    @Override
    public void undoTransaction() {
        station.setRadius(prevStationRadius);
    }
    
}
