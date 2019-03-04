/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;
import metro_map_maker.gui.M3Workspace;

/**
 *
 * @author alexc
 */
public class EditBackgroundColor implements jTPS_Transaction {
    Color currentColor;
    Color prevColor;
    M3Workspace workspace;
    
    public EditBackgroundColor(Color currentColor, Color prevColor, M3Workspace workspace) {
        this.currentColor = currentColor;
        this.prevColor = prevColor;
        this.workspace = workspace;
    }

    @Override
    public void doTransaction() {
        workspace.getCanvas().setBackground(new Background(new BackgroundFill(currentColor, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @Override
    public void undoTransaction() {
        workspace.getCanvas().setBackground(new Background(new BackgroundFill(prevColor, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    
}
