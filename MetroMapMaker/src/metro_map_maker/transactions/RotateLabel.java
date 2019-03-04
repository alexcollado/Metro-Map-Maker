/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableStation;

/**
 *
 * @author alexc
 */
public class RotateLabel implements jTPS_Transaction {
    DraggableStation station;
    
    public RotateLabel(DraggableStation station) {
        this.station = station;
    }

    @Override
    public void doTransaction() {
        if(station.getLabel().getRotate() == 0) {
            station.getLabel().setRotate(90);
            station.setRotateVal(90);
        }
        else if(station.getLabel().getRotate() == 90) {
            station.getLabel().setRotate(0);
            station.setRotateVal(0);
        }
    }

    @Override
    public void undoTransaction() {
        if(station.getLabel().getRotate() == 0) {
            station.getLabel().setRotate(90);
            station.setRotateVal(90);
        }
        else if(station.getLabel().getRotate() == 90) {
            station.getLabel().setRotate(0);
            station.setRotateVal(0);
        }
    }
    
}
