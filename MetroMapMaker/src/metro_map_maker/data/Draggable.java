/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.data;

import djf.AppTemplate;
import javafx.scene.Node;

/**
 *
 * @author alexc
 */
public interface Draggable {
    public static final String STATION = "STATION";
    public static final String ENDPOINT = "ENDPOINT";
    public static final String LINE = "LINE";
    public static final String IMAGE = "IMAGE";
    public static final String TEXT = "TEXT";
    
    public Node copy(Node node, AppTemplate app);
    public M3State getStartingState();
    public void start(int x, int y);
    public void drag(int x, int y);
    public void size(int x, int y);
    public double getX();
    public double getY();
    public double getWidth();
    public double getHeight();
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight);
    public String getNodeType();
}
