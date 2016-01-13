
package uk.ac.liv.pepregexengine.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import uk.ac.liv.pepregexengine.gui.MainFrame;
import uk.ac.liv.pepregexengine.gui.config.GlobalConfig;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 13-Jan-2016 11:10:18
 */
public class FastaFileListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent evt) {
        JFileChooser fastaFC = new JFileChooser();
        fastaFC.setDialogTitle("Choose a FASTA file");
        fastaFC.setCurrentDirectory(GlobalConfig.getInstance().getMfgFolder());

        // file extension filters
        FileNameExtensionFilter filter = new FileNameExtensionFilter("FASTA format(*.fasta)", "fasta");
        fastaFC.setFileFilter(filter);

        int returnVal = fastaFC.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fastaFile = fastaFC.getSelectedFile();
            GlobalConfig.getInstance().setMfgFolder(fastaFile.getParentFile());
            MainFrame.getTextComponentByName("tfFastaFile").setText(fastaFile.getAbsolutePath());
        }
    }

}
