/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableImage;
import metro_map_maker.gui.M3Workspace;

/**
 *
 * @author alexc
 */
public class SetImageBackground implements jTPS_Transaction {
    DraggableImage currentImage;
    DraggableImage prevImage;
    ObservableList<Node> nodes;
    
    public SetImageBackground(DraggableImage currentImage, DraggableImage prevImage, ObservableList<Node> nodes) {
        this.currentImage = currentImage;
        this.prevImage = prevImage;
        this.nodes = nodes;
    }

    @Override
    public void doTransaction() {
        if(nodes.isEmpty() || (nodes.get(0) instanceof DraggableImage && !((DraggableImage)nodes.get(0)).isBackground()))
            nodes.add(0, currentImage);
        else {
            nodes.remove(0);
            nodes.add(0, currentImage);
        }
    }

    @Override
    public void undoTransaction() {
        nodes.remove(currentImage);
        if(prevImage != null)
            nodes.add(0, prevImage);
    }
    
}
