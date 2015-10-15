
package uk.ac.liv.pepregexengine;

import uk.ac.liv.pepregexengine.data.Constants;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 09-Jul-2015 11:58:24
 */
public class PRMToPeptidesMatcher {

    private final FastaReader fr;
    private final MgfReader mr;
    private final Map<String, List<Object[]>> peptideIndex;
    private final Map<String, List<Object[]>> prmSpectrumTagsMap;
    private final Map<String, List<String>> peptideToAccs;
    private FileWriter writerRes;
    private FileWriter writerFullRes;

    public PRMToPeptidesMatcher(FastaReader fastaRd, MgfReader mgfRd) {
        this.fr = fastaRd;
        this.mr = mgfRd;
        this.peptideIndex = fastaRd.getPeptideIndex();
        this.peptideToAccs = fastaRd.getPeptideToAccs();
        this.prmSpectrumTagsMap = mgfRd.getPrmSpectrumTagsMap();
    }

    // Temperate method for outputting the results
    public void writeResults(String resFileName, String resFullFileName)
            throws IOException {
        if (!resFileName.endsWith(".csv")) {
            resFileName = resFileName.concat(".csv");
        }

        if (!resFullFileName.endsWith(".csv")) {
            resFullFileName = resFullFileName.concat("_full.csv");
        }

        writerRes = new FileWriter(resFileName);
        writerFullRes = new FileWriter(resFullFileName);

        writerFullRes.write("SpectrumID,ProtAcc,peptide,TAG,Prefix,PrefixDelta,Suffix,SuffixDelta\n");
        writerRes.write("Spectrum,ProtAcc,Seq,Score,MostCommonPrefixMass,CountPrefixMass,MostCommonSuffixMass,CountSuffixMass\n");

        for (String specID : prmSpectrumTagsMap.keySet()) {
            List<Object[]> spectrumTags = prmSpectrumTagsMap.get(specID);
            System.out.println("Querying spectrum: " + specID + " size:" + spectrumTags.size());

            matchPRMToPeptides(spectrumTags, specID);
        }

        writerRes.close();
        writerFullRes.close();
    }

    /*
     * Algorithm:
     * 1. Attempt to match in tag 7, 6, ... 3 index
     * 2. If get a tag match:
     * - Check prefix
     * - Check suffix
     * - Collect the delta errors from prefix and suffix match -
     * if same mass is seen more than once, assume this is a systematic shift
     * 3. Build a scoring function, based on tag length, prefix and suffix matches.
     * If same prefix or suffix error match is seen more than once,
     * then assume this is a systematic error and not down weighted
     */
    private void matchPRMToPeptides(List<Object[]> spectrumTags,
                                    String specID) {

        try {

            Map<String, List<Object[]>> mapPepSeqToMatches = new HashMap();

            for (Object[] spectrumTag : spectrumTags) {

                DecimalFormat df = new DecimalFormat("#.###");
                String tag = (String) spectrumTag[0];
                tag = tag.replaceAll("L", "J");
                tag = tag.replaceAll("I", "J");   //Replacing all Is and Ls with Js

                double prefix = (double) spectrumTag[1];
                double suffix = (double) spectrumTag[2];

                //System.out.println("tagsearch:"+tag+"\t"+df.format(prefix)+"\t"+df.format(suffix));
                if (!tag.contains("%%")) {

                    List<Object[]> tagMatches = peptideIndex.get(tag);
                    //System.out.println("Getting matches for tag:" + tag);
                    if (tagMatches != null) {
                        //System.out.println("\tsize:" + tagMatches.size());
                    }
                    String revTag = new StringBuilder(tag).reverse().toString();
                    List<Object[]> revTagMatches = peptideIndex.get(revTag);
                    //System.out.println("Getting matches for revtag:" + revTag);
                    if (revTagMatches != null) {
                        //System.out.println("\tsize:" + revTagMatches.size());
                    }

                    //ArrayList<Object[]> allMatches = new ArrayList();

                    /*
                     * if(tagMatches!=null){
                     * for(int i=0;i<tagMatches.size();i++){
                     * allMatches.add(tagMatches.get(i));
                     * }
                     * }
                     * if(revTagMatches!=null){
                     * for(int i=0;i<revTagMatches.size();i++){
                     * allMatches.add(revTagMatches.get(i));
                     * }
                     * }
                     */
                    if (tagMatches != null) {
                        for (Object[] matchedTag : tagMatches) {
                            //{protID2,prefixMass,suffixMass};
                            String seq = (String) matchedTag[0];
                            double prefixDelta = prefix - (double) matchedTag[1];    //These need to be reversed in rev matches
                            double suffixDelta = suffix - (double) matchedTag[2] - Constants.CTERM - Constants.HYDROGEN_MASS;

                            //if(doProteins){
                            //    writerFullRes.write(specID+","+id+",null,"+tag+","+prefix+","+prefixDelta+","+suffix+","+suffixDelta+"\n");
                            //}
                            //else{
                            List<String> protAccs = peptideToAccs.get(seq);
                            String accs = protAccs.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", ";");
                            writerFullRes.write(specID + "," + accs + "," + seq + "," + tag + "," + prefix + "," + prefixDelta + "," + suffix + "," + suffixDelta + ",Forward\n");
                            // }

                            List<Object[]> dataForProt = null;
                            if (!mapPepSeqToMatches.containsKey(seq)) {
                                dataForProt = new ArrayList();
                                mapPepSeqToMatches.put(seq, dataForProt);
                            }
                            else {
                                dataForProt = mapPepSeqToMatches.get(seq);
                            }
                            Object[] tagMatchedWithDeltas = {tag, prefixDelta, suffixDelta};    //TODO at source, can save memory (perhaps?) by moving down to 3 dp?
                            dataForProt.add(tagMatchedWithDeltas);
                        }
                    }
                    if (revTagMatches != null) {
                        for (Object[] matchedTag : revTagMatches) {

                            //{protID2,prefixMass,suffixMass};
                            String seq = (String) matchedTag[0];
                            double prefixDelta = suffix - (double) matchedTag[1];
                            double suffixDelta = prefix - (double) matchedTag[2] - Constants.CTERM - Constants.HYDROGEN_MASS;

                            //if(doProteins){
                            //    writerFullRes.write(specID+","+id+",null,"+revTag+","+prefix+","+prefixDelta+","+suffix+","+suffixDelta+"\n");
                            //}
                            //else{
                            List<String> protAccs = peptideToAccs.get(seq);
                            String accs = protAccs.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", ";");
                            writerFullRes.write(specID + "," + accs + "," + seq + "," + revTag + "," + prefix + "," + prefixDelta + "," + suffix + "," + suffixDelta + ",Rev\n");
                            //}

                            List<Object[]> dataForProt = null;
                            if (!mapPepSeqToMatches.containsKey(seq)) {
                                dataForProt = new ArrayList();
                                mapPepSeqToMatches.put(seq, dataForProt);
                            }
                            else {
                                dataForProt = mapPepSeqToMatches.get(seq);
                            }
                            Object[] tagMatchedWithDeltas = {revTag, prefixDelta, suffixDelta};    //TODO at source, can save memory (perhaps?) by moving down to 3 dp?
                            dataForProt.add(tagMatchedWithDeltas);
                        }
                    }

                }
                else {

                    //System.out.println("Regular expression matching not yet implemented");
                }

            }

            for (String seq : mapPepSeqToMatches.keySet()) {
                List<Object[]> tagMatches = mapPepSeqToMatches.get(seq);
                double pepScore = 0.0;

                HashMap<Double, Integer> mapPrefixErrorsToCount = new HashMap();
                HashMap<Double, Integer> mapSuffixErrorsToCount = new HashMap();

                //First pass to pick up the distributions of errors
                for (Object[] tagMatch : tagMatches) {
                    Boolean oneMassMatched = false;
                    String tag = (String) tagMatch[0];
                    int tagScore = tag.length() * tag.length() * tag.length();
                    double tagErrorScore = 1.0 * tagScore;
                    double prefixMass = (double) tagMatch[1];
                    double suffixMass = (double) tagMatch[2];

                    //TODO The following code is rather clunky in the use of HashMaps and Integers
                    DecimalFormat df = new DecimalFormat("#.#");
                    Double roundedPrefixMass = Double.parseDouble(df.format(prefixMass));
                    Double roundedSuffixMass = Double.parseDouble(df.format(suffixMass));

                    //System.out.println("Errors: " + roundedPrefixMass + " \t" + roundedSuffixMass);
                    Integer prefixErrorCount = null;
                    if (!mapPrefixErrorsToCount.containsKey(roundedPrefixMass)) {
                        prefixErrorCount = 1;
                    }
                    else {
                        prefixErrorCount = mapPrefixErrorsToCount.get(roundedPrefixMass);
                        prefixErrorCount = prefixErrorCount + 1;
                    }
                    mapPrefixErrorsToCount.put(roundedPrefixMass, prefixErrorCount);

                    Integer suffixErrorCount = null;
                    if (!mapSuffixErrorsToCount.containsKey(roundedSuffixMass)) {
                        suffixErrorCount = 1;
                    }
                    else {
                        suffixErrorCount = mapSuffixErrorsToCount.get(roundedSuffixMass);
                        suffixErrorCount = suffixErrorCount + 1;
                    }
                    mapSuffixErrorsToCount.put(roundedSuffixMass, suffixErrorCount);

                    if (Math.abs(prefixMass) < Constants.TAG_MATCH_ERROR && Math.abs(suffixMass) < Constants.TAG_MATCH_ERROR) {
                        tagErrorScore *= 1000;
                    }
                    else if (Math.abs(prefixMass) < Constants.TAG_MATCH_ERROR || Math.abs(suffixMass) < Constants.TAG_MATCH_ERROR) {
                        tagErrorScore *= 300;
                    }

                    pepScore += tagErrorScore;
                }
                pepScore /= 1000;

                //Now we work out the most frequent prefix and suffix errors
                Double mostFreqPrefixError = 0.0;
                int countMostFreqPrefixError = 0;
                for (Double prefixError : mapPrefixErrorsToCount.keySet()) {

                    int count = mapPrefixErrorsToCount.get(prefixError);

                    if (count > countMostFreqPrefixError) {
                        countMostFreqPrefixError = count;
                        mostFreqPrefixError = prefixError;
                    }
                }

                Double mostFreqSuffixError = 0.0;
                int countMostFreqSuffixError = 0;
                for (Double suffixError : mapSuffixErrorsToCount.keySet()) {
                    int count = mapSuffixErrorsToCount.get(suffixError);
                    if (count > countMostFreqSuffixError) {
                        countMostFreqSuffixError = count;
                        mostFreqSuffixError = suffixError;
                    }
                }

                List<String> protAccs = peptideToAccs.get(seq);
                String accs = protAccs.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", ";");
                if (pepScore > Constants.MIN_SCORE_THRESHOLD) {
                    writerRes.write(specID + "," + accs + "," + seq + "," + pepScore + "," + mostFreqPrefixError + "," + countMostFreqPrefixError + "," + mostFreqSuffixError + "," + countMostFreqSuffixError + "\n");
                }
            }

        }
        catch (Exception e) {
        }

    }

}
