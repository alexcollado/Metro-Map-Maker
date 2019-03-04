/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableImage;
import metro_map_maker.gui.M3Workspace;

/**
 *
 * @author alexc
 */
public class AddImage implements jTPS_Transaction {
    DraggableImage image;
    ObservableList<Node> nodes;
    M3Workspace workspace;
    
    public AddImage(DraggableImage image, ObservableList<Node> nodes, M3Workspace workspace) {
        this.image = image;
        this.nodes = nodes;
        this.workspace = workspace;
    }

    @Override
    public void doTransaction() {
        nodes.add(image);
    }

    @Override
    public void undoTransaction() {
        nodes.remove(image);
    }
    
}
