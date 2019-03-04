/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.data;

/**
 *
 * @author alexc
 */
public enum M3State {
    INSERTED_LINE,
    REMOVED_LINE,
    EDITED_LINE,
    INSERTED_STATION,
    REMOVED_STATION,
    EDITED_STATION,
    DRAGGING_STATION,
    DRAGGING_LINE_END,
    DRAGGING_IMAGE,
    DRAGGING_TEXT,
    ADD_STATION_MODE,
    REMOVE_STATION_MODE,
    SELECTED_TEXT,
    NEUTRAL
}
