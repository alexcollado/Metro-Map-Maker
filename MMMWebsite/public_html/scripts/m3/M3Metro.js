/*
    M3Metro.js

    This JavaScript object represents a metro system and provides a means for finding a minimal
    transfer path from one station to another.
*/
function M3Metro() {
    // DEFAULT NAME OF METRO
    var NO_METRO_LOADED = "NO_METRO_LOADED";
    this.name = NO_METRO_LOADED;

    // THESE ARE USED FOR PASSING DATA TO THIS OBJECT TO INITIALIZE THE OBJECT
    this.NAME_DATA = "name";
    this.CIRCULAR_DATA = "circular";
    this.X_DATA = "x";
    this.Y_DATA = "y";

    /*
        init

        This function sets up the metro by resetting all the data structures such that they may
        be properly loaded from a file. Note that in order to setup this object properly the data
        provided to this function must be in a very specific format, specifically the linesData,
        stationsData, and connectionsData parameters. We can describe them as follows:

        linesData - a sequential array of line data where each line is represented by an associative
        array with two pieces of data: name and circular

        stationsData - a sequential array of station data where each station is represented by an`
        associative array with three pieces of data: name, x, and y

        connectionsData - a sequential array of connection data where each connection is represented
        by a sequential array with two pieces of data: line name and station name
    */
    this.init = function (initName, linesData, stationsData, connectionsData) {
        // THIS IS THE NAME OF THE METRO SYSTEM, PROBABLY THE NAME OF THE CITY
        // OR METROPOLITAN AREA IT IS FOUND IN
        this.name = initName;

        // FIRST ADD ALL THE LINES TO AN ASSOCIATIVE ARRAY
        this.lines = new Array();
        this.lineNames = new Array();
        for (var i = 0; i < linesData.length; i++) {
            var lineData = linesData[i];
            var lineName = lineData[this.NAME_DATA];
            var lineCircular = lineData[this.CIRCULAR_DATA];
            this.lines[lineName] = new M3Line(lineName, lineCircular);
            this.lineNames.push(lineName);
        }

        // THEN GET ALL THE LINE NAMES AND KEEP THEM SORTED
        this.lineNames.sort();
        this.numLines = this.lineNames.length;

        // NOW ADD ALL THE STATIONS TO AN ASSOCIATIVE ARRAY
        this.stations = new Array();
        this.stationNames = new Array();
        for (var i = 0; i < stationsData.length; i++) {
            var stationData = stationsData[i];
            var stationName = stationData[this.NAME_DATA];
            var stationX = stationData[M3.metro.X_DATA];
            var stationY = stationData[M3.metro.Y_DATA];
            this.stations[stationName] = new M3Station(stationName, stationX, stationY);
            this.stationNames.push(stationName);
        }

        // THEN GET ALL THE STATION NAMES AND KEEP THEM SORTED
        this.stationNames.sort();
        this.numStations = this.stationNames.length;

        // GET ALL THE CONNECTIONS AND HOOK UP ALL 
        for (var i = 0; i < connectionsData.length; i++) {
            var connectionData = connectionsData[i];
            var lineName = connectionData[0];
            var stationName = connectionData[1];
            var line = this.lines[lineName];
            var station = this.stations[stationName];
            line.addStation(station);
            station.addLine(line);
        }

        // NOW GO THROUGH ALL THE LINES AND TELL EACH ONE WHAT OTHER LINES
        // IT TRANSFERS TO. THIS PRECOMPUTING MAKES PATHFINDING CHEAPER TO
        // COMPUTE AND EASIER TO IMPLEMENT
        for (var i = 0; i < this.numLines; i++) {
            var lineName = this.lineNames[i];
            var line = this.lines[lineName];
            for (var j = 0; j < line.stations.length; j++) {
                var station = line.stations[j];
                for (var k = 0; k < station.lines.length; k++) {
                    var lines = station.lines;
                    var transferLine = lines[k];
                    line.addTransfer(transferLine);
                }
            }
        }
    }

    /*
        findMinimumTransferPath

        This function finds a minimum transfer path from the start station to the end station.
    */
    this.findMinimumTransferPath = function (startStationName, endStationName) {
        var startStation = this.stations[startStationName];
        var endStation = this.stations[endStationName];
        var linesToTest = new Array();
        var visitedLineNames = new Array();

        // THIS WILL COUNT HOW MANY TRANSFERS
        var numTransfers = 0;

        // THESE WILL BE PATHS THAT WE WILL BUILD TO TEST
        var testPaths = new Array();

        // START BY PUTTING ALL THE LINES IN THE START STATION
        // IN OUR linesToTest Array
        for (var i = 0; i < startStation.lines.length; i++) {
            var path = new M3Path(startStation, endStation);
            testPaths.push(path);
            path.addBoarding(startStation.lines[i], startStation);
        }

        var found = false;
        var morePathsPossible = true;
        var completedPaths = new Array();
        while (!found && morePathsPossible) {
            var updatedPaths = new Array();
            for (var i = 0; i < testPaths.length; i++) {
                var testPath = testPaths[i];

                // FIRST CHECK TO SEE IF THE DESTINATION IS ALREADY ON THE PATH
                if (testPath.hasLineWithStation(endStationName)) {
                    completedPaths.push(testPath);
                    found = true;
                    morePathsPossible = false;
                }
                else if (morePathsPossible) {
                    // GET ALL THE LINES CONNECTED TO THE LAST LINE ON THE TEST PATH
                    // THAT HAS NOT YET BEEN VISITED
                    var lastLine = testPath.tripLines[testPath.tripLines.length - 1];
                    for (var j = 0; j < lastLine.transferNames.length; j++) {
                        var testLineName = lastLine.transferNames[j];
                        var testLine = this.lines[testLineName];
                        if (!testPath.hasLine(testLineName)) {
                            var newPath = testPath.clone();
                            var intersectingStation = lastLine.findIntersectingStation(testLine);
                            newPath.addBoarding(testLine, intersectingStation);
                            updatedPaths.push(newPath);
                        }
                        // DEAD ENDS DON'T MAKE IT TO THE NEXT ROUND
                    }
                }
            }
            if (updatedPaths.length > 0) {
                testPaths = updatedPaths;
                numTransfers++;
            }
            else {
                morePathsPossible = false;
            }
        }
        // WAS A PATH FOUND?
        if (found) {
            var shortestPath = completedPaths[0];
            var shortestTime = shortestPath.calculateTimeOfTrip();
            for (var i = 1; i < completedPaths.length; i++) {
                var testPath = completedPaths[i];
                var timeOfTrip = testPath.calculateTimeOfTrip();
                if (timeOfTrip < shortestTime) {
                    shortestPath = testPath;
                    shortestTime = timeOfTrip;
                }
            }
            // WE NOW KNOW THE SHORTEST PATH, COMPLETE ITS DATA FOR EASY USE
            return shortestPath;
        }
        // NO PATH FOUND
        else {
            return null;
        }
    }
}