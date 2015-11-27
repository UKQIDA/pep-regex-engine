
package uk.ac.liv.pepregexengine.data.tolerance;

import uk.ac.liv.pepregexengine.data.constants.Constants;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 26-Nov-2015 11:51:44
 */
public class MassToleranceWindow {

    private final MassTolerance mt;
    private final double left;
    private final double right;
    private final double base;

    public MassToleranceWindow(double base, MassTolerance mt) {
        this.base = base;
        this.mt = mt;
        double delta = 0;
        if (mt.getUnit().equalsIgnoreCase(Constants.DALTON)) {
            delta = mt.getDelta();
        }
        else if (mt.getUnit().equalsIgnoreCase(Constants.PPM)) {
            delta = this.base * (1 + mt.getDelta() / 1000000);
        }
        else {
            throw new IllegalArgumentException("The mass tolerence unit is not correct. It must be either \'Da\' or \'ppm\'.");
        }
        this.left = this.base - delta;
        this.right = this.base + delta;
    }

    /**
     * @return the mt
     */
    public MassTolerance getMt() {
        return mt;
    }

    /**
     * @return the left
     */
    public double getLeft() {
        return left;
    }

    /**
     * @return the right
     */
    public double getRight() {
        return right;
    }

    /**
     * @return the base
     */
    public double getBase() {
        return base;
    }

}
