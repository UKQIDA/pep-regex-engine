
package uk.ac.liv.pepregexengine.utils;

import java.util.ArrayList;
import java.util.List;
import uk.ac.liv.pepregexengine.AAMap;
import uk.ac.liv.pepregexengine.data.constants.Constants;
import uk.ac.liv.pepregexengine.data.Ion;
import uk.ac.liv.pepregexengine.data.Ion.IonType;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 05-Aug-2015 15:56:45
 */
public class BIonGenerator implements IonGenerator {

    private final String sequence;
    private final IonType type = IonType.B_ION;
    private final List<Ion> ions = new ArrayList<>();

    public BIonGenerator(String seq) {
        this.sequence = seq;
        double currentMass = 0;
        double totalMass = 0;
        Ion prevIon = null;
        for (int i = 0; i < sequence.length(); i++) {
            String currentResidue = String.valueOf(sequence.charAt(i));
            String currentSequence = sequence.substring(0, i + 1);
            if (currentResidue.equals("L") || currentResidue.equals("I")) {
                currentResidue = "J";
            }
            currentMass = AAMap.getAaMap().get(currentResidue);
            if (currentMass == AAMap.getAaMap().getNoEntryValue()) {
                throw new NullPointerException("The sequence contain unrecognised symbol.\n");
            }
            totalMass += currentMass;
            Ion ion = new Ion();
            //set ion properties
            ion.setMass(totalMass + Constants.PROTON_MASS);
            ion.setNumber(i + 1);
            ion.setSequence(currentSequence);
            ion.setType(type);
            ion.setPrevIon(prevIon);
            if (prevIon != null) {
                prevIon.setNextIon(ion);
            }
            ion.setNextIon(null); //the last ion will have null nextIon value
            ions.add(ion);

            prevIon = new Ion(ion); //set current ion as previous ion
        }
    }

    @Override
    public List<Ion> getIons() {
        return ions;
    }

    /**
     * @return the accession
     */
    @Override
    public String getSequence() {
        return sequence;
    }

}
