/*
    M3PageUpdater.js
    
    This script is concerned with the manner with which an M3Metro
    system is used to update the MetroMapMaker page. Note that this is designed to be
    independent of how data is loaded from files into the M3Metro object and independent
    from how paths are found. So, we can change the look of the site in many ways and
    change how it operates by switching this out for a differnt script without having
    to change other scripts.
*/
function M3PageUpdater() {
    // ADD THESE CONSTANTS TO THE M3 OBJECT, THEY ARE CONCERNED WITH BUILDING METRO OPTIONS
    // SO THE USER CAN CHOOSE A METRO SYSTEM 
    M3.METRO_OPTION_START = "<option value='";
    M3.METRO_OPTION_CLOSE = "'>";
    M3.OPTION_CLOSE = "</option>";

    M3.INIT_METRO_HEADER = "M<sup>3</sup> - the Metro Map Maker";
    M3.INIT_METRO_TOOLBAR = "<strong>Metro Map:</strong><br />"
                    +       "<select id='metro_systems_combo_box' "
                    +       "onchange='M3.controller.requestSelectMetro(this.value)'>"
                    +       +"</select>";

    //  THIS IS THE MINIMAL TRIP TOOLBAR BEFORE STATIONS HAVE BEEN EXTRACTED AND LOADED
    M3.EMPTY_TRIP_TOOLBAR =    
                            "<span id='trip_toolbar'>"
                    +       " <br /><br /><strong>Find a Path: </strong><br />"
                    +       " <table>"
                    +       "  <tr>"
                    +       "   <td><strong>Starting Station:</strong></td>"
                    +       "   <td><select id='" + M3.START_STATION_COMBOBOX_ID + "'></select></td>"
                    +       "  </tr>"
                    +       "  <tr>"
                    +       "   <td><strong>Ending Station:</strong></td>"
                    +       "   <td><select id='" + M3.END_STATION_COMBOBOX_ID + "'></select></td>"
                    +       "  </tr>"
                    +       " </table><br />"
                    +       " <button id='" + M3.COMPUTE_PATH_BUTTON_ID + "' onclick='M3.controller.requestFindPath()'>"
                    +       "  Compute Minimum Transfer Path"
                    +       " </button><br />"
                    +       " <br />"
                    +       " <span id='" + M3.PATH_DESCRIPTION_ID + "'>"
                    +       " </span>"
                    +       "</span>";
    M3.MAPS_PATH = "./maps/";
    M3.MMM_IMAGE_PATH = "./img/MetroMapMaker.png";
    M3.METRO_PNG_EXT = " Metro.png";
    M3.METRO_JSON_EXT = " Metro.json";
    M3.SELECT_A_METRO = "Select a Metro";

    /*
        initPage

        This method adds the pieces to the page including the header and toolbar.
    */
    this.initPage = function() {
        // FIRST LOAD WHAT'S MISSING FROM THE INDEX PAGE
        var pageHeader = $("#" + M3.METRO_HEADER_ID);
        pageHeader.html(M3.INIT_METRO_HEADER);

        var metroToolbar = $("#" + M3.METRO_TOOLBAR_ID);
        metroToolbar.html(M3.INIT_METRO_TOOLBAR);
    }

    /* 
        updateMetroSystemNames

        This function is called by the file loading script after all the data is
        fully loaded into the M3Metro object. It takes the metroNames argument and places
        them into the page's combo box so that the user can choose one.
    */
    this.updateMetroSystemNames = function(metroNames) {
        // WE'LL USE jQuery TO GET THE COMBO BOX WHERE WE'LL PUT THE METRO SYSTEM NAMES
        var metrosComboBox = $("#" + M3.METRO_SYSTEMS_COMBOBOX_ID);

        // AND THIS WILL BE THE DEFAULT OPTION FOR WHEN THE PAGE FIRST LOADS, WHICH
        // IS NOT A METRO SYSTEM AT ALL, JUST INSTRUCTIONS
        var defaultOption = M3.METRO_OPTION_START + M3.SELECT_A_METRO + M3.METRO_OPTION_CLOSE 
                                + M3.SELECT_A_METRO + M3.OPTION_CLOSE;
        metrosComboBox.append(defaultOption);

        // NOW GO THROUGH ALL THE METRO SYSTEMS AND MAKE THEM CHOICES IN THE COMBO BOX
        for (var i = 0; i < metroNames.length; i++) {
            var selectedMetro = metroNames[i];
            var newOption = M3.METRO_OPTION_START + selectedMetro + M3.METRO_OPTION_CLOSE 
                                + selectedMetro + M3.OPTION_CLOSE;
            metrosComboBox.append(newOption);
        }
    }

    /*
        updateMetroImage

        This function updates the page's image to display that of the selected metro
        system. Note that it returns true if the selectedMetro turns out to be a 
        real metro and false if it's the default instructions, which means it will 
        display the default image and no further data loading should proceed.
    */
    this.updateMetroImage = function(selectedMetro) {
        // THIS IS THE APP IMAGE WE'RE GOING TO UPDATE
        var imageElement = document.getElementById(M3.METRO_MAP_IMG_ID);

        if (selectedMetro.localeCompare(SELECT_A_METRO) != 0) {
            // THE USER HAS SELECTED A VALID METRO, SO CHANGE THE IMAGE
            imageElement.src = M3.MAPS_PATH + selectedMetro + "/" + selectedMetro + M3.METRO_PNG_EXT;
            return true;
        }
        else {
            // IT'S NOT A METRO IT WILL BE THE DEFAULT
            imageElement.src = M3.METRO_MAP_MAKER_IMAGE_PATH;
            return false;
        }        
    }

    /*
        updateTripToolbar

        Called after a metro has been selected, it re-initializes the trip toolbar accordingly.
    */
    this.updateTripToolbar = function(selectedMetro) {
        if (selectedMetro.localeCompare(M3.SELECT_A_METRO) != 0) {
            // THE USER HAS SELECTED A VALID METRO, SO CHANGE THE IMAGE
            this.loadTripToolbar();
        }
        else {
            // IT'S NOT A METRO IT WILL BE THE DEFAULT
            this.removeTripToolbar();
        }
    }

    /*
        loadTripToolbar

        This function loads all of the stations from the m3Metro into the trip toolbar
        so that the user may find a path.
    */
    this.loadTripToolbar = function() {
        // IF THERE IS NO TOOLBAR, ADD IT
        var tripToolbar = $("#" + M3.TRIP_TOOLBAR_ID);
        if (!tripToolbar.length) {
            var metroToolbar = $("#" + M3.METRO_TOOLBAR_ID);
            metroToolbar.append(M3.EMPTY_TRIP_TOOLBAR);
        }
        // NOW ADD THE STATION NAMES
        var startCombo = $("#" + M3.START_STATION_COMBOBOX_ID);
        var endCombo = $("#" + M3.END_STATION_COMBOBOX_ID);
        for (var i = 0; i < M3.metro.stationNames.length; i++) {
            var stationName = M3.metro.stationNames[i];
            var option = M3.METRO_OPTION_START + stationName + M3.METRO_OPTION_CLOSE 
                            + stationName + M3.OPTION_CLOSE;
            startCombo.append(option);
            endCombo.append(option);
        }
    }

    /*
        removeTripToolbar

        This function restores the default toolbar which doesn't have any trip selection
        controls, which is what should be in the page when no metro is selected.
    */    
    this.removeTripToolbar = function() {
        // GET AND REMOVE THE ENTIRE TOOLBAR
        var tripToolbar = $("#" + M3.TRIP_TOOLBAR_ID);
        if (tripToolbar) {
            tripToolbar.remove();
        }
    }

    /*
        updatePathDescription

        This function will display a summary of the path for the user to read.
    */
    this.updatePathDescription = function(path, startStationName, endStationName) {
        // ONLY DO THIS IF THERE ACTUALLY IS A PATH
        if ((path != null) && (path !== undefined)) {
            var pathDiv = $("#" + M3.PATH_DESCRIPTION_ID);
            var text = "PATH FOUND<br /><br />";
            var line = path.tripLines[0];
            var i = 0;
            for (; i < path.tripLines.length; i++) {
                var line = path.tripLines[i];
                text += (i+1) + ". Board " + line.name + " at " + path.boardingStations[i].name + "<br />";
            }
            text += (i+1) + ". Disembark " + line.name + " at " + endStationName + "<br />";
            pathDiv.html(text);
        }
    }
}