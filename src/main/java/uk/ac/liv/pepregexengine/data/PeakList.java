
package uk.ac.liv.pepregexengine.data;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.map.TDoubleDoubleMap;
import gnu.trove.map.hash.TDoubleDoubleHashMap;
import gnu.trove.set.TDoubleSet;
import java.util.Map;
import uk.ac.liv.pepregexengine.utils.TDoubleDoubleMapConverter;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 30-Jul-2015 17:47:45
 */
public class PeakList {

    private final TDoubleDoubleMap peakListMap;

    public PeakList(Map<Double, Double> inputMap) {
        peakListMap = TDoubleDoubleMapConverter.convert(inputMap);
    }

    /**
     * Get a number of peaks with the largest intensity value among the whole list.
     *
     * @param topN the top intense peaks number
     *
     * @return TDoubleDoubleMap
     */
    public TDoubleDoubleMap getIntensePeaks(int topN) {

        //reverse Map
        TDoubleDoubleMap tRevMap = TDoubleDoubleMapConverter.reverse(peakListMap);

        TDoubleSet valueSet = tRevMap.keySet();

        TDoubleList valueList = new TDoubleArrayList(valueSet);

        //sort valueList in ascending order
        valueList.sort();

        //reverse Map again by value order
        TDoubleDoubleMap retMap = new TDoubleDoubleHashMap();
        int k = 0;
        if (valueList.size() > topN) {
            k = valueList.size() - topN;
            for (int i = valueList.size() - 1; i >= k; i--) {
                double value = valueList.get(i);
                double key = tRevMap.get(value);
                retMap.put(key, value);
            }
            return retMap;
        }
        else {
            return this.peakListMap;
        }

    }

    /**
     * Get the whole peak list map.
     *
     * @return TDoubleDoubleMap
     */
    public TDoubleDoubleMap getPeaks() {
        return this.peakListMap;
    }

}
