/*
    M3Station.js

    This JavaScript object represents a station in a metro system. Note that stations
    have a name and x, y location in the map and they know what lines they are on.
*/
function M3Station(initName, initX, initY) {
    this.name = initName;
    this.x = initX;
    this.y = initY;
    this.lines = new Array();
    this.lineNames = new Array();

    this.addLine = function (lineToAdd) {
        // WE CAN ADD EACH LINE TO EACH STATION SO WE CAN ITERATE
        // THROUGH THEM WITH A COUNTING VARIABLE
        this.lines.push(lineToAdd);

        // AND WE CAN ALSO PROVIDE A QUICK LOOKUP HASH TABLE FOR
        // WHEN WE NEED TO CHECK AND SEE IF A STATION CONNECTS TO A LINE
        this.lineNames[lineToAdd.name] = lineToAdd.name;
    }
}