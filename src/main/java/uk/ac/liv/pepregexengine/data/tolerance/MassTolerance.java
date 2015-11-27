
package uk.ac.liv.pepregexengine.data.tolerance;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 26-Nov-2015 11:40:55
 */
public class MassTolerance extends Tolerance {

    public MassTolerance() {
        super();
    }

    public MassTolerance(double del, String u) {
        super(del, u);
    }

    @Override
    public String toString() {
        return "Mass tolerance: " + String.valueOf(this.getDelta()) + this.getUnit();
    }

}
