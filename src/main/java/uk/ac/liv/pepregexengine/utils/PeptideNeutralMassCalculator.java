
package uk.ac.liv.pepregexengine.utils;

import uk.ac.liv.pepregexengine.AAMap;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 07-Jul-2015 13:54:56
 */
public class PeptideNeutralMassCalculator {

    /*
     * Calculates the mass of a peptide based on its amino acids only i.e. a b ion
     *
     */
    public static double getPeptideNeutralMass(String peptide) {
        double mass = 0.0;

        peptide = peptide.toUpperCase();
        peptide = peptide.trim();
        //System.out.println("Peptide:" + peptidePlusModString);

        /*
         * String[] temp = peptidePlusModString.split("_");
         * String peptide = "[" + temp[0] + "]";
         * String modString = temp[1];
         *
         */
        String[] aas = peptide.split("");
        for (String aa : aas) {
            if (aa.length() == 1) {
                mass += AAMap.getAaMap().get(aa);
            }
        }

        //System.out.println( " mr: " + mr); 
        return mass;

    }

}
