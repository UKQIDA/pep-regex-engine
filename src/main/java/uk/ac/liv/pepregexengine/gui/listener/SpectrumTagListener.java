package uk.ac.liv.pepregexengine.gui.listener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import uk.ac.liv.pepregexengine.gui.MainFrame;
import uk.ac.liv.pepregexengine.gui.config.GlobalConfig;

/**
 *
 * @author Da
 */
public class SpectrumTagListener implements ChangeListener {

    @Override
    public void stateChanged(ChangeEvent evt) {
        GlobalConfig.getInstance().setSpectrumTag(MainFrame.getCheckBoxByName("cbSpectrumTag").isSelected());
    }

}
