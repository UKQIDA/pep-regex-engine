
package uk.ac.liv.pepregexengine.listener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import uk.ac.liv.pepregexengine.config.GlobalConfig;
import uk.ac.liv.pepregexengine.view.MainFrame;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 12-Jan-2016 17:22:59
 */
public class PeakFilterListener implements ChangeListener {

    @Override
    public void stateChanged(ChangeEvent evt) {
        Integer peakFilter = (Integer) MainFrame.getSpinnerByName("spPeakFilter").getModel().getValue();
        GlobalConfig.getInstance().setPeakFilter(peakFilter);
    }

}
