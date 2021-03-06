
package uk.ac.liv.pepregexengine.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;
import uk.ac.liv.pepregexengine.FastaReader;
import uk.ac.liv.pepregexengine.PRMToPeptidesMatcher;
import uk.ac.liv.pepregexengine.data.tolerance.MassTolerance;
import uk.ac.liv.pepregexengine.gui.MainFrame;
import uk.ac.liv.pepregexengine.gui.config.GlobalConfig;
import uk.ac.liv.pepregexengine.mgfreader.MgfReader;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 12-Jan-2016 16:57:01
 */
public class RunSearchListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent evt) {
        String mgfFile = MainFrame.getTextComponentByName("tfMgfFile").getText();
        String fastaFile = MainFrame.getTextComponentByName("tfFastaFile").getText();
        String dfString = "#.##";
        int m = GlobalConfig.getInstance().getDp();
        dfString = "#.";
        for (int i = 0; i < m; i++) {
            dfString += "#";
        }
        DecimalFormat df = new DecimalFormat(dfString);

        MassTolerance mt = GlobalConfig.getInstance().getMt();

        try {
            MgfReader mgfRd = new MgfReader(new File(mgfFile), mt, df);

            //output Tag generation result
            if (GlobalConfig.getInstance().isSpectrumTag()) {
                String tagFile = mgfFile.replace(".mgf", "_spectrumTagTable.csv");

                mgfRd.writePRMSpectruTags(tagFile, mt, df);
            }

            if (fastaFile != null || !fastaFile.isEmpty()) {
                int pos = mgfFile.lastIndexOf("\\");
                String resOutput = GlobalConfig.getInstance().getOutputDir().getAbsolutePath() + mgfFile.substring(pos).replace(".mgf", "_result.csv");
                String resFullOutput = resOutput.replace(".csv", "_full.csv");
                FastaReader fastaRd = new FastaReader(new File(fastaFile));
                PRMToPeptidesMatcher ppMatcher = new PRMToPeptidesMatcher(fastaRd, mgfRd);
                ppMatcher.writeResults(resOutput, resFullOutput);
            }

        }
        catch (JMzReaderException | IOException ex) {
            Logger.getLogger(RunSearchListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
