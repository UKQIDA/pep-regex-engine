
package uk.ac.liv.pepregexengine.utils;

import gnu.trove.map.TDoubleDoubleMap;
import gnu.trove.map.hash.TDoubleDoubleHashMap;
import gnu.trove.procedure.TDoubleDoubleProcedure;
import java.util.Map;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 25-Mar-2015 13:35:35
 */
public class TDoubleDoubleMapConverter {

    /**
     * Convert Map<Double, Double> to TDoubleDoubleMap.
     *
     * @param inputMap input Map<Double, Double>
     *
     * @return TDoubleDoubleMap
     */
    public static TDoubleDoubleMap convert(Map<Double, Double> inputMap) {
        TDoubleDoubleMap tMap = new TDoubleDoubleHashMap();
        if (inputMap != null) {
            inputMap.entrySet().stream().forEach((entry) -> {
                tMap.put(entry.getKey(), entry.getValue());
            });
        }
        return tMap;
    }

    /**
     * Reverse the key and value in TDoubleDoubleMap and create a reverse TDoubleDoubleMap.
     *
     * @param tMap input TDoubleTDoubleMap
     *
     * @return reversed TDoubleTDoubleMap
     */
    public static TDoubleDoubleMap reverse(TDoubleDoubleMap tMap) {
        final TDoubleDoubleMap retMap = new TDoubleDoubleHashMap();
        tMap.forEachEntry(new TDoubleDoubleProcedure() {

            @Override
            public boolean execute(double a, double b) {
                retMap.put(b, a);
                return true;
            }

        });
        return retMap;
    }

}
