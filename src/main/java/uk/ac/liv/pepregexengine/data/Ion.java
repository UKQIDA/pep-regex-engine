
package uk.ac.liv.pepregexengine.data;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 05-Aug-2015 13:20:43
 */
public class Ion {

    public enum IonType {

        A_ION("a ion"),
        B_ION("b ion"),
        C_ION("c ion"),
        X_ION("x ion"),
        Y_ION("y ion"),
        Z_ION("z ion");

        private final String name;

        IonType(String name) {
            this.name = name;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

    }

    private IonType type;
    private int number;
    private String sequence;
    private double mass;
    private int charge;
    private Ion nextIon;
    private Ion prevIon;

    public Ion() {
        this.charge = 1;
        this.number = -1;
        this.mass = 0.0;
        this.type = IonType.B_ION;
        this.nextIon = null;
        this.prevIon = null;
    }

    public Ion(Ion ion) {
        this.charge = ion.getCharge();
        this.mass = ion.getCharge();
        this.nextIon = ion.getNextIon();
        this.prevIon = ion.getPrevIon();
        this.number = ion.getNumber();
        this.sequence = ion.getSequence();
        this.type = ion.getType();
    }

    /**
     * @return the type
     */
    public IonType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(IonType type) {
        this.type = type;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return the sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
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
     * @return the charge
     */
    public int getCharge() {
        return charge;
    }

    /**
     * @param charge the charge to set
     */
    public void setCharge(int charge) {
        this.charge = charge;
    }

    /**
     * @return the nextIon
     */
    public Ion getNextIon() {
        return nextIon;
    }

    /**
     * @param nextIon the nextIon to set
     */
    public void setNextIon(Ion nextIon) {
        this.nextIon = nextIon;
    }

    /**
     * @return the prevIon
     */
    public Ion getPrevIon() {
        return prevIon;
    }

    /**
     * @param prevIon the prevIon to set
     */
    public void setPrevIon(Ion prevIon) {
        this.prevIon = prevIon;
    }

}
