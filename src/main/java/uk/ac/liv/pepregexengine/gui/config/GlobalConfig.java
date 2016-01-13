package uk.ac.liv.pepregexengine.gui.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.liv.pepregexengine.data.tolerance.MassTolerance;

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
    private File outputDir;
    private MassTolerance mt;
    private int peakFilter;
    private int dp;
    private boolean spectrumTag;

    public GlobalConfig() {
        try {
            mfgFolder = new File(new File(".").getCanonicalPath());
            outputDir = new File(new File(".").getCanonicalPath());
            mt = new MassTolerance(10.0, "ppm");
            dp =2;
            spectrumTag = true;
        } catch (IOException ex) {
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

    /**
     * @return the mt
     */
    public MassTolerance getMt() {
        return mt;
    }

    /**
     * @param mt the mt to set
     */
    public void setMt(MassTolerance mt) {
        this.mt = mt;
    }

    /**
     * @return the peakFilter
     */
    public int getPeakFilter() {
        return peakFilter;
    }

    /**
     * @param peakFilter the peakFilter to set
     */
    public void setPeakFilter(int peakFilter) {
        this.peakFilter = peakFilter;
    }

    /**
     * @return the dp
     */
    public int getDp() {
        return dp;
    }

    /**
     * @param dp the dp to set
     */
    public void setDp(int dp) {
        this.dp = dp;
    }

    /**
     * @return the outputDir
     */
    public File getOutputDir() {
        return outputDir;
    }

    /**
     * @param outputDir the outputDir to set
     */
    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    /**
     * @return the spectrumTag
     */
    public boolean isSpectrumTag() {
        return spectrumTag;
    }

    /**
     * @param spectrumTag the spectrumTag to set
     */
    public void setSpectrumTag(boolean spectrumTag) {
        this.spectrumTag = spectrumTag;
    }

}
