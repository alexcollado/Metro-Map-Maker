/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author alexc
 */
public class StationGraph extends AbstractEdgeWeightedGraph<DraggableStation> {
    
    /**
     * Set of all the stations in this StationGraph.
     */
    private final HashSet<DraggableStation> stationSet;
       
    /**
     * Map taking a station to a set of all the stations accessible from it by
     * inserting or deleting a single station.
     */
    private final HashMap<DraggableStation, Set<DraggableStation>> stationMap;
    
    /**
     * Initialize a WordGraph given an iterator to produce all the words.
     * 
     * @param words a collection of all the words.
     */
    public StationGraph(Iterator<DraggableStation> stations) {
        stationSet = new HashSet<>();
        stationMap = new HashMap<>();
        while(stations.hasNext()) {
            DraggableStation w = stations.next();
            stationSet.add(w);
        }
        for(DraggableStation station : stationSet) {
            for(int i = 0; i < stationSet.size(); i++) {
                for(int j = 0; j < 25; j++) {
                    DraggableStation w = station;
                    if(stationSet.contains(w)) {
                        addToMap(station, w);
                        addToMap(w, station);
                    }
                }
            }
        }
    }
    
    /**
     * Insert a pair of words in the map.
     * 
     * @param from The word to be used as the key.
     * @param to The word to be added to the list of words associated
     * with the key.
     */
    private void addToMap(DraggableStation from, DraggableStation to) {
        Set<DraggableStation> s = stationMap.get(from);
        if(s == null) {
            s = new HashSet<>();
            stationMap.put(from, s);
        }
        s.add(to);
    }
    
    @Override
    public boolean containsNode(DraggableStation word) {
        return stationSet.contains(word);
    }
    
    @Override
    public boolean isAdjacent(DraggableStation from, DraggableStation to) {
        Set<DraggableStation> s = stationMap.get(from);
        if(s == null)
            return false;
        else
            return s.contains(to);
    }

    @Override
    public Iterator<DraggableStation> nodeIterator(DraggableStation from) {
        if(from == null)
            return(stationSet.iterator());
        else {
            Set<DraggableStation> l = stationMap.get(from);
            if(l == null)
                l = new HashSet<>();
            return l.iterator();
        }
    }
    
    @Override
    public double edgeWeight(DraggableStation from, DraggableStation to) {
        if(isAdjacent(from, to))
            return from.compareTo(to);
        else
            return Double.POSITIVE_INFINITY;
    }

}
