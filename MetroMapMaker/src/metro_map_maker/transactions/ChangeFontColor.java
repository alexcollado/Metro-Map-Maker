/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableText;

/**
 *
 * @author alexc
 */
public class ChangeFontColor implements jTPS_Transaction {
    DraggableText label;
    Color currentColor;
    Color prevColor;
    
    public ChangeFontColor(DraggableText label, Color currentColor, Color prevColor) {
        this.label = label;
        this.currentColor = currentColor;
        this.prevColor = prevColor;
    }

    @Override
    public void doTransaction() {
        label.setColor(currentColor);
        label.setFill(currentColor);
    }

    @Override
    public void undoTransaction() {
        label.setColor(prevColor);
        label.setFill(prevColor);
    }
    
}
