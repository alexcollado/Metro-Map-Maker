/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.gui;

import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import djf.components.AppWorkspaceComponent;
import djf.controller.AppFileController;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import static djf.settings.AppStartupConstants.PATH_WORK;
import djf.ui.AppGUI;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import static metro_map_maker.css.M3Style.CLASS_WELCOME_LEFT;
import static metro_map_maker.css.M3Style.CLASS_WELCOME_RIGHT;

/**
 *
 * @author alexc
 */
public final class Welcome {
    VBox recentMaps;
    VBox createMap;
    
    Label recentWork;
    
    Pane panes;
    
    Scene welcomeScene;
    Stage welcomeStage;
    
    MapEditController mapEditController;
    
    public Welcome(AppFileComponent fileComponent, AppDataComponent dataComponent, AppWorkspaceComponent workspaceComponent, BorderPane appPane, Button newButton, AppGUI gui) {
        recentMaps = new VBox();
        createMap = new VBox();
        
        recentMaps.setPadding(new Insets(120, 20, 0, 20));
        createMap.setSpacing(30);
        recentMaps.setSpacing(20);
        
        recentWork = new Label("Recent Works" + '\n');
        
        recentMaps.getChildren().add(recentWork);

        File folder = new File(PATH_WORK);
        File[] listOfFiles = folder.listFiles();
        Arrays.sort(listOfFiles, (File f1, File f2) -> Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()));
        
        Hyperlink link;
        Hyperlink create;
        
        int count = 0;
        
        for(final File map:listOfFiles) {
            if(count > 5)
                break;
            link = new Hyperlink(map.getName());
            link.setBorder(Border.EMPTY);
            link.setUnderline(true);
            recentMaps.getChildren().add(link);
            count++;
            link.setOnAction(e->{
                try {
                    fileComponent.loadData(workspaceComponent, dataComponent, map.getAbsolutePath());
                    workspaceComponent.activateWorkspace(appPane);
                    ((M3Workspace)workspaceComponent).getMapEditController().setCurrentWorkFile(map.getAbsoluteFile());
                    File selectedFile = new File(PATH_WORK + map.getName());
                    gui.updateToolbarControls(true);
                    welcomeStage.close();
                } catch (IOException ex) {
                    Logger.getLogger(Welcome.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        
        Image logo = new Image(FILE_PROTOCOL + PATH_IMAGES + "Logo.png");
        ImageView logoContainer = new ImageView(logo);
        
        create = new Hyperlink("Create New Metro Map");
        create.setBorder(Border.EMPTY);
        create.setUnderline(true);
        
        create.setOnAction(e->{
            int before = folder.listFiles().length;
            newButton.fire();
            int after = new File(PATH_WORK).listFiles().length;
            
            if(before != after)
                welcomeStage.close();
        });
        
        createMap.getChildren().addAll(logoContainer, create);
        
        initWelcomeStyle();
        
        // WE'LL RENDER OUR STUFF HERE IN THE CANVAS
	panes = new Pane();
	
	// AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
//	M3Data data = (M3Data)app.getDataComponent();
//	data.setNodes(canvas.getChildren());

	// AND NOW SETUP THE PANES
	panes = new BorderPane();
	((BorderPane)panes).setLeft(recentMaps);
	((BorderPane)panes).setCenter(createMap);
        
        welcomeScene = new Scene(panes);
        
        welcomeStage = new Stage();
        welcomeStage.setScene(welcomeScene);
        
        
        welcomeStage.setMinWidth(700);
        welcomeStage.setMinHeight(550);
        welcomeStage.showAndWait();
    }
    
    public void initWelcomeStyle() {
        createMap.setAlignment(Pos.CENTER);
        recentMaps.getStyleClass().add(CLASS_WELCOME_LEFT);
        createMap.getStyleClass().add(CLASS_WELCOME_RIGHT);
    }
    
}
