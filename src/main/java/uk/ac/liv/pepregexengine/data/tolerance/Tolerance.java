
package uk.ac.liv.pepregexengine.data.tolerance;

import uk.ac.liv.pepregexengine.data.constants.Constants;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 26-Nov-2015 11:24:15
 */
public abstract class Tolerance {

    private String unit;
    private double delta;

    public Tolerance() {
        this.delta = 0.01;
        this.unit = Constants.DALTON;
    }

    public Tolerance(double del, String u) {
        this.delta = del;
        this.unit = u;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @return the delta
     */
    public double getDelta() {
        return delta;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @param delta the delta to set
     */
    public void setDelta(double delta) {
        this.delta = delta;
    }
    
    @Override
    public abstract String toString();

}
