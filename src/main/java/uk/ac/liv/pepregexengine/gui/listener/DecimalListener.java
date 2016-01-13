
package uk.ac.liv.pepregexengine.gui.listener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import uk.ac.liv.pepregexengine.gui.config.GlobalConfig;
import uk.ac.liv.pepregexengine.gui.MainFrame;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 12-Jan-2016 17:30:39
 */
public class DecimalListener implements ChangeListener {

    @Override
    public void stateChanged(ChangeEvent evt) {
        Integer decimal = (Integer) MainFrame.getSpinnerByName("spDecimal").getModel().getValue();
        GlobalConfig.getInstance().setDp(decimal);
    }

}
