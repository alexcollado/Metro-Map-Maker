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
public class SnapToGrid implements jTPS_Transaction {
    DraggableStation station;
    
    public SnapToGrid(DraggableStation station) {
        this.station = station;
    }

    @Override
    public void doTransaction() {
        double x = station.getCenterX();
        double y = station.getCenterY();
        if(y % 25 > 12) {
            y -= y % 25;
        } else{
            y += y % 25;
        }
        if(x % 25 <= 12) {
            x -= x % 25;
        } else {
            x += x % 25;
        }
        station.setCenterX(x);
        station.setCenterY(y);
    }

    @Override
    public void undoTransaction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
