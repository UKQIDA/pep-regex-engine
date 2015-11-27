
package uk.ac.liv.pepregexengine.data;

import uk.ac.liv.pepregexengine.data.constants.Constants;
import gnu.trove.map.TDoubleDoubleMap;
import gnu.trove.map.hash.TDoubleDoubleHashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import uk.ac.ebi.pride.tools.mgf_parser.model.Ms2Query;

/**
 * The PRMSpectrum handling class.
 * Each PRMSpectrum contains a list of PRMPeak.
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 07-Aug-2015 18:03:10
 */
public class PRMSpectrum {

    private final List<PRMPeak> prmSpectrum;
    private double proteinMass;
    private DecimalFormat df = new DecimalFormat("#.##"); //Default is 2 decimal place.
    public static final Comparator<PRMPeak> MASS_DESCENDING
            = new Comparator<PRMPeak>() {

                @Override
                public int compare(PRMPeak p1, PRMPeak p2) {
                    return Double.valueOf(p2.getMass()).compareTo(p1.getMass());
                }

            };

    public static final Comparator<PRMPeak> MASS_ASCENDING
            = new Comparator<PRMPeak>() {

                @Override
                public int compare(PRMPeak p1, PRMPeak p2) {
                    return Double.valueOf(p1.getMass()).compareTo(p2.getMass());
                }

            };

    /**
     * Create prefix residue mass spectrum (PRM spectrum) with subset of peak list from Ms2Query peaklist
     * based on their intensity values (higher intensity first).
     * The output spectrum contains double of number plus 2 peaks after inserting reverse peaks, start peak and whole mass.
     *
     * @param query  Ms2Query from MgfReader
     * @param number number of peaks with large intensity values from original peak list
     */
    public PRMSpectrum(Ms2Query query, int number) {
        this.prmSpectrum = new ArrayList<>();

        int z = query.getPrecursorCharge();
        proteinMass = (query.getPrecursorMZ() * z) - (z * Constants.PROTON_MASS); //Neutral mass
        //String title = query.getTitle();

        TDoubleDoubleMap massToIntensity2dp = new TDoubleDoubleHashMap();     //This is for checking if we have duplication - use the mass at 4dp with higher intensity, add intensity

        PeakList peakList = new PeakList(query.getPeakList()); // Convert Map<Double, Double> to TDoubleDoubleMap

        // Work on the top number
        TDoubleDoubleMap topNPeakList = peakList.getIntensePeaks(number);

        prmSpectrum.add(new PRMPeak(Double.parseDouble(df.format(0.0)), 50.0, 0)); //origin 0.0, forward
        prmSpectrum.add(new PRMPeak(Double.parseDouble(df.format(Constants.CTERM + Constants.HYDROGEN_MASS)), 50.0, 1)); //origin C term group needed for finding y ion series, reverse
        prmSpectrum.add(new PRMPeak(Double.parseDouble(df.format(proteinMass)), 50.0, 1));// last y ion mass, reverse

        //Todo: check the following to FOR statements. What if pepMass=2500, mz=1250, then the intensity at 1250 will be double counted? 
        //Todo: It seems that the intensity will not be used in the output result.
        //first test for duplicate peaks, and include higher intensity ones
        //the mass in the peaklist is de-charged mass
        //Create forward PRM peak list
        for (double mz : topNPeakList.keys()) {

            //Todo: check if mz is +1 charged or other possible charged
            //Calculate mz (from mgf) and take the decimal formate set in df
            double decimalFormatMass = Double.parseDouble(df.format(mz - Constants.PROTON_MASS));

            //Get intensity value of mz
            double intensity = Double.parseDouble(df.format(topNPeakList.get(mz)));

            PRMPeak tempPeak = new PRMPeak(decimalFormatMass);
            //index of the first occurrence of the tempPeak in prmSpectrum, -1 if prmSpectrum doesn't contain the element.
            int ind = prmSpectrum.indexOf(tempPeak);
            double newIntensity = 50.0;
            if (ind != -1) {
                //get the specific PRM peak by removing it from prmSpectrum
                PRMPeak rmPeak = prmSpectrum.remove(ind);
                double oldIntensity = rmPeak.getIntensity();
                newIntensity = oldIntensity + intensity;
            }        

            prmSpectrum.add(new PRMPeak(decimalFormatMass, newIntensity, 0)); //Direction should be always 0 at this stage
        }

        //Now get the inverse mzs and check we are not duplicating
        //Create reverse PRM peak list
        for (double mz : topNPeakList.keys()) {

            //Calculate mz (from mgf) and take the decimal formate set in df
            double inverseFormatMass = Double.parseDouble(df.format(proteinMass - (mz - Constants.PROTON_MASS)));
            
            //Get intensity value of mz
            double intensity = Double.parseDouble(df.format(topNPeakList.get(mz)));

            if (massToIntensity2dp.containsKey(inverseFormatMass)) {
                intensity += massToIntensity2dp.get(inverseFormatMass);
            }
            PRMPeak tempPeak = new PRMPeak(inverseFormatMass);
            //index of the first occurrence of the tempPeak in prmSpectrum, -1 if prmSpectrum doesn't contain the element.
            int ind = prmSpectrum.indexOf(tempPeak);
            double newIntensity = 50.0;
            if (ind != -1) {
                PRMPeak oldPeak = prmSpectrum.remove(ind);
                if (oldPeak.getDirection() != 1) { //if the direction of oldPeak is not reverse (it could be either 2 or 0), then new direction will be both (value 2).
                    tempPeak.setDirection(2);
                }
                else {
                    tempPeak.setDirection(1); //unneccessary?
                }
                double oldIntensity = oldPeak.getIntensity();
                newIntensity = oldIntensity + intensity;
                
            }
         
            prmSpectrum.add(new PRMPeak(inverseFormatMass, newIntensity, tempPeak.getDirection()));
        }

        Collections.sort(prmSpectrum, MASS_DESCENDING);
    }

    /**
     * Create prefix residue mass spectrum (PRM spectrum) with entire peak list from Ms2Query peaklist.
     * The output spectrum contains double number of the peaklist plus 2 peaks after inserting reverse peaks, start peak and whole mass.
     *
     * @param query Ms2Query from MgfReader
     */
    /*
     * Following MS-Align+ method we create the PRM spectrum from Ms2Query (of MGF file)
     * Currently set to work at 2dp - we might want to use something more complicated to check for same peak at 2dp, then use higher resolution for most intense peak
     */
    public PRMSpectrum(Ms2Query query) {
        this(query, query.getPeakList().size());
    }

    /**
     * @return the prmSpectrum
     */
    public List<PRMPeak> getPrmPeaks() {
        return prmSpectrum;
    }

    /**
     * Temporary helping function. 
     * Write spectrum matrix in csv file.
     *
     * @param fileName output file name
     *
     * @throws java.io.IOException
     */
    public void writePRMSpectrum(String fileName)
            throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("prefix residue mass, intensity, direction\n");

            List<PRMPeak> tempPeaks = this.getPrmPeaks();
            Collections.sort(tempPeaks, MASS_ASCENDING);

            for (PRMPeak outterP : tempPeaks) {
                String currentRow = outterP.getMass() + ", " + outterP.getIntensity() + ", " + decodeDirection(outterP.getDirection());
                for (int i = 0; i < tempPeaks.size(); i++) {
                    PRMPeak innerP = tempPeaks.get(i);
                    if ((outterP.getMass() - innerP.getMass() >= 0.0)) {
                        currentRow = currentRow + ", " + String.valueOf(outterP.getMass() - innerP.getMass());
                    }
                    else {
                        currentRow = currentRow + ", ";
                    }
                }
                currentRow = currentRow + "\n";
                writer.write(currentRow);
            }
        }
    }

    private String decodeDirection(int dir) {
        switch (dir) {
            case 0:
                return "forward";
            case 1:
                return "reverse";
            case 2:
                return "both";
            default:
                return "unrecognised";
        }

    }

    /**
     * The method to get df.
     *
     * @return the df
     */
    public DecimalFormat getDf() {
        return df;
    }

    /**
     * The method to set df.
     * Use this method to change the default DecimailFormat df which is 2 decimal place to any other decimal format.
     *
     * @param df the df to set
     */
    public void setDf(DecimalFormat df) {
        this.df = df;
    }

}
