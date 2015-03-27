
package uk.ac.liv.pepregexengine;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.map.TDoubleObjectMap;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.hash.TDoubleObjectHashMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import java.text.DecimalFormat;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 20-Mar-2015 13:20:35
 */
public class AAMap {

    private static final TObjectDoubleMap<String> aaMap = new TObjectDoubleHashMap<>();
    private static final TDoubleObjectMap<String> aaMapRev = new TDoubleObjectHashMap<>();
    private static final TDoubleObjectMap<String> regexAAMapRev = new TDoubleObjectHashMap<>();
    private static final TDoubleList aaMasses = new TDoubleArrayList();
    private static final TDoubleList regexAAMasses = new TDoubleArrayList();
    private static final DecimalFormat df = new DecimalFormat("#.####");

    // Class initialisation
    static {
        aaMap.put("A", 71.037114);
        aaMap.put("R", 156.101111);
        aaMap.put("N", 114.042927);
        aaMap.put("D", 115.026943);
        aaMap.put("C", 103.009185);
        aaMap.put("E", 129.042593);
        aaMap.put("Q", 128.058578);
        aaMap.put("G", 57.021464);
        aaMap.put("H", 137.058912);
        aaMap.put("I", 113.084064);
        aaMap.put("L", 113.084064);
        aaMap.put("K", 128.094963);
        aaMap.put("M", 131.040485);
        aaMap.put("F", 147.068414);
        aaMap.put("P", 97.052764);
        aaMap.put("S", 87.032028);
        aaMap.put("T", 101.047679);
        aaMap.put("U", 150.95363);
        aaMap.put("W", 186.079313);
        aaMap.put("Y", 163.06332);
        aaMap.put("V", 99.068414);
        aaMap.put("J", 113.084064);     //ambiguity code for I or L

        // make reverse aa map
        for (String aa : aaMap.keySet()) {
            double mz = aaMap.get(aa);
            aaMapRev.put(mz, aa);
        }

        // make aaMasses list
        for (double mz : aaMapRev.keys()) {
            aaMasses.add(mz);
        }

        for (int i = 0; i < aaMasses.size(); i++) {
            double firstMass = aaMasses.get(i);

            String firstRes = aaMapRev.get(firstMass);

            if (regexAAMapRev.containsKey(firstMass)) {
                String newPaired = regexAAMapRev.get(firstMass) + "|" + firstRes;
                regexAAMapRev.put(firstMass, newPaired);
            }
            else {
                regexAAMapRev.put(firstMass, firstRes);
            }

            for (int j = 0; j < aaMasses.size(); j++) {
                double secondMass = aaMasses.get(j);
                String secondRes = aaMapRev.get(secondMass);

                double pairedMass = Double.parseDouble(df.format(firstMass + secondMass));
                String paired = firstRes + secondRes;
                if (regexAAMapRev.containsKey(pairedMass)) {
                    String newPaired = regexAAMapRev.get(pairedMass) + "|" + paired;
                    regexAAMapRev.put(pairedMass, newPaired);
                }
                else {
                    regexAAMapRev.put(pairedMass, paired);
                }

                for (int k = 0; k < aaMasses.size(); k++) {
                    double thirdMass = aaMasses.get(k);
                    String thirdRes = aaMapRev.get(thirdMass);

                    String triplet = firstRes + secondRes + thirdRes;

                    double tripletMass = Double.parseDouble(df.format(firstMass + secondMass + thirdMass));
                    if (regexAAMapRev.containsKey(tripletMass)) {
                        String newRegex = regexAAMapRev.get(tripletMass) + "|" + triplet;
                        regexAAMapRev.put(tripletMass, newRegex);
                    }
                    else {
                        regexAAMapRev.put(tripletMass, triplet);
                    }
                }
            }

        }

        // make regexAAMasses
        for (double mz : regexAAMapRev.keys()) {
            regexAAMasses.add(mz);
        }

        aaMasses.sort();
        regexAAMasses.sort();
    }

    /**
     * @return the aaMap
     */
    public static TObjectDoubleMap<String> getAaMap() {
        return aaMap;
    }

    /**
     * @return the aaMapRev
     */
    public static TDoubleObjectMap<String> getAaMapRev() {
        return aaMapRev;
    }

    /**
     * @return the regexAAMapRev
     */
    public static TDoubleObjectMap<String> getRegexAAMapRev() {
        return regexAAMapRev;
    }

    /**
     * @return the aaMasses
     */
    public static TDoubleList getAaMasses() {
        return aaMasses;
    }

    /**
     * @return the regexAAMasses
     */
    public static TDoubleList getRegexAAMasses() {
        return regexAAMasses;
    }

    /**
     * @return the df
     */
    public static DecimalFormat getDf() {
        return df;
    }

}
