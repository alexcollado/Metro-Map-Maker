/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableLine;
import metro_map_maker.data.DraggableStation;

/**
 *
 * @author alexc
 */
public class RemoveStationFromLine implements jTPS_Transaction {
    DraggableLine line;
    DraggableStation station;
    
    public RemoveStationFromLine(DraggableLine line, DraggableStation station) {
        this.line = line;
        this.station = station;
    }

    @Override
    public void doTransaction() {
        line.removeStation(station);
    }

    @Override
    public void undoTransaction() {
        line.addStation(station);
    }
    
}
