
package uk.ac.liv.pepregexengine.listener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import uk.ac.liv.pepregexengine.config.GlobalConfig;
import uk.ac.liv.pepregexengine.view.MainFrame;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 12-Jan-2016 17:30:04
 */
public class MassTolListener implements ChangeListener {

    @Override
    public void stateChanged(ChangeEvent evt) {
        Double massTol = (Double) MainFrame.getSpinnerByName("spMassTol").getModel().getValue();
        GlobalConfig.getInstance().getMt().setDelta(massTol);
    }

}
