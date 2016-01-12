
package uk.ac.liv.pepregexengine.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import uk.ac.liv.pepregexengine.config.GlobalConfig;
import uk.ac.liv.pepregexengine.view.MainFrame;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 11-Jan-2016 13:18:47
 */
public class LoadMgfFileListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent evt) {

        JFileChooser mgfFC = new JFileChooser();
        mgfFC.setDialogTitle("Choose an MGF file");
        mgfFC.setCurrentDirectory(GlobalConfig.getInstance().getMfgFolder());

        // file extension filters
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Mascot Generic Format(*.mgf)", "mgf");
        mgfFC.setFileFilter(filter);

        int returnVal = mgfFC.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File mgfFile = mgfFC.getSelectedFile();
            GlobalConfig.getInstance().setMfgFolder(mgfFile.getParentFile());
            MainFrame.getTextComponentByName("tfMgfFile").setText(mgfFile.getAbsolutePath());
        }
    }

}
