/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableLine;
import metro_map_maker.gui.M3Workspace;

/**
 *
 * @author alexc
 */
public class SetLineThickness implements jTPS_Transaction {
    DraggableLine line;
    double currentLineThickness;
    double prevLineThickness;
    M3Workspace workspace;
    
    public SetLineThickness(DraggableLine line, double currentLineThickness, double prevLineThickness, M3Workspace workspace) {
        this.line = line;
        this.currentLineThickness = currentLineThickness;
        this.prevLineThickness = prevLineThickness;
        this.workspace = workspace;
    }

    @Override
    public void doTransaction() {
        line.setStrokeWidth(currentLineThickness);
    }

    @Override
    public void undoTransaction() {
        line.setStrokeWidth(prevLineThickness);
    }
    
}

