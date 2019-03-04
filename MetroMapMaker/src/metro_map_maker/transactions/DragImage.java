/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import jtps.jTPS_Transaction;
import metro_map_maker.data.Draggable;

/**
 *
 * @author alexc
 */
public class DragImage implements jTPS_Transaction {
    private Draggable draggableImage;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    
    public DragImage(Draggable node, int startX, int startY) {
        draggableImage = node;
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
        draggableImage.drag(endX, endY);
    }

    @Override
    public void undoTransaction() {
        draggableImage.drag(startX, startY);
    }
    
}
