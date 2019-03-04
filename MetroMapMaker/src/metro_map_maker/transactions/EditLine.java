/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableLine;
import metro_map_maker.data.DraggableText;
import metro_map_maker.data.M3Data.LineComboBoxItem;
import metro_map_maker.gui.M3Workspace;

/**
 *
 * @author alexc
 */
public class EditLine implements jTPS_Transaction {
    LineComboBoxItem item;
    DraggableLine line;
    String text;
    Color color;
    boolean isCircular;
    LineComboBoxItem prevItem;
    String prevText;
    Color prevColor;
    boolean prevCircular;
    M3Workspace workspace;
    
    public EditLine(LineComboBoxItem item, DraggableLine line, String text, Color color, boolean isCircular, LineComboBoxItem prevItem, String prevText, Color prevColor, boolean prevCircular, M3Workspace workspace) {
        this.item = item;
        this.line = line;
        this.text = text;
        this.color = color;
        this.isCircular = isCircular;
        this.prevItem = prevItem;
        this.prevText = prevText;
        this.prevColor = prevColor;
        this.prevCircular = prevCircular;
        this.workspace = workspace;
    }

    @Override
    public void doTransaction() {
        //DraggableText nameStart = new DraggableText(text);
        //DraggableText nameEnd = new DraggableText(text);
        line.getNameStart().setText(text);
        line.getNameEnd().setText(text);
        line.setColor(color);
        line.fixLabelPosition();
        line.setCircular(isCircular);
        item.setName(line.getName());
        item.setLine(line);
        workspace.getLinesComboBox().getSelectionModel().clearSelection();
        workspace.getLinesComboBox().setValue(item);
        workspace.getLinesComboBox().getSelectionModel().select(item);
    }

    @Override
    public void undoTransaction() {
        line.getNameStart().setText(prevText);
        line.getNameEnd().setText(prevText);
        line.setColor(prevColor);
        line.fixLabelPosition();
        line.setCircular(prevCircular);
        prevItem.setName(prevText);
        prevItem.setLine(line);
        workspace.getLinesComboBox().getSelectionModel().clearSelection();
        workspace.getLinesComboBox().setValue(prevItem);
        workspace.getLinesComboBox().getSelectionModel().select(prevItem);
    }
    
}
