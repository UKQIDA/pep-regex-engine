
package uk.ac.liv.pepregexengine.data;

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
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 07-Aug-2015 18:03:10
 */
public class PRMSpectrum {

    private final List<PRMPeak> prmSpectrum;
    private double proteinMass;
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
     * Create a subset of prefix residue mass spectrum with based on peak intensity values (large first).
     * The output spectrum contains double of number plus 2 peaks after insert reverse peaks, start peak and whole mass.
     *
     * @param query  Ms2Query from mgf reader
     * @param number number of peaks with large intensity values from original peak list
     */
    public PRMSpectrum(Ms2Query query, int number) {
        this.prmSpectrum = new ArrayList<>();

        int z = query.getPrecursorCharge();
        proteinMass = (query.getPrecursorMZ() * z) - (z * Constants.PROTON_MASS);
        //String title = query.getTitle();

        //System.out.println("Spectrum " + title + " mz: " + pepMZ + " mass: " + pepMass);
        // + CTERM + HMASS;
        //TDoubleDoubleMap massToIntensity4dp = new TDoubleDoubleHashMap();     //This will form the spectrum
        TDoubleDoubleMap massToIntensity2dp = new TDoubleDoubleHashMap();     //This is for checking if we have duplication - use the mass at 4dp with higher intensity, add intensity

        PeakList peakList = new PeakList(query.getPeakList()); // Convert Map<Double, Double> to TDoubleDoubleMap
        TDoubleDoubleMap topNPeakList = peakList.getIntensePeaks(number);
        DecimalFormat df2dp = new DecimalFormat("#.##");

        prmSpectrum.add(new PRMPeak(Double.parseDouble(df2dp.format(0.0)), 50.0, 0)); //origin 0.0, forward
        prmSpectrum.add(new PRMPeak(Double.parseDouble(df2dp.format(Constants.CTERM + Constants.HYDROGEN_MASS)), 50.0, 1)); //origin C term group needed for finding y ion series, reverse
        prmSpectrum.add(new PRMPeak(Double.parseDouble(df2dp.format(proteinMass)), 50.0, 1));// last y ion mass, reverse

        massToIntensity2dp.put(Double.parseDouble(df2dp.format(0.0)), 50.0); //origin 0.0
        massToIntensity2dp.put(Double.parseDouble(df2dp.format(Constants.CTERM + Constants.HYDROGEN_MASS)), 50.0); //origin C term group needed for finding y ion series
        massToIntensity2dp.put(Double.parseDouble(df2dp.format(proteinMass)), 50.0);

        //Todo: check the following to FOR statements. What if pepMass=2500, mz=1250, then the intensity at 1250 will be double counted? 
        //Todo: It seems that the intensity will not be used in the output result.
        //first test for duplicate peaks, and include higher intensity ones
        //the mass in the peaklist is charge deconvoluted mass
        for (double mz : topNPeakList.keys()) {

            //Todo: check if mz is +1 charged or other possible charged
            double formatMass2dp = Double.parseDouble(df2dp.format(mz - Constants.PROTON_MASS));
            double intensity = Double.parseDouble(df2dp.format(topNPeakList.get(mz)));

            if (massToIntensity2dp.containsKey(formatMass2dp)) {
                intensity += massToIntensity2dp.get(formatMass2dp);
            }
            PRMPeak tempPeak = new PRMPeak(formatMass2dp);
            if (prmSpectrum.contains(tempPeak)) {
                prmSpectrum.remove(tempPeak);
                tempPeak.setDirection(0);
                tempPeak.setIntensity(intensity);
                prmSpectrum.add(tempPeak);
            }
            massToIntensity2dp.put(formatMass2dp, intensity);
            prmSpectrum.add(new PRMPeak(formatMass2dp, intensity, 0));
        }

        //Now get the inverse mzs and check we are not duplicating
        for (double mz : topNPeakList.keys()) {

            double inverseMass2dp = Double.parseDouble(df2dp.format(proteinMass - (mz - Constants.PROTON_MASS)));
            double intensity = Double.parseDouble(df2dp.format(topNPeakList.get(mz)));

            if (massToIntensity2dp.containsKey(inverseMass2dp)) {
                intensity += massToIntensity2dp.get(inverseMass2dp);
            }
            PRMPeak tempPeak = new PRMPeak(inverseMass2dp);
            if (prmSpectrum.contains(tempPeak)) {
                PRMPeak oldPeak = prmSpectrum.get(prmSpectrum.indexOf(tempPeak));
                if (oldPeak.getDirection() != 1) { //if the direction of oldPeak is not reverse, then new direction will be both.
                    tempPeak.setDirection(2);
                }
                else {
                    tempPeak.setDirection(1);
                }
                prmSpectrum.remove(tempPeak);
                tempPeak.setIntensity(intensity);
                prmSpectrum.add(tempPeak);
            }
            massToIntensity2dp.put(inverseMass2dp, intensity);
            prmSpectrum.add(new PRMPeak(inverseMass2dp, intensity, 1));
        }

//        for (double mass : massToIntensity2dp.keys()) {
//            //newSpectrum.add(mz);
//            this.prmSpectrum.add(mass); //Change to make this a mass spectrum rather than MH+ spectrum
//        }
//
//        this.prmSpectrum.sort();
        Collections.sort(prmSpectrum, MASS_DESCENDING);
    }

    /*
     * Following MS-Align+ method we create the PRM spectrum
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
     *
     * @param fileName output file name
     *
     * @throws java.io.IOException
     */
    public void writeDetail(String fileName)
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

}
