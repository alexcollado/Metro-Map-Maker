/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.data;

import djf.AppTemplate;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author alexc
 */
public class DraggableText extends Text implements Draggable {
    DraggableStation endPoint;
    Color color;
    double startX;
    double startY;
    boolean bold;
    boolean italic;
    boolean isLabel;
    boolean isStationLabel;
    
    public DraggableText(String text) {
        super(text);
        startX = 0.0;
        startY = 0.0;
        bold = false;
        italic = false;
        isLabel = false;
        isStationLabel = false;
        color = Color.valueOf("#000000");
        setFill(Color.valueOf("#000000"));
    }
    
    public DraggableText(String text, boolean isLabel) {
        super(250, 250, text);
        startX = 0.0;
        startY = 0.0;
        bold = false;
        italic = false;
        this.isLabel = isLabel;
        color = Color.valueOf("#000000");
        setFill(Color.valueOf("#000000"));
    }
    
    public DraggableText(){};
    
    public void setEndPoint(DraggableStation endPoint) {
        this.endPoint = endPoint;
    }
    public DraggableStation getEndPoint() {
        return this.endPoint;
    }
    
    public boolean isBold() {
        return bold;
    }
    public boolean isItalic() {
        return italic;
    }
    public void setIsBold(boolean isBold) {
        bold = isBold;
    }
    public void setIsItalic(boolean isItalic) {
        italic = isItalic;
    }
    public boolean isLabel() {
        return isLabel;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return color;
    }
    public void setIsLabel(boolean isLabel) {
        this.isLabel = isLabel;
    }
    
    public void setIsStationLabel(boolean isStationLabel) {
        this.isStationLabel = isStationLabel;
    }
    public boolean isStationLabel() {
        return isStationLabel;
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
        startX = x;
        startY = y;
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
        xProperty().set(initX);
	yProperty().set(initY);
    }

    @Override
    public String getNodeType() {
        return TEXT;
    }

    @Override
    public void drag(int x, int y) {
        double diffX = x - (startX);
	double diffY = y - (startY);
	double newX = getX() + diffX;
	double newY = getY() + diffY;
	xProperty().set(newX);
	yProperty().set(newY);
        startX = x;
	startY = y;
    }
    
}
