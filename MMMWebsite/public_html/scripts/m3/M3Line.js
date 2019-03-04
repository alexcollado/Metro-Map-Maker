/*
    M3Line.js
    
    This script object represents a line on an M3Metro. Note that each line has references
    to the staitons on its line and it knows what lines it can transfer to. ALSO
    note that some lines are circular, which is important for pathfinding.
*/
function M3Line(initName, initCircular) {
    // HOLD ON TO THESE
    this.name = initName;
    this.circular = initCircular;

    // THESE WILL BE FILLED IN DURING THE LOADING PROCESS
    this.stations = new Array();        // ORDERED BY STOP
    this.stationNames = new Array();    // HASH TABLE
    this.transfers = new Array();       // HASH TABLE
    this.transferNames = new Array();   // ARRAY

    /*
        addStation

        This function adds a station to the line, which will append it to the end
        of the stations representing stops.
    */
    this.addStation = function (stationToAdd) {
        // WE ADD THE STATIONS IN THEIR STOP ORDER TO stations SUCH THAT
        // WE CAN PROPERLY GENERATE PATHS
        this.stations.push(stationToAdd);

        // AND WE ALSO PROVIDE A QUICK LOOKUP HASH TABLE FOR
        // WHEN WE NEED TO CHECK AND SEE IF A STATION IS ON A LINE
        this.stationNames[stationToAdd.name] = stationToAdd.name;
    }

    /*
        addTransfer

        This function adds a transfer line to this line, meaning one can connect
        to that line from this one.
    */
    this.addTransfer = function (transferLine) {
        if ((transferLine.name.localeCompare(this.name) != 0)
            && (!(this.transferNames.includes(transferLine.name)))) {
            this.transfers[transferLine.name] = transferLine;
            this.transferNames.push(transferLine.name);
        }
    }

    /*
        getStationIndex

        This function searches for the stationName argument in this line's list
        of stations and returns its index number.
    */
    this.getStationIndex = function (stationName) {
        // GO THROUGH ALL THE STATIONS
        for (var i = 0; i < this.stations.length; i++) {
            // AND FIND THE ONE WITH THE SAME NAME
            var testName = this.stations[i].name;
            var nameComparison = testName.localeCompare(stationName);
            if (nameComparison == 0) {
                return i;
            }
        }
        // NOT FOUND
        return -1;
    }

    /*
        hasStation

        This function searches the line and if it has a station named the same as
        the stationName argument it returns true, false otherwise.
    */
    this.hasStation = function (stationName) {
        return this.stationNames.hasOwnProperty(stationName);
    }
    
    /*
        findIntersectingStation

        This function finds and returns the station where this station intersects
        the intersectingLing argument.
    */
    this.findIntersectingStation = function (intersectingLine) {
        // GO TRHOUGH ALL THE STATIONS IN THIS LINE
        for (var i = 0; i < this.stations.length; i++) {
            var station1 = this.stations[i];
            var station1Name = station1.name;

            // FOUND IT
            if (intersectingLine.hasStation(station1Name)) {
                return station1;
            }
        }
        // THEY DON'T SHARE A STATION'
        return "none";
    }
}