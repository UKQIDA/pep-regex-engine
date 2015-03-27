
package uk.ac.liv.pepregexengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public FastaReader(File file)
            throws FileNotFoundException, IOException {
        try {
            in = new BufferedReader(new FileReader(file));
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
