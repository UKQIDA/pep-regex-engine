
package uk.ac.liv.pepregexengine.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import uk.ac.liv.pepregexengine.data.Ion;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 05-Aug-2015 13:24:59
 */
public interface IonGenerator {

    List<Ion> getIons();

    String getSequence();

    default void write(String out)
            throws IOException {
        try (FileWriter writer = new FileWriter(out)) {
            writer.write("Ion Type, #, Mass, Seq\n");
            for (Ion ion : this.getIons()) {
                writer.write(ion.getType().getName() + ", " + ion.getNumber() + ", " + ion.getMass() + ", " + ion.getSequence() + "\n");
            }
        }
    }

    default void writeDetail(String out)
            throws IOException {
        try (FileWriter writer = new FileWriter(out)) {
            writer.write("Ion Type, #, Mass, Seq\n");
            for (Ion ion : this.getIons()) {
                String currentRow = ion.getType().getName() + ", " + ion.getNumber() + ", " + ion.getMass() + ", " + ion.getSequence();

                for (Ion secondIon : this.getIons()) {
                    double mass = secondIon.getMass() - ion.getMass();
                    if (mass >= 0) {
                        currentRow = currentRow + ", " + mass;
                    }
                    else {
                        currentRow = currentRow + ", " + " ";
                    }
                }

                currentRow = currentRow + "\n";
                
                writer.write(currentRow);
            }
        }
    }

}
