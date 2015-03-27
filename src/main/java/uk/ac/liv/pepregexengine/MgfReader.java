
package uk.ac.liv.pepregexengine;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.map.TDoubleDoubleMap;
import gnu.trove.map.hash.TDoubleDoubleHashMap;
import java.io.File;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;
import uk.ac.ebi.pride.tools.mgf_parser.MgfFile;
import uk.ac.ebi.pride.tools.mgf_parser.model.Ms2Query;
import uk.ac.liv.pepregexengine.utils.TDoubleDoubleMapConverter;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 24-Mar-2015 18:23:04
 */
public class MgfReader {

    private static TDoubleList prmSpectrum;

    /**
     * @return the prmSpectrum
     */
    public static TDoubleList getPrmSpectrum() {
        return prmSpectrum;
    }

    public MgfReader(File file)
            throws JMzReaderException {
        loadSpectraFromFile(file);
    }

    private void loadSpectraFromFile(File f)
            throws JMzReaderException {

        MgfFile mgfFile = new MgfFile(f);
        Iterator<Ms2Query> it = mgfFile.getMs2QueryIterator();
        Set<String> processedIds = new HashSet<>(); // record the value of TITLE

        while (it.hasNext()) {
            Ms2Query query = it.next();

            // make sure every spectrum is only used once
            if (processedIds.contains(query.getTitle())) {
                continue;
            }

            processedIds.add(query.getTitle());

            // set the intensity to 1 in case it's missing
            if (query.getPeptideIntensity() == null) {
                query.setPeptideIntensity(1.0);
            }

            // throw an exception in case it's missing
            if (query.getPrecursorCharge() == null) {
                throw new IllegalStateException("Spectrum is missing precursor charge.");
            }

            // change the id to title
            // ClusteringSpectrum spectrum = new ClusteringSpectrum(query.getTitle(),query.getPrecursorMZ(),query.getPrecursorIntensity(),query.getPrecursorCharge(),query.getPeakList(),null,query.getMsLevel());
            double dummyPrecursorIntensity = 1.0;
            int msLevel = 2;

            if (query.getPeakList() != null) {
                int z = query.getPrecursorCharge();
                double pepMass = (query.getPrecursorMZ() * z) - (z * Constants.PROTON_MASS);
                prmSpectrum = createPRMSpectrum(query);

            }
            else {
                //System.out.println("\n\n\tNull peak list: " + query.getTitle());
            }
        }
    }

    /*
     * Following MS-Align+ method we create the PRM spectrum
     * Currently set to work at 2dp - we might want to use something more complicated to check for same peak at 2dp, then use higher resolution for most intense peak
     */
    private TDoubleList createPRMSpectrum(Ms2Query query) {

        int z = query.getPrecursorCharge();
        double pepMass = (query.getPrecursorMZ() * z) - (z * Constants.PROTON_MASS);
        //String title = query.getTitle();

        //System.out.println("Spectrum " + title + " mz: " + pepMZ + " mass: " + pepMass);
        // + CTERM + HMASS;
        TDoubleList newSpectrum = new TDoubleArrayList();

        //TDoubleDoubleMap massToIntensity4dp = new TDoubleDoubleHashMap();     //This will form the spectrum
        TDoubleDoubleMap massToIntensity2dp = new TDoubleDoubleHashMap();     //This is for checking if we have duplication - use the mass at 4dp with higher intensity, add intensity

        TDoubleDoubleMap peakList = TDoubleDoubleMapConverter.convert(query.getPeakList());
        DecimalFormat df4dp = new DecimalFormat("#.####");
        DecimalFormat df2dp = new DecimalFormat("#.##");

        massToIntensity2dp.put(Double.parseDouble(df2dp.format(0.0)), 50.0); //origin 0.0
        massToIntensity2dp.put(Double.parseDouble(df2dp.format(Constants.CTERM + Constants.HYDROGEN_MASS)), 50.0); //origin C term group needed for finding y ion series
        massToIntensity2dp.put(Double.parseDouble(df2dp.format(pepMass)), 50.0); //origin C term group needed for finding y ion series

        //first test for duplicate peaks, and include higher intensity ones
        for (double mz : peakList.keys()) {

            double formatMass2dp = Double.parseDouble(df2dp.format(mz - Constants.PROTON_MASS));
            double intensity = Double.parseDouble(df4dp.format(peakList.get(mz)));

            if (massToIntensity2dp.containsKey(formatMass2dp)) {
                intensity += massToIntensity2dp.get(formatMass2dp);
            }
            massToIntensity2dp.put(formatMass2dp, intensity);
        }

        //Now get the inverse mzs and check we are not duplicating
        for (double mz : peakList.keys()) {

            double inverseMass = Double.parseDouble(df2dp.format(pepMass - (mz - Constants.PROTON_MASS)));
            double intensity = Double.parseDouble(df2dp.format(peakList.get(mz)));

            if (massToIntensity2dp.containsKey(inverseMass)) {
                intensity += massToIntensity2dp.get(inverseMass);
            }
            massToIntensity2dp.put(inverseMass, intensity);
        }

        for (double mass : massToIntensity2dp.keys()) {
            //newSpectrum.add(mz);
            newSpectrum.add((mass)); //Change to make this a mass spectrum rather than MH+ spectrum
        }

        newSpectrum.sort();

        /*
         * If we want to print out the PRM spectrum
         *
         * for(Double mass: newSpectrum){
         * System.out.println(mass + " " + massToIntensity2dp.get(mass));
         * }
         */
        return newSpectrum;
    }

}
