/*
    M3Controller

    This JavaScript object serves as the controller for the Metro Map Maker, providing
    the appropriate programmed response for each user interaction.
*/
function M3Controller(initMMM) {
    // AFTER EVENTS HAPPEN WE'LL LOAD DATA, FORCE RENDERING AND PAGE UPDATING
    this.mmm = initMMM;

    /*
        requestSelectMetro

        This function is called when the user chooses one of the metro systems from the
        metro map combo box. It will load all the data necessary for that metro and then
        render it to the canvas.
    */
    this.requestSelectMetro = function (selectedMetroName) {
        // UPDATE THE PAGE'S IMAGE
        var metroIsSelected = selectedMetroName.localeCompare(M3.SELECT_A_METRO) != 0;
        if (metroIsSelected) {
            // THE USER HAS SELECTED A VALID METRO, SO LOAD THE METRO DATA
            M3.fileLoader.loadMetroData(selectedMetroName);

            // AND LOAD THE MAP IMAGE AND RENDER
            var imagePath = M3.MAPS_PATH + selectedMetroName + "/" + selectedMetroName + M3.METRO_PNG_EXT;
            M3.mapRenderer.updateMap(imagePath);
        }
        else {
            // USE THE DEFAULT SITE IMAGE
            M3.mapRenderer.updateMap(M3.DEFAULT_MAP_IMAGE);

            // AND REMOVE THE PATH TOOLBAR
            M3.pageUpdater.removeTripToolbar();
        }
    }

    /*
        requestFindPath

        This function is called when the user asks to find a path from one station to
        another in the currently loaded metro system. It responds by computing the path
        and then highlighting it on the map.
    */
    this.requestFindPath = function () {
        // GET THE START AND END STATIONS FROM THE COMBO BOXES
        var startStationName = document.getElementById(M3.START_STATION_COMBOBOX_ID).value;
        var endStationName = document.getElementById(M3.END_STATION_COMBOBOX_ID).value;        

        // COMPUTE THE PATH
        var path = M3.metro.findMinimumTransferPath(startStationName, endStationName);

        // UPDATE THE PATH DESCRIPTION
        M3.pageUpdater.updatePathDescription(path, startStationName, endStationName);

        // AND UPDATE THE MAP
        M3.mapRenderer.renderMap();
        M3.mapRenderer.renderPath(path);
    }
}