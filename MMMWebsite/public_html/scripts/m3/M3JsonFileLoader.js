/*
    M3JsonFileLoader.js

    This JavaScript object provides functions for loading M3 json data files.
*/
function M3JsonFileLoader() {

    /*
        loadMetroNames

        This will load the metroSystemsJsonPath file into the json object, which we
        will then use to extract all the metro system names.
    */
    this.loadMetroNames = function(metroSystemsJsonPath) {
        // LET jQuery RETRIEVE THE FILE
        $.getJSON(metroSystemsJsonPath, this.extractMetroNames);
    }

    /*
        extractMetroNames (helper for loadMetroNames)

        This extracts the data from the loaded JSON object.
    */
    this.extractMetroNames = function(json) {
        // LOAD ALL THE METRO NAMES FROM THE JSON FILE
        var metroSystemNames = new Array();
        for (var i = 0; i < json.metro_systems.length; i++) {
            metroSystemNames.push(json.metro_systems[i]);
        }

        // ALL THE METRO NAMES HAVE BEEN LOADED SO INVOKE THE CALLBACK TO USE IT
        M3.pageUpdater.updateMetroSystemNames(metroSystemNames);        
    }

    /*
        loadMetroData

        This function loads all the data from the selected metro into a M3Metro object.
    */
    this.loadMetroData = function(metroName, callback) {
        var jsonPath = M3.MAPS_PATH + metroName + "/" + metroName + M3.METRO_JSON_EXT;
        $.getJSON(jsonPath, function (json) {
            // GET ALL THE METRO INFO AND INIT THE METRO OBJECT WITH IT
            var name = json.name;
            var lineDatas = M3.fileLoader.extractLines(json);
            var stationDatas = M3.fileLoader.extractStations(json);
            var connectionDatas = M3.fileLoader.extractConnections(json);
            M3.metro.init(name, lineDatas, stationDatas, connectionDatas);

            // UPDATE THE UI
            M3.pageUpdater.updateTripToolbar(metroName);   
        });
    }

    /*
        extractLines (helper for loadMetroData)

        This function extracts the basic line information from the JSON file and
        builds and adds lines to the metro object. Note that it does not connect
        the lines to stations, however, as the stations do not yet exist.
    */
    this.extractLines = function(json) {
        // GO THROUGH AND EXTRACT ALL THE LINES FROM THE JSON FILE
        var linesArray = new Array();
        for (var i = 0; i < json.lines.length; i++) {
            var jsonLine = json.lines[i];            
            var lineData = new Array();
            lineData[M3.metro.NAME_DATA] = jsonLine.name;
            lineData[M3.metro.CIRCULAR_DATA] = jsonLine.circular;
            linesArray.push(lineData);
        }
        return linesArray;
    }
    
    /*
        extractStations (helper for loadMetroData)

        This function extracts and registers all the station.
    */
    this.extractStations = function(json) {
        // GO THROUGH AND EXTRACT ALL THE STATIONS FROM THE JSON FILE
        var stationsArray = new Array();
        for (var i = 0; i < json.stations.length; i++) {
            var stationData = new Array();
            var nameKey = M3.metro.NAME_DATA;
            var xKey = M3.metro.X_DATA;
            var yKey = M3.metro.Y_DATA;
            stationData[nameKey] = json.stations[i].name;
            stationData[xKey] = json.stations[i].x;
            stationData[yKey] = json.stations[i].y;
            stationsArray.push(stationData);
        }
        return stationsArray;
    }

    /*
        extractConnections (helper for loadMetroData)

        This function extracts and registers all the station-line connections.
    */
    this.extractConnections = function(json) {
        // WE'LL PUT ALL THE CONNECTIONS IN HERE, WHICH IS AN ARRAY OF TUPLES
        // REPRESENTING THE LINE NAME TO STATION NAME MAPPINGS
        var connections = new Array();

        // GO THROUGH AND EXTRACT ALL THE CONNECTIONS FROM THE JSON FILE
        for (var i = 0; i < json.lines.length; i++) {
            // GET THE LINE
            var jsonLine = json.lines[i];
            var lineName = jsonLine.name;

            // AND MUTUALLY CONNECT IT TO ALL ITS STATIONS
            for (var j = 0; j < jsonLine.station_names.length; j++) {
                var stationName = jsonLine.station_names[j];
                var connection = new Array();
                connection.push(lineName);
                connection.push(stationName);
                connections.push(connection);
            }
        }
        return connections;
    }    
}