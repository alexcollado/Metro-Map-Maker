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
public class DragStation implements jTPS_Transaction {
    private DraggableStation station;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    
    public DragStation(DraggableStation station, int startX, int startY) {
        this.station = station;
        this.startX = startX;
        this.startY = startY;
    }
    
    public void setEndX(int endX) {
        this.endX = endX;
    }
    public void setEndY(int endY) {
        this.endY = endY;
    }
    
    @Override
    public void doTransaction() {
        station.drag(endX, endY);
    }

    @Override
    public void undoTransaction() {
        station.drag(startX, startY);
    }
    
}
