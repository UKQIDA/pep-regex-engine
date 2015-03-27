
package uk.ac.liv.pepregexengine.utils;

import gnu.trove.map.TDoubleDoubleMap;
import gnu.trove.map.hash.TDoubleDoubleHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 25-Mar-2015 13:35:35
 */
public class TDoubleDoubleMapConverter {

    public static TDoubleDoubleMap convert(Map<Double, Double> inputMap) {
        TDoubleDoubleMap tMap = new TDoubleDoubleHashMap();
        if (inputMap != null) {
            for (Entry<Double, Double> entry : inputMap.entrySet()) {
                tMap.put(entry.getKey(), entry.getValue());
            }
        }
        return tMap;
    }

}
