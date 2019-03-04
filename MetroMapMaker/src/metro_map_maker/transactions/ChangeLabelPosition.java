/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableStation;

/**
 *
 * @author alexc
 */
public class ChangeLabelPosition implements jTPS_Transaction {
    DraggableStation station;
    
    public ChangeLabelPosition(DraggableStation station) {
        this.station = station;
    }

    @Override
    public void doTransaction() {
        if(station.getPosVal().equals("topright")) {
            station.getLabel().xProperty().bind(station.centerXProperty().add(15));
            station.getLabel().yProperty().bind(station.centerYProperty().add(15));
            station.setPosVal("bottomright");
        } else if(station.getPosVal().equals("bottomright")) {
            station.getLabel().xProperty().bind(station.centerXProperty().subtract(station.getLabel().getLayoutBounds().getWidth() + 15));
            station.getLabel().yProperty().bind(station.centerYProperty().add(15));
            station.setPosVal("bottomleft");
        } else if(station.getPosVal().equals("topleft")) {
            station.getLabel().xProperty().bind(station.centerXProperty().add(15));
            station.getLabel().yProperty().bind(station.centerYProperty().subtract(15));
            station.setPosVal("topright");
        } else if(station.getPosVal().equals("bottomleft")) {
            station.getLabel().xProperty().bind(station.centerXProperty().subtract(station.getLabel().getLayoutBounds().getWidth() + 15));
            station.getLabel().yProperty().bind(station.centerYProperty().subtract(15));
            station.setPosVal("topleft");
        }
    }

    @Override
    public void undoTransaction() {
        if(station.getPosVal().equals("topright")) {
            station.getLabel().xProperty().bind(station.centerXProperty().subtract(station.getLabel().getLayoutBounds().getWidth() + 15));
            station.getLabel().yProperty().bind(station.centerYProperty().subtract(15));
            station.setPosVal("topleft");
        } else if(station.getPosVal().equals("bottomright")) {
            station.getLabel().xProperty().bind(station.centerXProperty().add(15));
            station.getLabel().yProperty().bind(station.centerYProperty().subtract(15));
            station.setPosVal("topright");
        } else if(station.getPosVal().equals("topleft")) {
            station.getLabel().xProperty().bind(station.centerXProperty().subtract(station.getLabel().getLayoutBounds().getWidth() + 15));
            station.getLabel().yProperty().bind(station.centerYProperty().add(15));
            station.setPosVal("bottomleft");
        } else if(station.getPosVal().equals("bottomleft")) {
            station.getLabel().xProperty().bind(station.centerXProperty().add(15));
            station.getLabel().yProperty().bind(station.centerYProperty().add(15));
            station.setPosVal("bottomright");
        }
    }
    
}
