/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.data;

import djf.AppTemplate;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import metro_map_maker.data.M3Data.StationComboBoxItem;

/**
 *
 * @author alexc
 */
public class DraggableStation extends Circle implements Draggable, Comparable<DraggableStation> {
    DraggableText label;
    transient M3Data dataComponent;
    StationComboBoxItem item;
    DraggableLine line;
    Color color;
    ArrayList<DraggableLine> lines;
    String position;
    double rotate;
    boolean isEndPoint;
    double startCenterX;
    double startCenterY;
    ArrayList<DraggableStation> route = new ArrayList();
    
    public DraggableStation(DraggableText label, M3Data dataComponent) {
        super(375, 250, 10);
        isEndPoint = false;
        lines = new ArrayList();
        color = Color.valueOf("#ffffff");
        this.setFill(color);
        this.setStrokeType(StrokeType.OUTSIDE);
        this.setStroke(Color.valueOf("#000000"));
        this.label = label;
        position = "topright";
        rotate = label.getRotate();
        this.dataComponent = dataComponent;
        //addLabel();
    }

    public DraggableStation(int x, int y, int radius, DraggableLine line, DraggableText label) {
        super(x, y, radius);
        this.line = line;
        this.label = label;
        isEndPoint = true;
        position = "";
        rotate = 0;
        color = Color.valueOf("#ffffff");
        this.setFill(color);
        this.setStrokeType(StrokeType.OUTSIDE);
        this.setStroke(Color.valueOf("#000000"));
    }
    
    public DraggableStation(int x, int y, int radius, DraggableText label, String position, double rotate, Color color, M3Data dataComponent) {
        super(x, y, radius);
        this.label = label;
        this.dataComponent = dataComponent;
        this.position = position;
        this.rotate = rotate;
        this.label.setRotate(rotate);
        this.color = color;
        this.setFill(color);
        lines = new ArrayList();
        //this.setFill(Color.valueOf("#ffffff"));
        this.setStrokeType(StrokeType.OUTSIDE);
        this.setStroke(Color.valueOf("#000000"));
    }
    
    public DraggableStation() {
        super();
    }
    
    public void addLabel() {
        if(position.equals("topright")) {
            this.label.xProperty().bind(this.centerXProperty().add(15));
            this.label.yProperty().bind(this.centerYProperty().subtract(15));
        } else if(position.equals("bottomright")) {
            this.label.xProperty().bind(this.centerXProperty().add(15));
            this.label.yProperty().bind(this.centerYProperty().add(15));
        } else if(position.equals("topleft")) {
            this.label.xProperty().bind(this.centerXProperty().subtract(this.label.getLayoutBounds().getWidth() + 15));
            this.label.yProperty().bind(this.centerYProperty().subtract(15));
        } else if(position.equals("bottomleft")) {
            this.label.xProperty().bind(this.centerXProperty().subtract(this.label.getLayoutBounds().getWidth() + 15));
            this.label.yProperty().bind(this.centerYProperty().add(15));
        }
        dataComponent.getNodes().add(label);
    }
    
    public void removeLabel() {
        dataComponent.getNodes().remove(label);
    }
    
//    public EventHandler<MouseEvent> dragHandler = new EventHandler<MouseEvent>() {
//        @Override
//        public void handle(MouseEvent event) {
//            setDrag();
//        }
//    };

//    public void setDrag() {
//        this.setOnMouseDragged(e -> { 
//            centerXProperty().set(e.getX());
//            centerYProperty().set(e.getY()); 
//        });
//    }
//    public void removeDrag() {
//        this.setEffect(null);
//        this.removeEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
//    }
    
    public StationComboBoxItem getItem() {
        return item;
    }
    public void setItem(StationComboBoxItem item) {
        this.item = item;
    }
    public void setLabel(DraggableText label) {
        this.label = label;
    }
    public DraggableText getLabel() {
        return label;
    }
    public ArrayList<DraggableLine> getLines() {
        return lines;
    }
    public void setLine(DraggableLine line) {
        this.line = line;
    }
    public DraggableLine getLine() {
        return line;
    }
    public DraggableText getName() {
        return label;
    }
    public boolean getIsEndPoint() {
        return isEndPoint;
    }
    public void setIsEndPoint(boolean isEndpoint) {
        this.isEndPoint = isEndpoint;
    }
    public void setLines(ArrayList<DraggableLine> lines) {
        this.lines = lines;
    }
    public String getPosVal() {
        return position;
    }
    public void setPosVal(String position) {
        this.position = position;
    }
    public double getRotateVal() {
        return rotate;
    }
    public void setRotateVal(double rotate) {
        this.rotate = rotate;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Node copy(Node node, AppTemplate app) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public M3State getStartingState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void start(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void drag(int x, int y) {
        double diffX = x - getX();
	double diffY = y - getY();
	double newX = getCenterX() + diffX;
	double newY = getCenterY() + diffY;
	setCenterX(newX);
	setCenterY(newY);
	setCenterX(x);
	setCenterY(y);
    }

    @Override
    public void size(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getX() {
        return getCenterX();
    }

    @Override
    public double getY() {
        return getCenterY();
    }

    @Override
    public double getWidth() {
        return getRadius()*2;
    }

    @Override
    public double getHeight() {
        return getRadius()*2;
    }

    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getNodeType() {
        return STATION;
    }

    @Override
    public int compareTo(DraggableStation o) {
        return o.compareTo(o);
    }
    
}
