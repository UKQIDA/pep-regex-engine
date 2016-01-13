
package uk.ac.liv.pepregexengine.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import uk.ac.liv.pepregexengine.gui.config.GlobalConfig;
import uk.ac.liv.pepregexengine.gui.MainFrame;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 12-Jan-2016 17:42:53
 */
public class MassUnitListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent evt) {
        String massUnit = (String) MainFrame.getComboBoxByName("cbMassUnit").getModel().getSelectedItem();
        GlobalConfig.getInstance().getMt().setUnit(massUnit);
    }

}
