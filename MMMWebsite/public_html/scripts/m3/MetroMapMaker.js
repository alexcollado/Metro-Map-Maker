/*
    MetroMapMaker.js

    By including this script one is adding the MetroMapMaker to the window as
    a global variable. Note that there are two ways to use it, either by
    only referencing it in an html file, in which case one mush load it
    dynamically, or by including all the M3 scripts it uses in the html file,
    in which case one must call setupMMM directly. Note that the second
    technique is easier for debugging purposes because one can more easily 
    put breakpoints in scripts.
*/
(function(window){
    // WE WANT TO KNOW ABOUT ALL ERRORS
    'use strict';

    /*
        define_M3

        This function is called once, at startup, when JavaScript file is included in a Web page. Upon being called it simply reserves an M3 namespace globally in the window object. One can then get it rolling either by including
        all the other html files in the html file or by dynamically loading them.
    */
    function define_M3() {
        // WE DON'T HAVE ANY INITIAL DATA FOR THE M3 OBJECT
        var M3 = {};
        
        // THESE CONSTANTS ARE CONCERNED WITH GETTING PAGE ELEMENTS TO UPDATE THEM
        M3.METRO_HEADER_ID = "metro_header";
        M3.METRO_TOOLBAR_ID = "metro_toolbar";
        M3.TRIP_TOOLBAR_ID = "trip_toolbar";
        M3.PATH_DESCRIPTION_ID = "path_list";
        M3.METRO_SYSTEMS_COMBOBOX_ID = "metro_systems_combo_box";
        M3.START_STATION_COMBOBOX_ID = "start_station_combobox";
        M3.END_STATION_COMBOBOX_ID = "end_station_combobox";
        M3.COMPUTE_PATH_BUTTON_ID = "compute_path_button";
        M3.METRO_MAP_IMG_ID = "metro_map_image";
        M3.METRO_MAP_IMAGE_CANVAS_ID = "metro_map_image_canvas";

        // THIS IS THE LIST OF ALL METRO SYSTEMS
        M3.METRO_SYSTEMS_LIST_FILE_PATH = "./maps/MetroSystems.json";

        /*
            loadMMMDynamically

            This function prepares the Metro Map Maker Web app for use by initializing
            all the needed components and setting them up for use.
        */
        M3.loadMMMDynamically = function() {
            // NOTE THAT WE NEED TO LOAD THESE IN THE CORRECT DEPENDENCY ORDER
            var scriptsToLoad = [
                "scripts/m3/M3Metro.js",
                "scripts/m3/M3JsonFileLoader.js",
                "scripts/m3/M3PageUpdater.js",
                "scripts/m3/M3Controller.js",
                "scripts/m3/M3Renderer.js"
            ];
            M3.loadScripts(scriptsToLoad, 0, setupMMM);
        }

        /*
            loadScripts

            This function helps to dynamically load all the scripts needed by this application.
        */
        M3.loadScripts = function(scriptsArray, index, callback) {
            if (index >= scriptsArray.length) {
                callback();
                return;
            }
            else {
                index++;
                $.getScript(scriptsArray[index-1], function(){M3.loadScripts(scriptsArray, index, callback)});
            }
        }

        /*
            setupMMM
        
            This callback function completes the setup of the application.
        */
        M3.setupMMM = function() {
            // GET THE CANVAS FOR THE MAP
            M3.mapCanvas = document.getElementById(M3.METRO_MAP_IMAGE_CANVAS_ID);

            // INIT AN EMPTY METRO
            M3.metro = new M3Metro();

            // THIS WILL MANAGE HOW METRO DATA UPDATES THE DISPLAY
            M3.pageUpdater = new M3PageUpdater();
            M3.pageUpdater.initPage();

            // THIS WILL MANAGE HOW METRO DATA IS LOADED FROM FILES
            M3.fileLoader = new M3JsonFileLoader();

            // LOAD THE LIST OF METROS TO SET THINGS IN MOTION. NOTE THAT
            // THIS FUNCTION IS PROVIDING
            M3.fileLoader.loadMetroNames(M3.METRO_SYSTEMS_LIST_FILE_PATH, M3.pageUpdater.loadMetroSystemNamesCallback);

            // AND THEN INIT THE CONTROLLER
            M3.controller = new M3Controller();

            // AND THE SCRIPT DOING THE MAP RENDERING
            M3.mapRenderer = new M3MapRenderer();
        }

        // AND RETURN THE NEWLY DEFINED OBJECT
        return M3;
    }

    // DEFINE M3 TO BE A GLOBALLY AVAILBLE OBJECT
    if (typeof (M3) === 'undefined') {
        window.M3 = define_M3();
    }
    else {
        console.log("M3L already defined.");
    }
})(window);