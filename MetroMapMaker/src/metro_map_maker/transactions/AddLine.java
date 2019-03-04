/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableLine;
import metro_map_maker.data.M3Data.LineComboBoxItem;
import metro_map_maker.gui.M3Workspace;

/**
 *
 * @author alexc
 */
public class AddLine implements jTPS_Transaction {
    private ObservableList<Node> nodes;
    private DraggableLine line;
    LineComboBoxItem item;
    private M3Workspace workspace;
    
    public AddLine(DraggableLine line, ObservableList<Node> nodes, LineComboBoxItem item, M3Workspace workspace) {
        this.line = line;
        this.nodes = nodes;
        this.item = item;
        this.workspace = workspace;
    }
    
    @Override
    public void doTransaction() {
        nodes.add(line);
        line.insertLabelsAndPoints();
        workspace.getLinesComboBox().getItems().add(item);
        workspace.getLinesComboBox().setValue(item);
    }

    @Override
    public void undoTransaction() {
        nodes.remove(line);
        line.removeLabelsAndPoints();
        workspace.getLinesComboBox().getItems().remove(item);
    }
    
}
