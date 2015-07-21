
package uk.ac.liv.pepregexengine;

import gnu.trove.list.TDoubleList;
import gnu.trove.map.TDoubleObjectMap;
import gnu.trove.map.hash.TDoubleObjectHashMap;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 25-Mar-2015 14:12:38
 */
public class SpectrumTags {

    private final MgfReader mgfRd;
    private final static double parts = 5;
    private final static double million = 1000000;
    private final static double ppmError = parts / million;  //initialising as follows doesn't work? ppmError = 3/100000

    public SpectrumTags() {
        this.mgfRd = null;
    }

    public SpectrumTags(File spectrumFile)
            throws JMzReaderException {
        this.mgfRd = new MgfReader(spectrumFile);
    }

    public static List<Object[]> generateTags(String specTitle,
                                              TDoubleList spectrum,
                                              double pepMass) {

        List<Object[]> spectrumTags = new ArrayList<>();
        double biggestMass = AAMap.getAaMasses().max() + 0.5;
        double smallestMass = AAMap.getAaMasses().min() - 0.5;

        //FileWriter file = new FileWriter(tagFile);
        TDoubleObjectMap<String> endPosToTag = new TDoubleObjectHashMap<>();

        spectrum.sort();

        /*
         * Algorithm
         *
         * Build chains:
         *
         * 1. Insert end positions (peak 2) into a hashMap, where value is the current chain
         * 2. If new start position is a current end position, create new chain with paired values
         *
         */
        DecimalFormat df = new DecimalFormat("#.####"); //format to 4dp

        //First pass through the spectrum looks for perfect tags
        for (int i = 0; i < spectrum.size(); i++) {
            double outerMass = Double.parseDouble(df.format(spectrum.get(i)));
            //System.out.println("o:" + outerMass);
            for (int j = i + 1; j < spectrum.size(); j++) {

                double innerMass = Double.parseDouble(df.format(spectrum.get(j)));
                double delta = Double.parseDouble(df.format(innerMass - outerMass));
                //System.out.println("\ti:" + innerMass);

                //Boolean aaFound = false;
                if (delta > smallestMass && delta < biggestMass) {
                    // set a range for delta mass between two spectrums (innerMass, outerMass)
                    double topRange = delta + (ppmError * innerMass);
                    double bottomRange = delta - (ppmError * innerMass);
                    //System.out.print("\t\tppm: " + ppmError + " Delta: " + delta + " bottom: " + bottomRange + " top: " + topRange+"\n");

                    for (int k = 0; k < AAMap.getAaMasses().size(); k++) {
                        double aa = AAMap.getAaMasses().get(k);
                        //System.out.print("\t\t\t" + aa);

                        // check if SINGLE amino acid mass falls into the range of delta mass
                        if (aa > bottomRange && aa < topRange) {
                            //System.out.print("Found" + aa);
                            //aaFound = true; // found a possible amino acid
//                            double error = delta - aa;
//                            double prefix = outerMass;
//                            double suffix = pepMass - innerMass;
                            String foundAAString = AAMap.getAaMapRev().get(aa);
                            // String resLine = foundAA + "," + outerMass + "," + innerMass + "," + delta + "," + error + "," + suffix;

                            if (endPosToTag.containsKey(outerMass)) {
                                String currTag = endPosToTag.get(outerMass) + foundAAString;    //Add to chain
                                endPosToTag.put(innerMass, currTag);
                                //System.out.println("Adding to tag, inner: " + innerMass + " outer: " + outerMass + " tag: " +currTag);
                            }
                            else {
                                double prefixMass = outerMass;
                                /*
                                 * if(doProteins){
                                 * prefixMass = outerMass- CTERM - HMASS; //I don't see why this works but it seems to TODO TO DO
                                 * }
                                 * else{
                                 * prefixMass = outerMass;
                                 * }
                                 */
                                endPosToTag.put(innerMass, "" + outerMass + "," + prefixMass + "," + foundAAString);
                                //System.out.println("Found tag, inner: " + innerMass + " outer: " + outerMass + " tag: " +foundAA);
                            }

                            //writerTags.write(resLine +"\n");
                            //k = AAMap.getAaMasses().size();
                            break;
                        }
                    }
                }
            } //inner loop
        } //outer_loop

        //Trace through = prefixMass is outerMZ (i.e. first peak - 18Daltons) - unclear why this was needed
        //Suffix Mass = peptide mass - peak being the end of the tag
        // writerTags.write("\n\n");
        for (double endPosition : endPosToTag.keys()) {
            String tempTag = endPosToTag.get(endPosition);

            String temp[] = tempTag.split(",");
            String startPos = temp[0]; // outerMass
            //temp[1] is prefixMass which is not changed here

            String tag = temp[2]; // foundAA
            double suffixMass = pepMass - endPosition;

            //writerTags.write(specTitle + "," + startPos + "," + endPosition + "," + tag + "," + suffixMass + "," + pepMass + "\n");
            double prefixMass = Double.parseDouble(temp[1]);

            //TODO - There is redunancy in the tags which needs to be removed
            if (tag.length() >= Constants.MIN_TAG_LENGTH_FOR_SEARCH) {
                Object[] triplet = {tag, prefixMass, suffixMass};
                spectrumTags.add(triplet);
            }
        }

        //Second pass through the data to generate longer strings, including ambiguity
        //Reset if allowed paired amino acids
        biggestMass = AAMap.getRegexAAMasses().max() + 0.5;
        smallestMass = AAMap.getRegexAAMasses().min() - 0.5;
        endPosToTag = new TDoubleObjectHashMap<>();    //start again
        for (int i = 0; i < spectrum.size(); i++) {

            Double outerMass = Double.parseDouble(df.format(spectrum.get(i)));
            for (int j = i + 1; j < spectrum.size(); j++) {
                Double innerMass = Double.parseDouble(df.format(spectrum.get(j)));
                Double delta = Double.parseDouble(df.format(innerMass - outerMass));

                Boolean aaFound = false;
                if (delta > smallestMass && delta < biggestMass) {
                    double topRange = delta + (ppmError * innerMass);
                    double bottomRange = delta - (ppmError * innerMass);
                    //System.out.print("ppm: " + ppmError + " Delta: " + delta + " bottom: " + bottomRange + " top: " + topRange);

                    for (int k = 0; k < AAMap.getAaMasses().size(); k++) {
                        Double aa = AAMap.getAaMasses().get(k);
                        //System.out.print("\t" + aa);

                        if (aa > bottomRange && aa < topRange) {
                            aaFound = true;
                            double error = delta - aa;
                            double prefix = outerMass;
                            double suffix = pepMass - innerMass;
                            String foundAA = AAMap.getRegexAAMapRev().get(aa);
                            //String resLine = foundAA + "," + outerMZ + "," + innerMZ + "," + delta + "," + error + "," + suffix;

                            if (endPosToTag.containsKey(outerMass)) {
                                String currTag = endPosToTag.get(outerMass) + foundAA;    //Add to chain
                                endPosToTag.put(innerMass, currTag);
                            }
                            else {
                                double prefixMass = outerMass;

                                endPosToTag.put(innerMass, "" + outerMass + "," + prefixMass + "," + foundAA);
                            }

                            //writerTags.write(resLine +"\n");
                            k = AAMap.getAaMasses().size();
                        }
                    }

                    /*
                     * Now check for pairs of amino acids if diff between peaks is not a single AA
                     *
                     */
                    if (!aaFound) {
                        for (int k = 0; k < AAMap.getRegexAAMasses().size(); k++) {
                            Double aa = AAMap.getRegexAAMasses().get(k);
                            //System.out.print("\t" + aa);

                            if (aa > bottomRange && aa < topRange) {
//                                double error = delta - aa;
//                                double prefix = outerMass;
//                                double suffix = pepMass - innerMass;
                                String foundRegex = "[" + AAMap.getRegexAAMapRev().get(aa) + "]";
                                //System.out.println(foundPair + "\t" + outerMZ + "\t" + innerMZ+ "\t" + delta + "\t" + error + "\t" + suffix);
                                k = AAMap.getRegexAAMasses().size();
                                if (endPosToTag.containsKey(outerMass)) {
                                    String currTag = endPosToTag.get(outerMass) + foundRegex;    //Add to chain
                                    endPosToTag.put(innerMass, currTag);
                                }
                                else {
                                    double prefixMass = outerMass;
                                    /*
                                     * if(doProteins){
                                     * prefixMass = outerMass- CTERM - HMASS; //I don't see why this works but it seems to TODO TO DO
                                     * }
                                     * else{
                                     * prefixMass = outerMass;
                                     * }
                                     */
                                    endPosToTag.put(innerMass, "" + outerMass + "," + prefixMass + "," + foundRegex);
                                }
                            }
                        }
                    }

                    //System.out.print("\n");
                }
            }
        }

        for (double endPosition : endPosToTag.keys()) {
            String tempTag = endPosToTag.get(endPosition);
            String temp[] = tempTag.split(",");
            //String startPos = temp[0];
            if (temp[2].contains("null")){
                System.out.println("Something wrong with the null value, please check!");
            }
            String tag = "%%" + temp[2];//To denote this is now a regular expression
            double suffixMass = pepMass - endPosition;

            //writerTags.write(specTitle + "," + startPos + "," + endPosition + "," + tag + "," + suffixMass + "," + pepMass + "\n");
            double prefixMass = Double.parseDouble(temp[1]);

            //TODO - There is redunancy in the tags which needs to be removed
            if (tag.contains("[")) {
                Object[] triplet = {tag, prefixMass, suffixMass};
                spectrumTags.add(triplet);
            }
        }

        return spectrumTags;
    }

    /**
     * @return the mgfRd
     */
    public MgfReader getMgfRd() {
        return mgfRd;
    }

}
