package uk.ac.liv.pepregexengine.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import uk.ac.liv.pepregexengine.gui.config.GlobalConfig;
import uk.ac.liv.pepregexengine.gui.MainFrame;

/**
 *
 * @author Da
 */
public class OutputDirectoryListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent evt) {
        JFileChooser outFC = new JFileChooser();
        outFC.setDialogTitle("Choose directory to save");
        outFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        outFC.setAcceptAllFileFilterUsed(false);
        outFC.setCurrentDirectory(GlobalConfig.getInstance().getOutputDir());

        int returnVal = outFC.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File outDir = outFC.getSelectedFile();
            GlobalConfig.getInstance().setOutputDir(outDir.getAbsoluteFile());
            MainFrame.getTextComponentByName("tfOutputDir").setText(outDir.getAbsolutePath());
        }
    }

}
