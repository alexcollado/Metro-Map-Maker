/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.file;

import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import djf.components.AppWorkspaceComponent;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import metro_map_maker.gui.M3Workspace;
import metro_map_maker.data.DraggableImage;
import metro_map_maker.data.DraggableLine;
import metro_map_maker.data.DraggableStation;
import metro_map_maker.data.DraggableText;
import metro_map_maker.data.M3Data;

/**
 *
 * @author alexc
 */
public class M3Files implements AppFileComponent {
    static final String JSON_STATIONS = "stations";
    static final String JSON_LINES = "lines";
    static final String JSON_IMAGES = "images";
    static final String JSON_TEXT = "text";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_ALPHA = "alpha";
    static final String JSON_BG_COLOR = "background_color";
    static final String JSON_BG_IMAGE = "background_image";
    
    static final String JSON_NAME = "name";
    static final String JSON_TYPE = "type";
    static final String JSON_X = "x";
    static final String JSON_Y = "y";
    static final String JSON_SIZE = "size";
    static final String JSON_STROKE_WIDTH = "stroke width";
    static final String JSON_WIDTH = "width";
    static final String JSON_HEIGHT = "height";
    static final String JSON_COLOR = "color";
    static final String JSON_STATIONS_IN_LINE = "stationsInLine";
    static final String JSON_PATH_ELEMENTS = "pathElements";
    static final String JSON_MAPPING = "mapping";
    static final String JSON_FILEPATH = "file_path";
    static final String JSON_STRING = "testing";
    static final String JSON_CIRCULAR = "circular";
    static final String JSON_STATION = "stations";
    static final String JSON_RADIUS = "radius";
    static final String JSON_IS_ENDPOINT = "is_endpoint";
    static final String JSON_POSITION = "position";
    static final String JSON_ROTATE = "rotate";
    static final String JSON_IS_BACKGROUND = "is_background";
    static final String JSON_IS_LABEL = "is_label";
    static final String JSON_IS_STATION_LABEL = "is_station_label";
    static final String JSON_CANVAS = "canvas";
    
    
    static final String JSON_TEXT_VALUE = "text_value";
    static final String JSON_FONT_TYPE = "font_type";
    static final String JSON_FONT_SIZE = "font_size";
    static final String JSON_IS_BOLD = "is_bold";
    static final String JSON_IS_ITALICIZED = "is_italicized";

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        // GET THE DATA
	M3Data dataManager = (M3Data)data;
        
        //SAVE BACKGROUND COLOR
        Color bgColor = dataManager.getBackgroundColor();
        if (bgColor == null)
            bgColor = Color.web("#FFFFFF");
	JsonObject bgColorJson = makeJsonColorObject(bgColor);
        
        //SAVE CANVAS SIZE
        double canvasWidth = dataManager.getCanvas().getWidth();
        double canvasHeight = dataManager.getCanvas().getHeight();
        JsonObject jsonCanvas = Json.createObjectBuilder()
                .add(JSON_WIDTH, canvasWidth)
                .add(JSON_HEIGHT, canvasHeight).build();
        
//        Image bgImage = new Image();
//        ImageView bgImageView = dataManager.getImageBackground();
//        JsonObject bgImageJson = Json.createObjectBuilder()
//                .add(JSON_BG_IMAGE, bgImage.get)
        
        DraggableImage bgDImage = dataManager.getImageBackground();
        Image bgImage;
        JsonObject bgImageJson;
        //Image bgImage = dataManager.getImageBackground().getImage();
        if(bgDImage == null) { 
            //File def = new File(FILE_PROTOCOL + PATH_IMAGES + "DefaultBackground.png");
            bgImage = new Image(FILE_PROTOCOL + PATH_IMAGES + "DefaultBackground.png");
            DraggableImage fin = new DraggableImage(bgImage, true);
            fin.setFilePath(FILE_PROTOCOL + PATH_IMAGES + "DefaultBackground.png");
            DraggableImage bgDraggableImage = new DraggableImage(bgImage, true);
            bgDraggableImage.setFilePath(fin.getFilePath());
            bgImageJson = Json.createObjectBuilder()
                    .add(JSON_IS_BACKGROUND, bgDraggableImage.isBackground())
                    .add(JSON_FILEPATH, bgDraggableImage.getFilePath()).build();
        } else {
            bgImage = dataManager.getImageBackground().getImage();
            DraggableImage bgDraggableImage = new DraggableImage(bgImage, true);
            bgDraggableImage.setFilePath(dataManager.getImageBackground().getFilePath());
            bgImageJson = Json.createObjectBuilder()
                        .add(JSON_IS_BACKGROUND, bgDraggableImage.isBackground())
                        .add(JSON_FILEPATH, bgDraggableImage.getFilePath()).build();
        }

	// NOW BUILD THE JSON OBJCTS TO SAVE
	JsonArrayBuilder lineArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder stationArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder textArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder imageArrayBuilder = Json.createArrayBuilder();
	ObservableList<Node> nodes = dataManager.getNodes();
	for (Node node : nodes) {
            if (node instanceof DraggableLine) {
                DraggableLine line = (DraggableLine)node;
                String name = line.getName();
                JsonObject color = makeJsonColorObject(line.getColor());
                double strokeWidth = line.getStrokeWidth();
                JsonArrayBuilder stationsInLineBuilder = Json.createArrayBuilder();
                ArrayList<DraggableStation> stationsInLine = line.getStations();
                for(DraggableStation station:stationsInLine) {
                    JsonObject fillColorJson = makeJsonColorObject((Color)station.getFill());
                    JsonObject label = makeJsonLabelObject(station.getLabel());
                    if(station.getIsEndPoint()) {
                        JsonObject stationJson = Json.createObjectBuilder()
                            .add(JSON_NAME, label)
                            .add(JSON_X, station.getCenterX())
                            .add(JSON_Y, station.getCenterY())
                            .add(JSON_RADIUS, station.getRadius())
                            .add(JSON_POSITION, station.getPosVal())
                            .add(JSON_ROTATE, station.getRotateVal())
                            .add(JSON_COLOR, fillColorJson)
                            .add(JSON_IS_ENDPOINT, station.getIsEndPoint()).build();
                        stationsInLineBuilder.add(stationJson);
                    } else {
                        JsonObject stationJson = Json.createObjectBuilder()
                            .add(JSON_NAME, label)
                            .add(JSON_X, station.getCenterX())
                            .add(JSON_Y, station.getCenterY())
                            .add(JSON_RADIUS, station.getRadius())
                            .add(JSON_POSITION, station.getPosVal())
                            .add(JSON_ROTATE, station.getRotateVal())
                            .add(JSON_COLOR, fillColorJson)
                            .add(JSON_IS_ENDPOINT, station.getIsEndPoint()).build();
                        stationsInLineBuilder.add(stationJson);
                    }
                }
                boolean circular = line.isCircular();

                JsonObject lineJson = Json.createObjectBuilder()
                        .add(JSON_NAME, name)
                        .add(JSON_COLOR, color)
                        .add(JSON_STROKE_WIDTH, strokeWidth)
                        .add(JSON_CIRCULAR, circular)
                        .add(JSON_STATION, stationsInLineBuilder)
                        .build();
                lineArrayBuilder.add(lineJson);
            } else if(node instanceof DraggableText) {
                DraggableText draggableText = ((DraggableText)node);
                double x = draggableText.getX();
                double y = draggableText.getY();
                boolean isBold = draggableText.isBold();
                boolean isItalic = draggableText.isItalic();
                boolean isLabel = draggableText.isLabel();
                if(!isLabel)
                    continue;
                boolean isStationLabel = draggableText.isStationLabel();
                String textValue = draggableText.getText();
                String fontType = draggableText.getFont().getFamily();
                double fontSize = draggableText.getFont().getSize();
                JsonObject fillColorJson = makeJsonColorObject((Color)draggableText.getFill());
                
                JsonObject textJson = Json.createObjectBuilder()
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_FONT_TYPE, fontType)
                        .add(JSON_FONT_SIZE, fontSize)
                        .add(JSON_IS_BOLD, isBold)
                        .add(JSON_IS_ITALICIZED, isItalic)
                        .add(JSON_TEXT_VALUE, textValue)
                        .add(JSON_IS_LABEL, isLabel)
                        .add(JSON_IS_STATION_LABEL, isStationLabel)
                        .add(JSON_COLOR, fillColorJson).build();
                textArrayBuilder.add(textJson);
            } else if(node instanceof DraggableStation) {
                DraggableStation station = (DraggableStation)node;
                if(!station.getLines().isEmpty())
                    continue;
                //String type = station.getNodeType();
                String name = station.getLabel().getText();
                double x = station.getX();
                double y = station.getY();
                double width = station.getWidth();
                double height = station.getHeight();
                JsonObject fillColorJson = makeJsonColorObject((Color)station.getFill());
                JsonObject label = makeJsonLabelObject(station.getLabel());

                if(station.getIsEndPoint()) {
                    JsonObject stationJson = Json.createObjectBuilder()
                        .add(JSON_NAME, label)
                        .add(JSON_X, station.getCenterX())
                        .add(JSON_Y, station.getCenterY())
                        .add(JSON_RADIUS, station.getRadius())
                        .add(JSON_POSITION, station.getPosVal())
                        .add(JSON_ROTATE, station.getRotateVal())
                        .add(JSON_COLOR, fillColorJson)
                        .add(JSON_IS_ENDPOINT, station.getIsEndPoint()).build();
                    stationArrayBuilder.add(stationJson);
                } else {
                    JsonObject stationJson = Json.createObjectBuilder()
                        .add(JSON_NAME, label)
                        .add(JSON_X, station.getCenterX())
                        .add(JSON_Y, station.getCenterY())
                        .add(JSON_RADIUS, station.getRadius())
                        .add(JSON_POSITION, station.getPosVal())
                        .add(JSON_ROTATE, station.getRotateVal())
                        .add(JSON_COLOR, fillColorJson)
                        .add(JSON_IS_ENDPOINT, station.getIsEndPoint()).build();
                    stationArrayBuilder.add(stationJson);
                }
            } else if(node instanceof DraggableImage) {
                DraggableImage image = (DraggableImage)node;
                double x = image.getX();
                double y = image.getY();
                boolean isBackground = image.isBackground();
                String imagePath = image.getFilePath();
                
                JsonObject imageJson = Json.createObjectBuilder()
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_IS_BACKGROUND, isBackground)
                        .add(JSON_FILEPATH, imagePath).build();
                imageArrayBuilder.add(imageJson);
            }
	}
	JsonArray stationsArray = stationArrayBuilder.build();
        JsonArray linesArray = lineArrayBuilder.build();
        JsonArray textArray = textArrayBuilder.build();
	JsonArray imageArray = imageArrayBuilder.build();
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_BG_IMAGE, bgImageJson)
                .add(JSON_CANVAS, jsonCanvas)
                .add(JSON_BG_COLOR, bgColorJson)
                .add(JSON_STATIONS, stationsArray)
                .add(JSON_LINES, linesArray)
                .add(JSON_TEXT, textArray)
                .add(JSON_IMAGES, imageArray)
                .build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }

    @Override
    public void loadData(AppWorkspaceComponent work, AppDataComponent data, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT
	M3Data dataManager = (M3Data)data;
        M3Workspace workspace = (M3Workspace)work;
	dataManager.resetData();

        File file = new File(filePath);
        if(file.length() == 0)
            return;

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        
        ArrayList<DraggableLine> lines = new ArrayList();
    
	// LOAD THE BACKGROUND COLOR
	Color bgColor = loadColor(json, JSON_BG_COLOR);
	dataManager.editBackgroundColor(bgColor);
        
        // LOAD THE BACKGROUND IMAGE
        DraggableImage image = loadImage(json, JSON_BG_IMAGE);
        dataManager.setImageBackground(image);
        
        // LOAD CANVAS SIZE
        loadSize(json, JSON_CANVAS, workspace);
        
        // AND NOW LOAD ALL THE IMAGES
	JsonArray jsonImageArray = json.getJsonArray(JSON_IMAGES);
        if (jsonImageArray != null) {
            for (int i = 0; i < jsonImageArray.size(); i++) {
                JsonObject jsonImage = jsonImageArray.getJsonObject(i);
                DraggableImage imageOverlay = loadImage(jsonImage);
                dataManager.addImage(imageOverlay);
            }
        }
        
        //AND NOW LOAD ALL THE LINES
        JsonArray jsonLinesArray = json.getJsonArray(JSON_LINES);
        if (jsonLinesArray != null) {
            for (int i = 0; i < jsonLinesArray.size(); i++) {
                JsonObject jsonShape = jsonLinesArray.getJsonObject(i);
                DraggableLine line = loadLine(jsonShape, dataManager, workspace, lines);
                lines.add(line);
                dataManager.getNodes().add(line);
                line.insertLabelsAndPoints();
                for(DraggableStation station:line.getStations()) {
                    if(!station.getIsEndPoint()) {
                        dataManager.getNodes().add(station);
                        //dataManager.getNodes().add(station.getLabel());
                    }
                }
                //dataManager.getNodes().add(line.getNameStart());
                //dataManager.getNodes().add(line.getNameEnd());
            }    
        }
	
	// AND NOW LOAD ALL THE STATIONS
	JsonArray jsonStationsArray = json.getJsonArray(JSON_STATIONS);
        if (jsonStationsArray != null) {
            for (int i = 0; i < jsonStationsArray.size(); i++) {
                JsonObject jsonShape = jsonStationsArray.getJsonObject(i);
                DraggableStation station = loadStation(jsonShape, dataManager, workspace);
                if(station.getLine() == null)
                    dataManager.addStation(station);
            }    
        } 
        
        if(!workspace.getLinesComboBox().getItems().isEmpty()) {
            workspace.getLineAdd().setDisable(false);
            workspace.getLineRemove().setDisable(false);
            workspace.getAddStationToLine().setDisable(false);
            workspace.getRemoveStationFromLine().setDisable(false);
            workspace.getListLines().setDisable(false);
            workspace.getSnap().setDisable(false);
        }
        if(!workspace.getStationsComboBox().getItems().isEmpty()) {
            workspace.getStationAdd().setDisable(false);
            workspace.getStationRemove().setDisable(false);
            workspace.getMoveLabel().setDisable(false);
            workspace.getRotateLabel().setDisable(false);
            workspace.getSnap().setDisable(false);
        }
        
        // AND NOW LOAD ALL THE TEXT
	JsonArray jsonTextArray = json.getJsonArray(JSON_TEXT);
        if (jsonTextArray != null) {
            for (int i = 0; i < jsonTextArray.size(); i++) {
	    JsonObject jsonText = jsonTextArray.getJsonObject(i);
	    DraggableText text = loadText(jsonText);
	    dataManager.addText(text);
	}
        }
        
        dataManager.getJTPS().clear();
        dataManager.disableUndoRedo();
    }
    
    private double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    
    private int getDataAsInt(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber)value;
        return number.bigIntegerValue().intValue();
    }
    
    private String getDataAsString(JsonObject json, String dataName) {
        String value = json.getJsonString(dataName).getString();
        return value;
    }
    
    private boolean getDataAsBoolean(JsonObject json, String dataName) {
        boolean value = json.getBoolean(dataName);
        return value;
    }
    
    private DraggableText getDataAsLabel(JsonObject json, String dataName) {
        DraggableText value = new DraggableText();
        //JsonArray jsonArray = json.getJsonArray(dataName);
        double x = getDataAsDouble(json.getJsonObject(dataName), JSON_X);
	double y = getDataAsDouble(json.getJsonObject(dataName), JSON_Y);
        String textValue = getDataAsString(json.getJsonObject(dataName), JSON_TEXT_VALUE);
        String fontType = getDataAsString(json.getJsonObject(dataName), JSON_FONT_TYPE);
        double size = getDataAsDouble(json.getJsonObject(dataName), JSON_FONT_SIZE);
        boolean isBold = getDataAsBoolean(json.getJsonObject(dataName), JSON_IS_BOLD);
        boolean isItalic = getDataAsBoolean(json.getJsonObject(dataName), JSON_IS_ITALICIZED);
        boolean isLabel = getDataAsBoolean(json.getJsonObject(dataName), JSON_IS_LABEL);
        boolean isStationLabel = getDataAsBoolean(json.getJsonObject(dataName), JSON_IS_STATION_LABEL);
        
        value.setX(x);
        value.setY(y);
        value.setText(textValue);
        Color fillColor = loadColor(json.getJsonObject(dataName), JSON_COLOR);
        
        value.setFill(fillColor);
        value.setColor(fillColor);
        value.setIsLabel(isLabel);
        value.setIsStationLabel(isStationLabel);
        
        if (isBold && !isItalic) {
            value.setFont(Font.font(fontType, FontWeight.BOLD, size));
        } else if (isBold && isItalic) {
            value.setFont(Font.font(fontType, FontWeight.BOLD, FontPosture.ITALIC, size));
        } else if (!isBold && !isItalic) {
            value.setFont(Font.font(fontType, size));
        } else if (!isBold && isItalic) {
            value.setFont(Font.font(fontType, FontPosture.ITALIC, size));
        }
        
        //text.setText(textValue);
        return value;
    }
    
    private ArrayList<DraggableStation> getDataAsListOfStations(JsonObject json, DraggableLine line, String dataName, M3Data dataComponent) {
        ArrayList<DraggableStation> data = new ArrayList<>();

        JsonArray jsonArray = json.getJsonArray(dataName);

        for (int i = 0; i < jsonArray.size(); i++) {
            boolean isEndpoint = jsonArray.getJsonObject(i).getBoolean(JSON_IS_ENDPOINT);
            JsonValue x = jsonArray.getJsonObject(i).getJsonNumber(JSON_X);
            JsonValue y = jsonArray.getJsonObject(i).getJsonNumber(JSON_Y);
            JsonValue radius = jsonArray.getJsonObject(i).getJsonNumber(JSON_RADIUS);
            Color fillColor = loadColor(jsonArray.getJsonObject(i), JSON_COLOR);
            DraggableText label = getDataAsLabel(jsonArray.getJsonObject(i), JSON_NAME);
            DraggableStation station = new DraggableStation
                ((((JsonNumber)x).intValueExact()), 
                (((JsonNumber)y).intValueExact()),
                (((JsonNumber)radius).intValueExact()),
                label,
                (jsonArray.getJsonObject(i).getJsonString(JSON_POSITION).getString()),
                (jsonArray.getJsonObject(i).getJsonNumber(JSON_ROTATE).doubleValue()),
                fillColor,
                dataComponent);
            station.setIsEndPoint(isEndpoint);
            if(!station.getIsEndPoint()) {
                station.addLabel();
                if(station.getLines() == null)
                    station.setLines(new ArrayList());
                station.getLines().add(line);
            }
            data.add(station);
        }
        
        return data;
    }
    
    private DraggableLine loadLine(JsonObject jsonLine, M3Data dataManager, M3Workspace workspace, ArrayList<DraggableLine> lines) {
        DraggableLine line = new DraggableLine();
        
        String lineName = getDataAsString(jsonLine, JSON_NAME);
        Color lineColor = loadColor(jsonLine, JSON_COLOR);
        double strokeWidth = getDataAsDouble(jsonLine, JSON_STROKE_WIDTH);
        boolean isCircular = getDataAsBoolean(jsonLine, JSON_CIRCULAR);
        M3Data.LineComboBoxItem item = dataManager.new LineComboBoxItem(lineName, line);
        ArrayList<DraggableStation> stations = getDataAsListOfStations(jsonLine, line, JSON_STATION, dataManager);
        ArrayList<PathElement> pathElements = new ArrayList();
        line.setDataComponent(dataManager);
        
        line.setStations(stations);
        line.setPathElements(pathElements);
        
        DraggableText nameStart = stations.get(0).getLabel();
        DraggableText nameEnd = stations.get(stations.size() - 1).getLabel();
        
        MoveTo moveTo = new MoveTo(stations.get(0).getCenterX(), stations.get(0).getCenterY());
        line.setMoveTo(moveTo);
        pathElements.add(moveTo);
        
        for(DraggableStation station:stations) {
            if(!station.getIsEndPoint()) {
                boolean check = false;
                for(DraggableLine lineCheck:lines) {
                    for(DraggableStation stationCheck:lineCheck.getStations()) {
                        if(stationCheck.getName().getText().equals(station.getName().getText())) {
                            workspace.getStationsComboBox().getItems().remove(stationCheck.getItem());
                            dataManager.getNodes().remove(stationCheck);
                            dataManager.getNodes().remove(stationCheck.getLabel());
                            LineTo lineTo = new LineTo(station.getCenterX(), station.getCenterY());
                            lineCheck.getElements().remove(lineCheck.getStations().indexOf(stationCheck));
                            lineCheck.getElements().add(lineCheck.getStations().indexOf(stationCheck), lineTo);
                            station.centerXProperty().bindBidirectional(lineTo.xProperty());
                            station.centerYProperty().bindBidirectional(lineTo.yProperty());
                        }
                    }
                }
                LineTo lineTo = new LineTo(station.getCenterX(), station.getCenterY());
                station.centerXProperty().bindBidirectional(lineTo.xProperty());
                station.centerYProperty().bindBidirectional(lineTo.yProperty());
                pathElements.add(lineTo);
                //line.addStation(station);
                M3Data.StationComboBoxItem stationItem = dataManager.new StationComboBoxItem(station.getName().getText(), station);
                station.setItem(stationItem);
                workspace.getStationsComboBox().getItems().add(stationItem);
                workspace.getStationsComboBox().setValue(stationItem);
                if(!workspace.getStationStart().getItems().contains(stationItem))
                    workspace.getStationStart().getItems().add(stationItem);
                if(!workspace.getStationEnd().getItems().contains(stationItem))
                    workspace.getStationEnd().getItems().add(stationItem);
            }
        }
        
        LineTo lineTo = new LineTo(stations.get(stations.size()-1).getCenterX(), stations.get(stations.size()-1).getCenterY());
        line.setLineTo(lineTo);
        pathElements.add(lineTo);
        
        line.setNameStart(nameStart);
        line.getNameStart().setEndPoint(stations.get(0));
        line.getNameStart().getEndPoint().setLine(line);
        line.setNameEnd(nameEnd);
        line.getNameEnd().setEndPoint(stations.get(stations.size()-1));
        line.getNameEnd().getEndPoint().setLine(line);
        
        nameStart.xProperty().bind(moveTo.xProperty().subtract(nameStart.getLayoutBounds().getWidth() + 15));
        nameStart.yProperty().bind(moveTo.yProperty().subtract(nameStart.getLayoutBounds().getHeight() - 20));
        nameEnd.xProperty().bind(lineTo.xProperty().add(15));
        nameEnd.yProperty().bind(lineTo.yProperty().subtract(nameEnd.getLayoutBounds().getHeight() - 20));
        
        line.setColor(lineColor);
        line.setCircular(isCircular);
        line.getElements().addAll(pathElements);
        line.setStrokeWidth(strokeWidth);
        line.setStart(stations.get(0));
        line.setItem(item);
        line.setEnd(stations.get(stations.size()-1));
        line.setDataComponent(dataManager);
        line.setItem(item);
        
        workspace.getLinesComboBox().getItems().add(item);
        workspace.getLinesComboBox().setValue(item);
        dataManager.loadLineColor();
        dataManager.loadStationColor();
        
        return line;
    }
    
    private DraggableStation loadStation(JsonObject jsonStation, M3Data dataManager, M3Workspace workspace) {
        int x = getDataAsInt(jsonStation, JSON_X);
        int y = getDataAsInt(jsonStation, JSON_Y);
        int radius = getDataAsInt(jsonStation, JSON_RADIUS);
        DraggableText label = getDataAsLabel(jsonStation, JSON_NAME);
        String position = getDataAsString(jsonStation, JSON_POSITION);
        double rotate = getDataAsDouble(jsonStation, JSON_ROTATE);
        Color fillColor = loadColor(jsonStation, JSON_COLOR);
        
        DraggableStation station = new DraggableStation(x, y, radius, label, position, rotate, fillColor, dataManager);
        station.setIsEndPoint(false);
        if(!station.getIsEndPoint())
            station.addLabel();
        station.setLines(new ArrayList());
        
        M3Data.StationComboBoxItem item = dataManager.new StationComboBoxItem(label.getText(), station);
        station.setItem(item);
        workspace.getStationsComboBox().getItems().add(item);
        workspace.getStationsComboBox().setValue(item);
        dataManager.loadStationColor();
        
        return station;
    }
    
    private DraggableImage loadImage(JsonObject json, String imageToGet) {
        JsonObject jsonImage = json.getJsonObject(imageToGet);
        DraggableImage image = new DraggableImage();

        boolean isBackground = getDataAsBoolean(jsonImage, JSON_IS_BACKGROUND);
        
        if(isBackground) {
            image.setFilePath(loadFilePath(jsonImage, JSON_FILEPATH));
            Image pic = new Image(image.getFilePath());
            image.setImage(pic);
        } else {
            double x = getDataAsDouble(jsonImage, JSON_X);
            double y = getDataAsDouble(jsonImage, JSON_Y);

            image.setFilePath(loadFilePath(jsonImage, JSON_FILEPATH));
            Image pic = new Image(image.getFilePath());
            image.setImage(pic);
            image.setLocationAndSize(x, y, 0, 0);
        }
        
        return image;
    }
    
    private DraggableImage loadImage(JsonObject json) {
        DraggableImage image = new DraggableImage();
        double x = getDataAsDouble(json, JSON_X);
        double y = getDataAsDouble(json, JSON_Y);
        boolean isBackground = getDataAsBoolean(json, JSON_IS_BACKGROUND);
        image.setFilePath(loadFilePath(json, JSON_FILEPATH));
        Image pic = new Image(image.getFilePath());
        image.setIsBackground(isBackground);
        image.setImage(pic);
        image.setLocationAndSize(x, y, 0, 0);
        return image;
    }
    
    private DraggableText loadText(JsonObject jsonText) {
        double x = getDataAsDouble(jsonText, JSON_X);
	double y = getDataAsDouble(jsonText, JSON_Y);
        String textValue = getDataAsString(jsonText, JSON_TEXT_VALUE);
        String fontType = getDataAsString(jsonText, JSON_FONT_TYPE);
        double size = getDataAsDouble(jsonText, JSON_FONT_SIZE);
        boolean isBold = getDataAsBoolean(jsonText, JSON_IS_BOLD);
        boolean isItalic = getDataAsBoolean(jsonText, JSON_IS_ITALICIZED);
        boolean isLabel = getDataAsBoolean(jsonText, JSON_IS_LABEL);
        boolean isStationLabel = getDataAsBoolean(jsonText, JSON_IS_STATION_LABEL);
        
        DraggableText text = new DraggableText(textValue);
        
        Color fillColor = loadColor(jsonText, JSON_COLOR);
        
        text.setFill(fillColor);
        text.setIsLabel(isLabel);
        text.setIsStationLabel(isStationLabel);
        
        if (isBold && !isItalic) {
            text.setFont(Font.font(fontType, FontWeight.BOLD, size));
        } else if (isBold && isItalic) {
            text.setFont(Font.font(fontType, FontWeight.BOLD, FontPosture.ITALIC, size));
        } else if (!isBold && !isItalic) {
            text.setFont(Font.font(fontType, size));
        } else if (!isBold && isItalic) {
            text.setFont(Font.font(fontType, FontPosture.ITALIC, size));
        }
        
        text.setText(textValue);
        
	text.setLocationAndSize(x, y, 0, 0);
        
        return text;
    }
    
    private Color loadColor(JsonObject json, String colorToGet) {
	JsonObject jsonColor = json.getJsonObject(colorToGet);
	double red = getDataAsDouble(jsonColor, JSON_RED);
	double green = getDataAsDouble(jsonColor, JSON_GREEN);
	double blue = getDataAsDouble(jsonColor, JSON_BLUE);
	double alpha = getDataAsDouble(jsonColor, JSON_ALPHA);
	Color loadedColor = new Color(red, green, blue, alpha);
	return loadedColor;
    }
    
    private void loadSize(JsonObject json, String size, M3Workspace workspace) {
        JsonObject jsonSize = json.getJsonObject(size);
        double x = getDataAsDouble(jsonSize, JSON_WIDTH);
        double y = getDataAsDouble(jsonSize, JSON_HEIGHT);
        workspace.getCanvas().setMinSize(x, y);
    }
    
    private JsonObject makeJsonColorObject(Color color) {
        if(color == null)
            return null;
        
	JsonObject colorJson = Json.createObjectBuilder()
		.add(JSON_RED, color.getRed())
		.add(JSON_GREEN, color.getGreen())
		.add(JSON_BLUE, color.getBlue())
		.add(JSON_ALPHA, color.getOpacity()).build();
	return colorJson;
    }
    
    private JsonObject makeJsonLabelObject(DraggableText draggableText) {
        if(draggableText == null)
            return null;
        
        double x = draggableText.getX();
        double y = draggableText.getY();
        boolean isBold = draggableText.isBold();
        boolean isItalic = draggableText.isItalic();
        boolean isLabel = draggableText.isLabel();
        boolean isStationLabel = draggableText.isStationLabel();
        String textValue = draggableText.getText();
        String fontType = draggableText.getFont().getFamily();
        double fontSize = draggableText.getFont().getSize();
        JsonObject fillColorJson = makeJsonColorObject((Color)draggableText.getFill());
        
        JsonObject labelJson = Json.createObjectBuilder()
                .add(JSON_X, x)
                .add(JSON_Y, y)
                .add(JSON_FONT_TYPE, fontType)
                .add(JSON_FONT_SIZE, fontSize)
                .add(JSON_IS_BOLD, isBold)
                .add(JSON_IS_ITALICIZED, isItalic)
                .add(JSON_TEXT_VALUE, textValue)
                .add(JSON_IS_LABEL, isLabel)
                .add(JSON_IS_STATION_LABEL, isStationLabel)
                .add(JSON_COLOR, fillColorJson).build();
        return labelJson;
    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    private String loadFilePath(JsonObject json, String filePathToGet) {
        String filePath = json.getString(filePathToGet);
        return filePath;
    }

    @Override
    public void exportData(AppDataComponent data, String filePath, String mapName) throws IOException {
        M3Data dataManager = (M3Data)data;

	// NOW BUILD THE JSON OBJCTS TO SAVE
	JsonArrayBuilder lineArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder stationArrayBuilder = Json.createArrayBuilder();
	ObservableList<Node> nodes = dataManager.getNodes();
	for (Node node : nodes) {
            if (node instanceof DraggableLine) {
                DraggableLine line = (DraggableLine)node;
                String name = line.getName();
                JsonObject color = makeJsonColorObject(line.getColor());
                JsonArrayBuilder stationsInLineBuilder = Json.createArrayBuilder();
                ArrayList<DraggableStation> stationsInLine = line.getStations();
                for(DraggableStation station:stationsInLine) {
                    if(station.getIsEndPoint())
                        continue;
                    stationsInLineBuilder.add(station.getLabel().getText());
                }
                boolean circular = line.isCircular();
                
                JsonObject lineJson = Json.createObjectBuilder()
                        .add("name", name)
                        .add("circular", circular)
                        .add("color", color)
                        .add("station_names", stationsInLineBuilder)
                        .build();
                lineArrayBuilder.add(lineJson);
            } else if(node instanceof DraggableStation) {
                DraggableStation station = (DraggableStation)node;
                //String type = station.getNodeType();
                String name = station.getLabel().getText();
                double x = station.getX();
                double y = station.getY();
                JsonObject stationJson = Json.createObjectBuilder()
                    .add(JSON_NAME, name)
                    .add(JSON_X, x)
                    .add(JSON_Y, y).build();
                stationArrayBuilder.add(stationJson);
            }
	}
	JsonArray stationsArray = stationArrayBuilder.build();
        JsonArray linesArray = lineArrayBuilder.build();
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add("name", mapName)
                .add("lines", linesArray)
		.add("stations", stationsArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
