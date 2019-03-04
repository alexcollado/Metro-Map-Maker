/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.data;

import djf.AppTemplate;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author alexc
 */
public class DraggableImage extends ImageView implements Draggable {
    double startX;
    double startY;
    String filePath;
    boolean isBackground;
    
    public DraggableImage() {
	startX = 0.0;
	startY = 0.0;
    }
    
    public DraggableImage(Image image, boolean isBackground) {
        super(image);
        this.isBackground = isBackground;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setIsBackground(boolean isBackground) {
        this.isBackground = isBackground;
    }
    public boolean isBackground() {
        return isBackground;
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
        return IMAGE;
    }
    
}
