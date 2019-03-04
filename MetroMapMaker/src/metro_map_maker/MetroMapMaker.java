/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker;

import djf.AppTemplate;
import java.util.Locale;
import static javafx.application.Application.launch;
import metro_map_maker.data.M3Data;
import metro_map_maker.file.M3Files;
import metro_map_maker.gui.M3Workspace;
import metro_map_maker.gui.Welcome;

/**
 *
 * @author alexc
 */
public class MetroMapMaker extends AppTemplate {
     /**
     * This hook method must initialize all three components in the
     * proper order ensuring proper dependencies are respected, meaning
     * all proper objects are already constructed when they are needed
     * for use, since some may need others for initialization.
     */
    @Override
    public void buildAppComponentsHook() {
        // CONSTRUCT ALL THREE COMPONENTS. NOTE THAT FOR THIS APP
        // THE WORKSPACE NEEDS THE DATA COMPONENT TO EXIST ALREADY
        // WHEN IT IS CONSTRUCTED, AND THE DATA COMPONENT NEEDS THE
        // FILE COMPONENT SO WE MUST BE CAREFUL OF THE ORDER
        fileComponent = new M3Files();
        dataComponent = new M3Data(this);
        workspaceComponent = new M3Workspace(this);
        Welcome welcome = new Welcome(fileComponent, dataComponent, workspaceComponent, this.gui.getAppPane(), this.gui.getNewButton(), this.gui);
    }
    
    /**
     * This is where program execution begins. Since this is a JavaFX app it
     * will simply call launch, which gets JavaFX rolling, resulting in sending
     * the properly initialized Stage (i.e. window) to the start method inherited
     * from AppTemplate, defined in the Desktop Java Framework.
     * @param args
     */
    public static void main(String[] args) {
	Locale.setDefault(Locale.US);
	launch(args);
    }
    
}
