/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.data;

import djf.AppTemplate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import metro_map_maker.data.M3Data.LineComboBoxItem;

/**
 *
 * @author alexc
 */
public class DraggableLine extends Path implements Draggable{
    MoveTo moveTo;
    LineTo lineTo;
    DraggableStation start;
    DraggableStation end;
    DraggableText nameStart;
    DraggableText nameEnd;
    LineComboBoxItem item;
    M3Data dataComponent;
    Color color;
    boolean circular;
    ArrayList<DraggableStation> stations;
    ArrayList<PathElement> pathElements;
    
    public DraggableLine(DraggableText nameStart, DraggableText nameEnd, Color color, M3Data dataComponent) {
        moveTo = new MoveTo(250, 250);
        lineTo = new LineTo(500, 250);
        stations = new ArrayList();
        pathElements = new ArrayList();
        pathElements.add(moveTo);
        pathElements.add(lineTo);
        start = new DraggableStation(250, 250, 10, this, nameStart);
        start.setFill(Color.valueOf("#ffffff"));
        start.setStrokeType(StrokeType.OUTSIDE);
        start.setStroke(Color.valueOf("#000000"));
        stations.add(start);
        end = new DraggableStation(500, 250, 10, this, nameEnd);
        end.setFill(Color.valueOf("#ffffff"));
        end.setStrokeType(StrokeType.OUTSIDE);
        end.setStroke(Color.valueOf("#000000"));
        stations.add(end);
        this.nameStart = nameStart;
        this.nameEnd = nameEnd;
        nameStart.setEndPoint(start);
        nameEnd.setEndPoint(end);
        this.dataComponent = dataComponent;
        this.color = color;
        this.setStroke(color);
        this.setStrokeWidth(8);
        this.getElements().addAll(moveTo, lineTo);
    }

    public DraggableLine() {
        super();
    }
    
    public void addStation(DraggableStation station) {
        dataComponent.getNodes().remove(station);
        dataComponent.getNodes().add(station);
        dataComponent.getNodes().remove(station.label);
        dataComponent.getNodes().add(station.label);
        if(stations.contains(station)) {
            dataComponent.setState(M3State.NEUTRAL);
            return;
        }
        if(stations.size() == 2) {
            stations.add(1, station);
            station.getLines().add(this);
            LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
            station.centerXProperty().bindBidirectional(line.xProperty());
            station.centerYProperty().bindBidirectional(line.yProperty());
            this.getElements().add(1, line);
            pathElements.add(1, line);
        } else {
            Point2D firstStation = new Point2D(stations.get(1).getCenterX(), stations.get(1).getCenterY());
            Point2D stationToAdd = new Point2D(station.getCenterX(), station.getCenterY());
            DraggableStation closestStation = stations.get(1);
            Double distance = stationToAdd.distance(firstStation);

            for (DraggableStation s : stations) {
                if(s.isEndPoint)
                    continue;
                Point2D p1 = new Point2D(s.getCenterX(), s.getCenterY());
                Point2D p2 = new Point2D(station.getCenterX(), station.getCenterY());
                if(p1.distance(p2) < distance) {
                    distance = p1.distance(p2);
                    closestStation = s;
                }
            }
            
            if(closestStation.equals(stations.get(1)) && stations.size() == 3) {
                Double distance1;
                Double distance2;
                Point2D p1 = new Point2D(station.getCenterX(), station.getCenterY());
                Point2D p2 = new Point2D(stations.get(0).getCenterX(), stations.get(0).getCenterY());
                Point2D p3 = new Point2D(stations.get(stations.size()-1).getCenterX(), stations.get(stations.size()-1).getCenterY());
                distance1 = p1.distance(p2);
                distance2 = p1.distance(p3);
                if(distance1 < distance2) {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(1, line);
                    pathElements.add(1, line);
                    stations.add(1, station);
                    station.getLines().add(this);
                } else {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(2, line);
                    pathElements.add(2, line);
                    stations.add(2, station);
                    station.getLines().add(this);
                }
            } else if(closestStation.equals(stations.get(1))) {
                Double distance1;
                Double distance2;
                Point2D p1 = new Point2D(station.getCenterX(), station.getCenterY());
                Point2D p2 = new Point2D(stations.get(0).getCenterX(), stations.get(0).getCenterY());
                Point2D p3 = new Point2D(stations.get(2).getCenterX(), stations.get(2).getCenterY());
                distance1 = p1.distance(p2);
                distance2 = p1.distance(p3);
                if(distance1 < distance2) {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(1, line);
                    pathElements.add(1, line);
                    stations.add(1, station);
                    station.getLines().add(this);
                } else {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(2, line);
                    pathElements.add(2, line);
                    stations.add(2, station);
                    station.getLines().add(this);
                }
            } else if(closestStation.equals(stations.get(stations.size()-2)) && stations.size() != 1) {
                Double distance1;
                Double distance2;
                Point2D p1 = new Point2D(station.getCenterX(), station.getCenterY());
                Point2D p2 = new Point2D(stations.get(stations.size()-1).getCenterX(), stations.get(stations.size()-1).getCenterY());
                Point2D p3 = new Point2D(stations.get(stations.size() - 3).getCenterX(), stations.get(stations.size() - 3).getCenterY());
                distance1 = p1.distance(p3);
                distance2 = p1.distance(p2);
                if(distance1 < distance2) {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(stations.size() - 2, line);
                    pathElements.add(stations.size() - 2, line);
                    stations.add(stations.size() - 2, station);
                    station.getLines().add(this);
                } else {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(stations.size() - 1, line);
                    pathElements.add(stations.size() - 1, line);
                    stations.add(stations.size() - 1, station);
                    station.getLines().add(this);
                }
            } else {
                Double distance1;
                Double distance2;
                Point2D p1 = new Point2D(station.getCenterX(), station.getCenterY());
                Point2D p2 = new Point2D(stations.get(stations.indexOf(closestStation) - 1).getCenterX(), stations.get(stations.indexOf(closestStation) - 1).getCenterY());
                Point2D p3 = new Point2D(stations.get(stations.indexOf(closestStation) + 1).getCenterX(), stations.get(stations.indexOf(closestStation) + 1).getCenterY());
                distance1 = p1.distance(p2);
                distance2 = p1.distance(p3);
                if(distance1 < distance2) {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(stations.indexOf(closestStation) - 1, line);
                    pathElements.add(stations.indexOf(closestStation) - 1, line);
                    stations.add(stations.indexOf(closestStation) - 1, station);
                    station.getLines().add(this);
                } else {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(stations.indexOf(closestStation) + 1, line);
                    pathElements.add(stations.indexOf(closestStation) + 1, line);
                    stations.add(stations.indexOf(closestStation) + 1, station);
                    station.getLines().add(this);
                }
            }
            
        }
    }
    
    public void reAddStation(DraggableStation station) {
        dataComponent.getNodes().remove(station);
        dataComponent.getNodes().add(station);
        dataComponent.getNodes().remove(station.label);
        dataComponent.getNodes().add(station.label);
        if(stations.contains(station)) {
            dataComponent.setState(M3State.NEUTRAL);
            return;
        }
        if(stations.size() == 2) {
            stations.add(1, station);
            LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
            station.centerXProperty().bindBidirectional(line.xProperty());
            station.centerYProperty().bindBidirectional(line.yProperty());
            this.getElements().add(1, line);
            pathElements.add(1, line);
        } else {
            Point2D firstStation = new Point2D(stations.get(1).getCenterX(), stations.get(1).getCenterY());
            Point2D stationToAdd = new Point2D(station.getCenterX(), station.getCenterY());
            DraggableStation closestStation = stations.get(1);
            Double distance = stationToAdd.distance(firstStation);
            for (DraggableStation s : stations) {
                if(s.isEndPoint)
                    continue;
                Point2D p1 = new Point2D(s.getCenterX(), s.getCenterY());
                Point2D p2 = new Point2D(station.getCenterX(), station.getCenterY());
                if(p1.distance(p2) < distance) {
                    distance = p1.distance(p2);
                    closestStation = s;
                }
            }
            if(closestStation.equals(stations.get(1)) && stations.size() == 3) {
                Double distance1;
                Double distance2;
                Point2D p1 = new Point2D(station.getCenterX(), station.getCenterY());
                Point2D p2 = new Point2D(stations.get(0).getCenterX(), stations.get(0).getCenterY());
                Point2D p3 = new Point2D(stations.get(stations.size()-1).getCenterX(), stations.get(stations.size()-1).getCenterY());
                distance1 = p1.distance(p2);
                distance2 = p1.distance(p3);
                if(distance1 < distance2) {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(1, line);
                    pathElements.add(1, line);
                    stations.add(1, station);
                } else {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(2, line);
                    pathElements.add(2, line);
                    stations.add(2, station);
                }
            } else if(closestStation.equals(stations.get(1))) {
                Double distance1;
                Double distance2;
                Point2D p1 = new Point2D(station.getCenterX(), station.getCenterY());
                Point2D p2 = new Point2D(stations.get(0).getCenterX(), stations.get(0).getCenterY());
                Point2D p3 = new Point2D(stations.get(2).getCenterX(), stations.get(2).getCenterY());
                distance1 = p1.distance(p2);
                distance2 = p1.distance(p3);
                if(distance1 < distance2) {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(1, line);
                    pathElements.add(1, line);
                    stations.add(1, station);
                } else {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(2, line);
                    pathElements.add(2, line);
                    stations.add(2, station);
                }
            } else if(closestStation.equals(stations.get(stations.size()-2)) && stations.size() != 1) {
                Double distance1;
                Double distance2;
                Point2D p1 = new Point2D(station.getCenterX(), station.getCenterY());
                Point2D p2 = new Point2D(stations.get(stations.size()-1).getCenterX(), stations.get(stations.size()-1).getCenterY());
                Point2D p3 = new Point2D(stations.get(stations.size() - 3).getCenterX(), stations.get(stations.size() - 3).getCenterY());
                distance1 = p1.distance(p3);
                distance2 = p1.distance(p2);
                if(distance1 < distance2) {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(stations.size() - 2, line);
                    pathElements.add(stations.size() - 2, line);
                    stations.add(stations.size() - 2, station);
                } else {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(stations.size() - 1, line);
                    pathElements.add(stations.size() - 1, line);
                    stations.add(stations.size() - 1, station);
                }
            } else {
                Double distance1;
                Double distance2;
                Point2D p1 = new Point2D(station.getCenterX(), station.getCenterY());
                Point2D p2 = new Point2D(stations.get(stations.indexOf(closestStation) - 1).getCenterX(), stations.get(stations.indexOf(closestStation) - 1).getCenterY());
                Point2D p3 = new Point2D(stations.get(stations.indexOf(closestStation) + 1).getCenterX(), stations.get(stations.indexOf(closestStation) + 1).getCenterY());
                distance1 = p1.distance(p2);
                distance2 = p1.distance(p3);
                if(distance1 < distance2) {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(stations.indexOf(closestStation) - 1, line);
                    pathElements.add(stations.indexOf(closestStation) - 1, line);
                    stations.add(stations.indexOf(closestStation) - 1, station);
                } else {
                    LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
                    station.centerXProperty().bindBidirectional(line.xProperty());
                    station.centerYProperty().bindBidirectional(line.yProperty());
                    this.getElements().add(stations.indexOf(closestStation) + 1, line);
                    pathElements.add(stations.indexOf(closestStation) + 1, line);
                    stations.add(stations.indexOf(closestStation) + 1, station);
                }
            } 
        }
    }
    
    public void removeStation(DraggableStation station) {
        if(!stations.contains(station)) {
            dataComponent.setState(M3State.NEUTRAL);
            return;
        }
        LineTo line = new LineTo(station.getCenterX(), station.getCenterY());
        station.centerXProperty().unbindBidirectional(line.xProperty());
        station.centerYProperty().unbindBidirectional(line.yProperty());
        this.getElements().remove(stations.indexOf(station));
        stations.remove(station);
        station.getLines().remove(this);
    }
    
    public void insertLabelsAndPoints() {
        nameStart.xProperty().bind(moveTo.xProperty().subtract(nameStart.getLayoutBounds().getWidth() + 15));
        nameStart.yProperty().bind(moveTo.yProperty().subtract(nameStart.getLayoutBounds().getHeight() - 20));
        nameEnd.xProperty().bind(lineTo.xProperty().add(15));
        nameEnd.yProperty().bind(lineTo.yProperty().subtract(nameEnd.getLayoutBounds().getHeight() - 20));
        moveTo.xProperty().bindBidirectional(start.centerXProperty());
        moveTo.yProperty().bindBidirectional(start.centerYProperty());
        lineTo.xProperty().bindBidirectional(end.centerXProperty());
        lineTo.yProperty().bindBidirectional(end.centerYProperty());
        dataComponent.getNodes().add(nameStart);
        dataComponent.getNodes().add(nameEnd);
        
        initMoveEndPoints();
    }
    
    public void removeLabelsAndPoints() {
        dataComponent.getNodes().remove(nameStart);
        dataComponent.getNodes().remove(nameEnd);
    }
    
    public void fixLabelPosition() {
        nameStart.xProperty().unbind();
        nameStart.yProperty().unbind();
        nameEnd.xProperty().unbind();
        nameEnd.yProperty().unbind();
        nameStart.xProperty().bind(start.centerXProperty().subtract(nameStart.getLayoutBounds().getWidth() + 10));
        nameStart.yProperty().bind(start.centerYProperty().subtract(nameStart.getLayoutBounds().getHeight() - 20));
        nameEnd.xProperty().bind(end.centerXProperty().add(10));
        nameEnd.yProperty().bind(end.centerYProperty().subtract(nameEnd.getLayoutBounds().getHeight() - 20));
    }
    
    public void initMoveEndPoints() {
        nameStart.setOnMousePressed(e -> {
            nameStart.xProperty().unbind();
            nameStart.yProperty().unbind();
            nameStart.xProperty().bind(start.centerXProperty().subtract(nameStart.getLayoutBounds().getWidth() + 15));
            nameStart.yProperty().bind(start.centerYProperty().subtract(nameStart.getLayoutBounds().getHeight() - 20));
            //dataComponent.getNodes().add(start);
            //start.setDrag();
            dataComponent.setState(M3State.DRAGGING_LINE_END);
        });
        nameEnd.setOnMousePressed(e -> {
            nameEnd.xProperty().unbind();
            nameEnd.yProperty().unbind();
            nameEnd.xProperty().bind(end.centerXProperty().add(15));
            nameEnd.yProperty().bind(end.centerYProperty().subtract(nameEnd.getLayoutBounds().getHeight() - 20));
            //dataComponent.getNodes().add(end);
            //end.setDrag();
            dataComponent.setState(M3State.DRAGGING_LINE_END);
        });
    }
    
    public void removeEndPoint(DraggableStation point, DraggableText name) {
        //point.removeDrag();
        dataComponent.getNodes().remove(point);
        dataComponent.getNodes().add(name);
    }
    
    public void removeEndPoint(PathElement path, DraggableStation point, DraggableText name) {
        if(path instanceof MoveTo) {
            //point.removeDrag();
            dataComponent.getNodes().remove(point);
            name.xProperty().unbind();
            name.yProperty().unbind();
            name.xProperty().bind(((MoveTo)path).xProperty().subtract(name.getLayoutBounds().getWidth() + 15));
            name.yProperty().bind(((MoveTo)path).yProperty().subtract(name.getLayoutBounds().getHeight() - 20));
            name.setEndPoint(point);
            dataComponent.getNodes().add(name);
        } if(path instanceof LineTo) {
            //point.removeDrag();
            dataComponent.getNodes().remove(point);
            name.xProperty().unbind();
            name.yProperty().unbind();
            name.xProperty().bind(((LineTo)path).xProperty().add(15));
            name.yProperty().bind(((LineTo)path).yProperty().subtract(name.getLayoutBounds().getHeight() - 20));
            name.setEndPoint(point);
            dataComponent.getNodes().add(name);
        }
    }
    
    public DraggableText getNameStart() {
        return nameStart;
    }
    public DraggableText getNameEnd() {
        return nameEnd;
    }
    public void setNameStart(String nameStart) {
        this.nameStart.setText(nameStart);
    }
    public void setNameEnd(String nameEnd) {
        this.nameEnd.setText(nameEnd);
    }
    public void setNameStart(DraggableText nameStart) {
        this.nameStart = nameStart;
    }
    public void setNameEnd(DraggableText nameEnd) {
        this.nameEnd = nameEnd;
    }
    public DraggableStation getStart() {
        return start;
    }
    public DraggableStation getEnd() {
        return end;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
        this.setStroke(color);
    }
    public String getName() {
        return this.nameStart.getText();
    }
    public LineComboBoxItem getItem() {
        return this.item;
    }
    public void setItem(LineComboBoxItem item) {
        this.item = item;
    }
    public ArrayList<DraggableStation> getStations() {
        return stations;
    }
    public MoveTo getMoveTo() {
        return moveTo;
    }
    public LineTo getLineTo() {
        return lineTo;
    }
    public ArrayList<PathElement> getPathElements() {
        return pathElements;
    }
    public boolean isCircular() {
        return circular;
    }
    public void setCircular(boolean isCircular) {
        circular = isCircular;
    }
    public void setStations(ArrayList<DraggableStation> stations) {
        this.stations = stations;
    }
    public void setPathElements(ArrayList<PathElement> pathElements) {
        this.pathElements = pathElements;
    }
    public void setMoveTo(MoveTo moveTo) {
        this.moveTo = moveTo;
    }
    public void setLineTo(LineTo lineTo) {
        this.lineTo = lineTo;
    }
    public void setDataComponent(M3Data dataComponent) {
        this.dataComponent = dataComponent;
    }
    public void setStart(DraggableStation start) {
        this.start = start;
    }
    public void setEnd(DraggableStation end) {
        this.end = end;
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
    }

    @Override
    public void drag(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void size(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getNodeType() {
        return LINE;
    }

    @Override
    public double getX() {
        return this.moveTo.getX();
    }

    @Override
    public double getY() {
        return this.moveTo.getY();
    }
    
}
