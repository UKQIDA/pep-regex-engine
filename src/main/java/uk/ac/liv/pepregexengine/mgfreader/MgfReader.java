
package uk.ac.liv.pepregexengine.mgfreader;

import uk.ac.liv.pepregexengine.data.constants.Constants;
import gnu.trove.list.TDoubleList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;
import uk.ac.ebi.pride.tools.mgf_parser.MgfFile;
import uk.ac.ebi.pride.tools.mgf_parser.model.Ms2Query;
import uk.ac.liv.pepregexengine.data.PRMPeak;
import uk.ac.liv.pepregexengine.data.PRMSpectrum;
import uk.ac.liv.pepregexengine.data.tolerance.MassTolerance;

/**
 * The MGF file reader mainly for generating list of PRM spectrum tag for every spectrum.
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 24-Mar-2015 18:23:04
 */
public class MgfReader {

    private List<PRMPeak> prmPeaks;
    private TDoubleList spectrum;
    private Map<String, List<Object[]>> prmSpectrumTagsMap;
    private final MgfFile mgfFile;
    private final String mgfFilename;

    /**
     * @return the prmSpectrumMap
     */
    public Map<String, List<Object[]>> getPrmSpectrumTagsMap() {
        return prmSpectrumTagsMap;
    }

    public MgfReader(File file, MassTolerance mt, DecimalFormat df)
            throws JMzReaderException, IOException {
        this.mgfFilename = file.getCanonicalPath();
        this.mgfFile = new MgfFile(file);
//        System.out.println("Start loading spectrum file ......");
        loadSpectraFromFile(mt, df);
    }

    private void loadSpectraFromFile(MassTolerance mt, DecimalFormat df)
            throws JMzReaderException {

        Iterator<Ms2Query> it = mgfFile.getMs2QueryIterator();
        Set<String> processedIds = new HashSet<>(); // record the value of TITLE

        prmSpectrumTagsMap = new HashMap<>();
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
            //double dummyPrecursorIntensity = 1.0;
            //int msLevel = 2;
            if (query.getPeakList() != null) {
                int z = query.getPrecursorCharge();
                double pepMass = (query.getPrecursorMZ() * z) - (z * Constants.PROTON_MASS);
                //TODO: testing parameter
                PRMSpectrum prmSpectrum = new PRMSpectrum(query, 15);

                PRMSpectrum allSpectrum = new PRMSpectrum(query);

                prmPeaks = allSpectrum.getPrmPeaks();
//                
//                try {
//                    //testSpectrum.writeDetail("3177-PRM-spectrum.csv");
//                    testSpectrum.writeDetail("3177-b-ion-simulation-PRM-spectrum.csv");
//                }
//                catch (IOException ex) {
//                    Logger.getLogger(MgfReader.class.getName()).log(Level.SEVERE, null, ex);
//                }

                //System.out.println(prmSpectrum);
                List<Object[]> spectrumTags = SpectrumTags.generateTags(query.getTitle(), prmPeaks, pepMass, mt, df);
                //System.out.println("Generated tags for: " + query.getTitle() + " size:" + spectrumTags.size());
                prmSpectrumTagsMap.put(query.getTitle(), spectrumTags);
            }
            else {
                //System.out.println("\n\n\tNull peak list: " + query.getTitle());
            }
        }
    }

    /*
     * Write out the prmSpectrumTagsMap
     */
    public void writePRMSpectruTags(String filename, MassTolerance mt,
                                    DecimalFormat df)
            throws JMzReaderException {
        //loadSpectraFromFile(mt, df);
        try (FileWriter writer = new FileWriter(filename.replace(".csv", "_") + mt.getDelta() + "_" + df.toPattern().replace(".", "_") + ".csv")) {
            writer.write("Parameters, Input Mgf file, " + this.mgfFilename + ", " + mt.toString() + ", Decimal Place, " + df.toPattern() + "\n");
            writer.write("Spectrum title, tag, prefix mass, suffix mass\n");
            for (String title : prmSpectrumTagsMap.keySet()) {
                List<Object[]> tagList = prmSpectrumTagsMap.get(title);
                for (Object[] tag : tagList) {
                    writer.write(title + ", " + tag[0] + ", " + tag[1] + ", " + tag[2] + "\n");
                }
            }
        }
        catch (IOException ex) {
            Logger.getLogger(MgfReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
