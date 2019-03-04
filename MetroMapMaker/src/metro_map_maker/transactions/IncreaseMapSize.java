/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import jtps.jTPS_Transaction;
import metro_map_maker.gui.M3Workspace;

/**
 *
 * @author alexc
 */
public class IncreaseMapSize implements jTPS_Transaction {
    M3Workspace workspace;
    
    public IncreaseMapSize(M3Workspace workspace) {
        this.workspace = workspace;
    }

    @Override
    public void doTransaction() {
        workspace.getCanvas().setMinSize(workspace.getCanvas().getWidth() * 1.1, workspace.getCanvas().getHeight() * 1.1);
    }

    @Override
    public void undoTransaction() {
        workspace.getCanvas().setMinSize(workspace.getCanvas().getWidth() * 0.9, workspace.getCanvas().getHeight() * 0.9);
    }
    
}
