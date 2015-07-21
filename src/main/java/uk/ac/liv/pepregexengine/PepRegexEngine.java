
package uk.ac.liv.pepregexengine;

import gnu.trove.iterator.TDoubleIterator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 09-Jul-2015 13:09:07
 */
public class PepRegexEngine {

    public static void main(String[] args) {
        try {
            String fastaFile = args[0];
            String mgfFile = args[1];
            String resOutput = args[2];
            String resFullOutput = args[3];

            System.out.println("Start run with the params: " + args[0] + ", " + args[1] + ", " + args[2] + ", " + args[3]);
//            String folderName = "D:\\Users\\ddq\\Dropbox\\TopDownData_from_SamPayne\\";
//            String fastaFile = folderName + "3177.fasta";
//            String mgfFile = folderName + "3177.mgf";
//            String resOutput = folderName + "3177";
//            String resFullOutput = resOutput;

              /*
               * Write out the AA table and Regex table (max triplet) for reference. Only run once
               */            
            FileWriter writer = new FileWriter("aaTable.csv");
            writer.write("mz, regex\n");

            TDoubleIterator iRegex = AAMap.getRegexAAMapRev().keySet().iterator();
            while (iRegex.hasNext()) {
                double mz = iRegex.next();
                writer.write(mz + ", " + AAMap.getRegexAAMapRev().get(mz) + "\n");
            }

            writer.close();

            FastaReader fastaRd = new FastaReader(new File(fastaFile));
            MgfReader mgfRd = new MgfReader(new File(mgfFile));
            //mgfRd.write("spectrumTagsTable.csv");
            PRMToPeptidesMatcher ppMatcher = new PRMToPeptidesMatcher(fastaRd, mgfRd);
            ppMatcher.writeResults(resOutput, resFullOutput);
        }
        catch (IOException ex) {
            Logger.getLogger(PepRegexEngine.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Could not open or find file: " + ex.getMessage());
        }
        catch (JMzReaderException ex) {
            Logger.getLogger(PepRegexEngine.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error in jmzReader: " + ex.getMessage());
        }

    }

}
