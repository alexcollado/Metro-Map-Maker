/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import javafx.scene.text.Font;
import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableText;

/**
 *
 * @author alexc
 */
public class ChangeFontStyle implements jTPS_Transaction {
    DraggableText label;
    Font currentFontStyle;
    Font prevFontStyle;
    
    public ChangeFontStyle(DraggableText label, Font currentFontStyle, Font prevFontStyle) {
        this.label = label;
        this.currentFontStyle = currentFontStyle;
        this.prevFontStyle = prevFontStyle;
    }

    @Override
    public void doTransaction() {
        label.setFont(currentFontStyle);
    }

    @Override
    public void undoTransaction() {
        label.setFont(prevFontStyle);
    }
    
}
