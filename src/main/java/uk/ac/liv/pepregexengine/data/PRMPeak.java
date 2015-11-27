
package uk.ac.liv.pepregexengine.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * The class represents a single prefix residue mass (PRM) peak.
 * The attributes of a PRMPeak includes de-charged mass value, intensity value and direction flag.
 * @author Da Qi
 * @institute University of Liverpool
 * @time 10-Aug-2015 14:59:46
 */
public class PRMPeak {

    private double mass; //decharged mass (neutral mass?) //mass from mgf MINUS one proton mass
    private double intensity; //Default is 1.0
    private int direction; //direction of the peak: forward-0; reverse-1; both-2. Default is 0

    public PRMPeak() {
        this.mass = 0.0;
        this.intensity = 1.0;
        this.direction = 0;
    }

    public PRMPeak(double mass, double intensity, int direc) {
        this.mass = mass;
        this.intensity = intensity;
        this.direction = direc;
    }

    public PRMPeak(double mass) {
        this(mass, 50.0, 0);
    }

    /**
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * @return the intensity
     */
    public double getIntensity() {
        return intensity;
    }

    /**
     * @param intensity the intensity to set
     */
    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    /**
     * @return the direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    /**
     * Two PRMPeaks equal only if their masses are equal.
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }

        PRMPeak rhs = (PRMPeak) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.getMass(), rhs.getMass())
                .isEquals();

    }

    @Override
    public int hashCode() {
        int hash = 7;
        return new HashCodeBuilder(hash, 89)
                .append(this.getMass())
                .hashCode();
    }

}
