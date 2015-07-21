
package uk.ac.liv.pepregexengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.ac.liv.pepregexengine.utils.PeptideNeutralMassCalculator;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 20-Mar-2015 14:38:25
 */
public class FastaReader {

    private final BufferedReader in;

    private String accessionRegex = " ";

    private List<String> allProteinIDs = new ArrayList<>();
    private Map<String, String> accToSeq = new HashMap<>();
    private Map<String, String> accToDefLine = new HashMap<>();
    private Map<String, String[]> accToPeptides = new HashMap<>();

    //TODO: the variables that can be made as user input parameters.
    //TODO: make an independant enzyme rule class to deal with different digested enzyme?
    private String enzymeRegex = "(?<=[KR])(?!P)"; //Trypsin default
    private int missedCleavages = 2;

    private Map<String, List<String>> peptideToAccs = new HashMap<>();
    private Map<String, List<Object[]>> peptideIndex = new HashMap<>();           //The complete protein tag index
    private Map<Double, List<Object[]>> prefixIndex = new HashMap<>();            //The index accessed by prefix mass - not yet implemented
    private Map<Double, List<Object[]>> suffixIndex = new HashMap<>();            //The index accessed by suffix mass

    public FastaReader(File file)
            throws FileNotFoundException, IOException {
        try {
            in = new BufferedReader(new FileReader(file));
            System.out.println("Start parsing fasta file ......");
            parseFile();
        }
        catch (FileNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
            throw new FileNotFoundException("Can't not find the input file ".concat(file.getName()).concat("."));
        }

    }

    public FastaReader(String filename)
            throws FileNotFoundException, IOException {
        this(new File(filename));
    }

    public void close()
            throws IOException {
        this.in.close();
    }

    public Map<String, List<String>> getPeptideToAccs(){
        return peptideToAccs;
    }
    
    public Map<String, List<Object[]>> getPeptideIndex() {
        return peptideIndex;
    }

    public Map<Double, List<Object[]>> getPrefixIndex() {
        return prefixIndex;
    }

    public Map<Double, List<Object[]>> getSuffixIndex() {
        return suffixIndex;
    }

    private void parseFile()
            throws IOException {

        String line;

        String currSequence = "";
        String currProtAcc = null;
        String currDefline = null;

        int recordCounter = 0;
        while ((line = in.readLine()) != null) {
            line = line.replaceAll("\n", "");
            line = line.replaceAll("\r", "");

            if (line.contains(">")) {
                //Insert previous protein sequence into hash and reset
                if (recordCounter != 0) {
                    currSequence = currSequence.replaceAll(" ", "");
                    allProteinIDs.add(currProtAcc);

                    String[] peptides = currSequence.split(enzymeRegex);
                    addPeptidesToMap(currProtAcc, peptides);

                    accToSeq.put(currProtAcc, currSequence);
                    //System.out.println("Inserting:" + currProtAcc + "_" + currSequence);
                    accToDefLine.put(currProtAcc, currDefline);
                    //System.out.println("Inserting2:" + currProtAcc + "_" + currDefline);

                    currSequence = "";
                }

                line = line.replaceAll(">", "");

                int splitPos = line.indexOf(accessionRegex);
                if (splitPos != -1) {
                    currProtAcc = line.substring(0, splitPos);
                    currDefline = line.substring(splitPos + 1);
                }
                else {
                    System.out.println("Regular expression not found for split: " + line);
                    throw new IllegalStateException("Accession regular expression not found for split: " + line);
                }

                recordCounter++;
            }
            else {
                currSequence += line;
            }
        }
        //handle last
        currSequence = currSequence.replaceAll(" ", "");
        accToSeq.put(currProtAcc, currSequence);
        accToDefLine.put(currProtAcc, currDefline);

        String[] peptides = currSequence.split(enzymeRegex);
        addPeptidesToMap(currProtAcc, peptides);
        //System.out.println("Put last:" + currProtAcc + " seq: " + currSequence);
        allProteinIDs.add(currProtAcc);

        // build peptide index
//        FileWriter writer = new FileWriter(pepIndexFile);
//        FileWriter prefixWriter = new FileWriter(prefixIndexFile);
//        FileWriter suffixWriter = new FileWriter(suffixIndexFile);
        System.out.println("Start building index ......");
        DecimalFormat df = new DecimalFormat("#.####");
        for (String acc : accToSeq.keySet()) {
            //String protSeq = accToSeq.get(acc);

            for (String peptide : accToPeptides.get(acc)) {
                peptide = peptide.replaceAll("L", "J");
                peptide = peptide.replaceAll("I", "J");   //Replacing all Is and Ls with Js
                //int[] windowSizes = {3,4,5,6,7};
                int[] windowSizes = {1, 2, 3, 4, 5};

                //Store peptide to protein maps
                List<String> accsFromThisPep = null;
                if (peptideToAccs.containsKey(peptide)) {
                    accsFromThisPep = peptideToAccs.get(peptide);
                }
                else {
                    accsFromThisPep = new ArrayList();
                    peptideToAccs.put(peptide, accsFromThisPep);
                }
                accsFromThisPep.add(acc);

                for (int windowSize : windowSizes) {
                    for (int i = 0; i <= peptide.length() - windowSize; i++) {
                        String prefix = peptide.substring(0, i);
                        String tag = peptide.substring(i, i + windowSize);
                        String suffix = peptide.substring(i + windowSize, peptide.length());

                        double prefixMass = Double.parseDouble(df.format(PeptideNeutralMassCalculator.getPeptideNeutralMass(prefix)));
                        double suffixMass = Double.parseDouble(df.format(PeptideNeutralMassCalculator.getPeptideNeutralMass(suffix)));

                        Object[] tagTriplet = {peptide, prefixMass, suffixMass};
                        Object[] prefixTriplet = {peptide, tag, suffixMass};
                        Object[] suffixTriplet = {peptide, tag, prefixMass};

                        List<Object[]> dataList = null;
                        if (!peptideIndex.containsKey(tag)) {
                            dataList = new ArrayList();
                        }
                        else {
                            dataList = peptideIndex.get(tag);
                            peptideIndex.put(tag, dataList);
                        }
                        dataList.add(tagTriplet);
                        peptideIndex.put(tag, dataList);

                        //Now the prefix index
                        dataList = null;

                        //To implement this as a key search, the keys will need to go down to 1 or 2dp I guess, will also need to implement some kind of fuzzy matching between tags and sequence
                        //        Also need to remember to implement I/L ambiguity...
                        //        Just try out for one or two difficult spectra to see how it works, then stop for now
                        if (!prefixIndex.containsKey(prefixMass)) {
                            dataList = new ArrayList();
                        }
                        else {
                            dataList = prefixIndex.get(prefixMass);
                            prefixIndex.put(prefixMass, dataList);
                        }
                        dataList.add(prefixTriplet);
                        prefixIndex.put(prefixMass, dataList);

                        //Now the suffix index
                        dataList = null;
                        if (!suffixIndex.containsKey(suffixMass)) {
                            dataList = new ArrayList();
                        }
                        else {
                            dataList = suffixIndex.get(suffixMass);
                            suffixIndex.put(suffixMass, dataList);
                        }
                        dataList.add(suffixTriplet);
                        suffixIndex.put(suffixMass, dataList);

                    }
                }
            }
        }
//        for (String tag : peptideIndex.keySet()) {
//            List<Object[]> allDataForTag = peptideIndex.get(tag);
//            //String tagLine = tag + ",";
//            writer.write(tag);
//            for (Object[] data : allDataForTag) {
//                String peptide = (String) data[0];
//                double prefixMass = (double) data[1];
//                double suffixMass = (double) data[2];
//                writer.write("," + peptide + "," + prefixMass + "," + suffixMass);
//            }
//            writer.write("\n");
//        }
//
//        for (Double prefix : prefixIndex.keySet()) {
//            List<Object[]> allDataForTag = prefixIndex.get(prefix);
//            //String tagLine = tag + ",";
//            prefixWriter.write("" + prefix);
//            for (Object[] data : allDataForTag) {
//                String peptide = (String) data[0];
//                String tag = (String) data[1];
//                double suffixMass = (double) data[2];
//                prefixWriter.write("," + peptide + "," + tag + "," + suffixMass);
//            }
//            prefixWriter.write("\n");
//        }
//
//        for (Double suffix : suffixIndex.keySet()) {
//            List<Object[]> allDataForTag = suffixIndex.get(suffix);
//            //String tagLine = tag + ",";
//            suffixWriter.write("" + suffix);
//            for (Object[] data : allDataForTag) {
//                String peptide = (String) data[0];
//                String tag = (String) data[1];
//                double prefixMass = (double) data[2];
//                suffixWriter.write("," + peptide + "," + tag + "," + prefixMass);
//            }
//            suffixWriter.write("\n");
//        }
//
//        writer.close();
//        prefixWriter.close();
//        suffixWriter.close();

    }

    /**
     * Add all possible peptide combinations of each protein accession into accToPeptides HashMap.
     *
     * @param protAcc  the protein accession string
     * @param peptides the digested peptide list from the same protein
     */
    private void addPeptidesToMap(String protAcc, String[] peptides) {

        List<String> totalPeptides = new ArrayList<>();
        for (int i = 0; i <= missedCleavages; i++) {
            List<String> partPeptides = getPeptideCombinations(peptides, i);
            totalPeptides.addAll(partPeptides);
        }

        accToPeptides.put(protAcc, totalPeptides.toArray(new String[0]));
    }

    /**
     * Get list of peptide combinations from a given peptides array and missed cleavage number.
     * The size of the result list depends on the given size of peptide list and missed cleavage number.
     * For example, given there are 5 peptides in the list and the missed cleavage is 2.
     * Then every 3 peptides in the original order will combined into the result list.
     * The result size of the combined peptide list will be only 3.
     * If missedCleavage is zero, then return the list with same member as from input array.
     *
     * @param peptides
     * @param i
     *
     * @return
     */
    private List<String> getPeptideCombinations(String[] peptides,
                                                int missedCleavage) {
        List<String> combinedList = new ArrayList<>();
        // if the size of given peptide array is greater than missedCleavage. then the result list is not empty.
        if (peptides.length > missedCleavage) {
            for (int i = 0; i < (peptides.length - missedCleavage); i++) {
                StringBuilder sb = new StringBuilder();
                // combine the peptides starting from i until (i+missedCleavage+1)
                for (int j = i; j < (i + missedCleavage + 1); j++) {
                    sb.append(peptides[j]);
                }
                combinedList.add(sb.toString());
            }
        }

        return combinedList;
    }

}
