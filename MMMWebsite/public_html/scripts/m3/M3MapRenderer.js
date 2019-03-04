/*
    M3MapRenderer.js

    This script is responsible for rendering the map image and any paths
    on top of it to the canvas.
*/
function M3MapRenderer() {
    // THIS IS THE HTML5 CANVAS THAT WE'LL RENDER TO'
    M3.canvas2D = M3.mapCanvas.getContext("2d");

    // THIS IS THE IMAGE THAT WILL STORE THE METRO MAP
    M3.mapImage = new Image();

    // THE DEFAULT VALUES USED BY THIS SCRIPT
    M3.DEFAULT_MAP_IMAGE = "./img/MetroMapMaker.png";
    M3.DEFAULT_START_COLOR = "#00FF0088";
    M3.DEFAULT_END_COLOR   = "#FF000088";
    M3.DEFAULT_PATH_LINE_COLOR = "#FFFF0088";
    M3.DEFAULT_PATH_STATION_COLOR  = "#FF884488";
    M3.DEFAULT_STATION_RADIUS = 10;
    M3.DEFAULT_LINE_WIDTH = 20;

    /*
        renderMap
        
        This function renders the map image to the canvas.
    */
    this.renderMap = function() {
        // CLEAR THE CANVAS
        M3.canvas2D.clearRect(0, 0, M3.canvasWidth, M3.canvasHeight);

        // DRAW THE MAP IMAGE
        M3.canvas2D.drawImage(M3.mapImage, 0, 0);
    }

    /*
        renderPath

        This function would be optionally called after rendering the map and would draw
        a metro path on top of the map to highlight a route for one to take.
    */
    this.renderPath = function(path) {
        // ONLY DRAW A PATH IF THERE IS ONE
        if ((path !== undefined) && (path !== null)) {
            // THESE ARE THE STATIONS IN THE TRIP
            var tripStations = path.getTripStations();

            // FIRST THE LINES
            M3.canvas2D.lineWidth = M3.DEFAULT_LINE_WIDTH;
            M3.canvas2D.beginPath();
            M3.canvas2D.moveTo(tripStations[0].x, tripStations[0].y);
            for (var i = 1; i < tripStations.length; i++) {
                M3.canvas2D.lineTo(tripStations[i].x, tripStations[i].y);
            }
            M3.canvas2D.strokeStyle = M3.DEFAULT_PATH_LINE_COLOR;
            M3.canvas2D.stroke();

            // THEN HIGHLIGHT THE START STATION
            var startStationName = path.startStation.name;
            this.highlightStation(M3.metro.stations[startStationName], M3.DEFAULT_START_COLOR);

            // HIGHLIGHT THE STATIONS IN THE PATH, IF THERE IS ONE
            for (var i = 1; i < tripStations.length-1; i++) {
                var station = tripStations[i];
                this.highlightStation(station, M3.DEFAULT_PATH_STATION_COLOR);
            }

            // HIGHLIGHT THE END STATION
            var endStationName = path.endStation.name;
            this.highlightStation(M3.metro.stations[endStationName], M3.DEFAULT_END_COLOR);
        }
    }

    /*
        highlightStation

        This function draws a filled circle on top of a station in the metro
        so as to highlight it.
    */
    this.highlightStation = function(station, color) {
        var x = station.x;
        var y = station.y;
        M3.canvas2D.beginPath();
        M3.canvas2D.arc(x, y, M3.DEFAULT_STATION_RADIUS, 0, 2*Math.PI, false);
        M3.canvas2D.fillStyle = color;
        M3.canvas2D.fill();
    }

    /*
        updateMap

        This function changes the map to be rendered to the canvas
    */
    this.updateMap = function(imagePath) {
        // THIS IS THE CALLBACK FUNCTION FOR WHEN THE IMAGE STARTS LOADING
        M3.mapImage.onload = function() {
            // UPDATE THE SIZE OF THE CANVAS
            M3.mapCanvas.width = M3.mapImage.width;
            M3.mapCanvas.height = M3.mapImage.height;

            // AND RENDER WITH THE NEW MAP
            M3.mapRenderer.renderMap();
        };
        // THIS ACTUALLY STARTS THE IMAGE LOADING
        M3.mapImage.src = imagePath;   
    }

    // RENDER THE DEFAULT IMAGE
    this.updateMap(M3.DEFAULT_MAP_IMAGE);
}