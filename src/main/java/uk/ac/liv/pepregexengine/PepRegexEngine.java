
package uk.ac.liv.pepregexengine;

import uk.ac.liv.pepregexengine.mgfreader.MgfReader;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.math.NumberUtils;
import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;
import uk.ac.liv.pepregexengine.command.CliConstants;
import uk.ac.liv.pepregexengine.data.constants.Constants;
import uk.ac.liv.pepregexengine.data.tolerance.MassTolerance;
import uk.ac.liv.pepregexengine.view.MainFrame;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 09-Jul-2015 13:09:07
 */
public class PepRegexEngine {

    public static void main(String[] args) {

        //b ion simulation
//        String sequence = "MDKKSARIRRATRARRKLKELGATRLVVHRTPRHIYAQVIAPNGSEVLVAASTVEKAIAEQLKYTGNKDAAAAVGKAVAERALEKGIKDVSFDRSGFQYHGRVQALADAAREAGLQF";
//        BIonGenerator bIG = new BIonGenerator(sequence);
//
//        try {
//            bIG.writeDetail("Y:/ddq/TopDownData_from_SamPayne/PepRegexEngine-Test/3177/b ion simulation/3177-b-ion-simulation.csv");
//        }
//        catch (IOException ex) {
//            Logger.getLogger(PepRegexEngine.class.getName()).log(Level.SEVERE, null, ex);
//        }
        //define of command line options
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        String helpOpt = "h";
        options.addOption(helpOpt, false, CliConstants.HELP_DESCRIPTION);

        String fastaOpt = "fasta-file";
        options.addOption(Option.builder("f")
                .hasArg()
                .argName("file")
                .longOpt(fastaOpt)
                //                .required(false)    //TODO: fasta is not require for testing. If fasta file is not provided, the database searching will not be performed.
                .desc(CliConstants.FASTA_DESCRIPTION)
                .build());

        String mgfOpt = "mgf-file";
        options.addOption(Option.builder("m")
                .hasArg()
                .argName("file")
                .longOpt(mgfOpt)
                //                .required(false)
                .desc(CliConstants.MGF_DESCRIPTION)
                .build());

        String massTolOpt = "mass-tolerance";
        options.addOption(Option.builder("mt")
                .hasArg()
                .argName("mass tolerance")
                .longOpt(massTolOpt)
                .desc(CliConstants.MASS_TOLERANCE_DESCRIPTION)
                .build());

        String massUnitOpt = "mass-unit";
        options.addOption(Option.builder("mu")
                .hasArg()
                .argName("mass unit")
                .longOpt(massUnitOpt)
                .desc(CliConstants.MASS_UNIT_DESCRIPTION)
                .build());

        String dfOpt = "decimal-place";
        options.addOption(Option.builder("dp")
                .hasArg()
                .argName("decimal place")
                .longOpt(dfOpt)
                .build());

        String resOpt = "result-file";
        options.addOption(Option.builder("o")
                .hasArg()
                .argName("file")
                .longOpt(resOpt)
                //                .required(false)
                .desc(CliConstants.RES_DESCRIPTION)
                .build());

        String resFullOpt = "result-full-file";
        options.addOption(Option.builder("u")
                .hasArg()
                .argName("file")
                .longOpt(resFullOpt)
                .desc(CliConstants.RES_FULL_DESCRIPTION)
                //                .required(false)
                .build());

        String tagOpt = "output-tag-file";
        options.addOption(Option.builder("t")
                .hasArg()
                .argName("file")
                .desc(CliConstants.TAG_FILE_DESCRIPTION)
                .longOpt(tagOpt)
                .required(false)
                .build());

        String filterOpt = "peak-filter";
        options.addOption(Option.builder("pf")
                .hasArg()
                .argName("positive integer")
                .longOpt(filterOpt)
                .required(false)
                .desc(CliConstants.FILTER_DESCRIPTION)
                .build());

        String GUIOpt = "gui";
        options.addOption(Option.builder("g")
                .longOpt(GUIOpt)
                .required(false)
                .desc(CliConstants.GUI_DESCRIPTION)
                .build());

        try {
//            AAMap.writeRegexAAMap("Y:/ddq/TopDownData_from_SamPayne/PepRegexEngine-Test/AAMap.csv");         
            //real data test arguments (Sam Payne)
//            args = new String[10];
//            args[0] = "-f";
//            args[1] = "Y:/ddq/TopDownData_from_SamPayne/PepRegexEngine-Test/3177/3177.fasta";
//            args[2] = "-m";
//            args[3] = "Y:/ddq/TopDownData_from_SamPayne/PepRegexEngine-Test/3177/3177.mgf";
//            args[4] = "-o";
//            args[5] = "Y:/ddq/TopDownData_from_SamPayne/PepRegexEngine-Test/3177/3177_results_debug.csv";
//            args[6] = "-u";
//            args[7] = "Y:/ddq/TopDownData_from_SamPayne/PepRegexEngine-Test/3177/3177_results_full.debug.csv";
//            args[8] = "-t";
//            args[9] = "Y:/ddq/TopDownData_from_SamPayne/PepRegexEngine-Test/3177/3177-SpectrumTagTable_debug.csv";

            //simulation arguments
//            args = new String[10];
//            args[0] = "-f";
//            args[1] = "Y:/ddq/TopDownData_from_SamPayne/PepRegexEngine-Test/3177/3177.fasta";
//            args[2] = "-m";
//            args[3] = "Y:/ddq/TopDownData_from_SamPayne/PepRegexEngine-Test/3177/b ion simulation/3177-b-ion-peak-list-simulation.mgf";
//            args[4] = "-o";
//            args[5] = "Y:/ddq/TopDownData_from_SamPayne/PepRegexEngine-Test/3177/b ion simulation/3177_b_ion_simulation_results_debug.csv";
//            args[6] = "-u";
//            args[7] = "Y:/ddq/TopDownData_from_SamPayne/PepRegexEngine-Test/3177/b ion simulation/3177_b_ion_simulation_results_full.debug.csv";
//            args[8] = "-t";
//            args[9] = "Y:/ddq/TopDownData_from_SamPayne/PepRegexEngine-Test/3177/b ion simulation/3177-b_ion_SpectrumTagTable_debug.csv";
            //PXD001845 data
//            args = new String[10];
//            args[0] = "-f";
//            args[1] = "Y:/ddq/TopDownData_Proteomexchange/PXD001845/fasta/PXD001845.fasta";
//            args[2] = "-m";
//            //mgf from Xtract output
//            //args[3] = "Y:/ddq/TopDownData_Proteomexchange/PXD001845/Xtract/20141202_AMB_Bora_10x_40MeOH_1FA_OT_120k_10uscans_920_ETD_8ms_19precZ-Xtract.mgf";
//            //mgf from DeconTools
//            //args[3] = "Y:/ddq/TopDownData_Proteomexchange/PXD001845/DeconTools/20141202_AMB_Bora_10x_40MeOH_1FA_OT_120k_10uscans_920_ETD_8ms_19precZ.mgf";
//            //mgf from MsDeconv
//            args[3] = "Y:/ddq/TopDownData_Proteomexchange/PXD001845/MsDeconv-mzXML_with_precursor_charge/20141202_AMB_Bora_10x_40MeOH_1FA_OT_120k_10uscans_920_ETD_8ms_19precZ_msdeconv.mgf";
//            args[4] = "-o";
//            args[5] = "Y:/ddq/TopDownData_Proteomexchange/PXD001845/TagGeneration/20141202_AMB_Bora_10x_40MeOH_1FA_OT_120k_10uscans_920_ETD_8ms_19precZ_results_debug.csv";
//            args[6] = "-u";
//            args[7] = "Y:/ddq/TopDownData_Proteomexchange/PXD001845/TagGeneration/20141202_AMB_Bora_10x_40MeOH_1FA_OT_120k_10uscans_920_ETD_8ms_19precZ_results_full.debug.csv";
//            args[8] = "-t";
//            args[9] = "Y:/ddq/TopDownData_Proteomexchange/PXD001845/TagGeneration/20141202_AMB_Bora_10x_40MeOH_1FA_OT_120k_10uscans_920_ETD_8ms_19precZ_SpectrumTagTable_debug.csv";
            //Matthias data
            // parse command line
            CommandLine line = parser.parse(options, args);

            // interrogation stage
            if (line.getOptions().length == 0 || line.hasOption(helpOpt)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("PepRegexEngine", options);
            }
            else if (line.hasOption(GUIOpt)) {
                MainFrame.main(args);
            }
            else {
                String fastaFile = line.getOptionValue("f");
                String mgfFile = line.getOptionValue("m");
                String resOutput = line.getOptionValue("o");
                String resFullOutput = line.getOptionValue("u");
                String massTol = line.getOptionValue("mt");
                String massUnit = line.getOptionValue("mu");
                String decPlace = line.getOptionValue("dp");

                if (mgfFile == null) {
                    throw new IllegalArgumentException("No input MGF file argument!");
                }
                if (resOutput == null) {
                    throw new IllegalArgumentException("No output file argument!");
                }
                if (resFullOutput == null) {
                    throw new IllegalArgumentException("No full output file argument!");
                }
                int filterNumber = 0;
                if (line.hasOption("pf")) {
                    filterNumber = Integer.parseInt(line.getOptionValue("pf"));
                };

                MassTolerance mt = new MassTolerance();
                if (massTol == null || massUnit == null) {
                    mt.setDelta(0.01);
                    mt.setUnit(Constants.DALTON);
                }
                else if (!NumberUtils.isNumber(massTol) || !massUnit.equalsIgnoreCase(Constants.DALTON) && !massUnit.equalsIgnoreCase(Constants.PPM)) {
                    throw new IllegalArgumentException("Mass tolerance setting is wrong.");
                }
                else {
                    mt.setDelta(Double.parseDouble(massTol));
                    mt.setUnit(massUnit);
                }
                System.out.println("Start run with the params: fasta file: " + fastaFile + ", mgf file: " + mgfFile + ", result file: " + resOutput
                        + ", full result file: " + resFullOutput + ", peak filter number: " + filterNumber + ", " + mt.toString());

                String dfString = "#.##";
                if (decPlace != null && NumberUtils.isNumber(decPlace)) {
                    int m = Integer.parseInt(decPlace);
                    dfString = "#.";
                    for (int i = 0; i < m; i++) {
                        dfString += "#";
                    }
                }
                DecimalFormat df = new DecimalFormat(dfString);

                MgfReader mgfRd = new MgfReader(new File(mgfFile));

                //output Tag generation result
                if (line.hasOption("t")) {
                    String tagFile = "";
                    if (!line.getOptionValue("t").endsWith(".csv")) {
                        tagFile = line.getOptionValue("t").concat(".csv");
                    }
                    else {
                        tagFile = line.getOptionValue("t");
                    }
                    mgfRd.writePRMSpectruTags(tagFile, mt, df);
                }
                if (fastaFile != null) {
                    FastaReader fastaRd = new FastaReader(new File(fastaFile));
                    PRMToPeptidesMatcher ppMatcher = new PRMToPeptidesMatcher(fastaRd, mgfRd);
                    ppMatcher.writeResults(resOutput, resFullOutput);
                }
            }
        }
        catch (IOException ex) {
            Logger.getLogger(PepRegexEngine.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Could not open or find file: " + ex.getMessage());
        }
        catch (JMzReaderException ex) {
            Logger.getLogger(PepRegexEngine.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error in jmzReader: " + ex.getMessage());
        }
        catch (ParseException ex) {
            Logger.getLogger(PepRegexEngine.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
