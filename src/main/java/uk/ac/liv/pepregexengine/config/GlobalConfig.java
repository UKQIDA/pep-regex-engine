
package uk.ac.liv.pepregexengine.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 11-Jan-2016 14:55:05
 */
public class GlobalConfig {

    private static final GlobalConfig instance = new GlobalConfig();

    /**
     * @return the instance
     */
    public static GlobalConfig getInstance() {
        return instance;
    }

    private File mfgFolder;

    public GlobalConfig() {
        try {
            mfgFolder = new File(new File(".").getCanonicalPath());
        }
        catch (IOException ex) {
            //TODO
            Logger.getLogger(GlobalConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the mfgFolder
     */
    public File getMfgFolder() {
        return mfgFolder;
    }

    /**
     * @param mfgFolder the mfgFolder to set
     */
    public void setMfgFolder(File mfgFolder) {
        this.mfgFolder = mfgFolder;
    }

}
