package uk.ac.liv.pepregexengine.gui;

import uk.ac.liv.pepregexengine.gui.listener.PeakFilterListener;
import uk.ac.liv.pepregexengine.gui.listener.LoadMgfFileListener;
import uk.ac.liv.pepregexengine.gui.listener.OutputDirectoryListener;
import uk.ac.liv.pepregexengine.gui.listener.MassUnitListener;
import uk.ac.liv.pepregexengine.gui.listener.MassTolListener;
import uk.ac.liv.pepregexengine.gui.listener.DecimalListener;
import uk.ac.liv.pepregexengine.gui.listener.RunSearchListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import uk.ac.liv.pepregexengine.gui.listener.FastaFileListener;
import uk.ac.liv.pepregexengine.gui.listener.SpectrumTagListener;

/**
 *
 * @author Da Qi
 */
public class MainFrame extends javax.swing.JFrame {

    private static Map<String, JTextComponent> textComponentMap = new HashMap<>();
    private static Map<String, JSpinner> spinnerMap = new HashMap<>();
    private static Map<String, JComboBox> comboMap = new HashMap<>();
    private static Map<String, JCheckBox> checkBoxMap = new HashMap<>();

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        mgfPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfMgfFile = new javax.swing.JTextField();
        btMgfFile = new javax.swing.JButton();
        fastaPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        tfFastaFile = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btFastaFile = new javax.swing.JButton();
        optionPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        spMassTol = new javax.swing.JSpinner();
        cbMassUnit = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cbSpectrumTag = new javax.swing.JCheckBox();
        outputPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        tfOutputDir = new javax.swing.JTextField();
        btOutputDir = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        btRun = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Proteoformer-TD-alpha");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new java.awt.GridLayout(5, 1));

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 10, 10);
        flowLayout1.setAlignOnBaseline(true);
        mgfPanel.setLayout(flowLayout1);

        jLabel1.setText("MGF File:");
        mgfPanel.add(jLabel1);

        tfMgfFile.setEditable(false);
        tfMgfFile.setPreferredSize(new java.awt.Dimension(403, 25));
        mgfPanel.add(tfMgfFile);
        textComponentMap.put("tfMgfFile", tfMgfFile);

        btMgfFile.setText("Browse");
        mgfPanel.add(btMgfFile);
        btMgfFile.addActionListener(new LoadMgfFileListener());

        jPanel1.add(mgfPanel);

        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 10, 10);
        flowLayout2.setAlignOnBaseline(true);
        fastaPanel.setLayout(flowLayout2);

        jPanel3.setLayout(new java.awt.GridLayout(2, 1));

        jLabel2.setText("FASTA File:");
        jPanel3.add(jLabel2);

        fastaPanel.add(jPanel3);

        jPanel2.setLayout(new java.awt.GridLayout(2, 1));

        tfFastaFile.setEditable(false);
        tfFastaFile.setPreferredSize(new java.awt.Dimension(340, 25));
        jPanel2.add(tfFastaFile);
        textComponentMap.put("tfFastaFile", tfFastaFile);

        jLabel4.setText("Warning: DO select 'Output Spectrum Tag File when FASTA File is not designated.");
        jPanel2.add(jLabel4);

        fastaPanel.add(jPanel2);

        jPanel4.setLayout(new java.awt.GridLayout(2, 1));

        btFastaFile.setText("Browse");
        jPanel4.add(btFastaFile);
        btFastaFile.addActionListener(new FastaFileListener());

        fastaPanel.add(jPanel4);

        jPanel1.add(fastaPanel);

        optionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));
        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 10, 10);
        flowLayout3.setAlignOnBaseline(true);
        optionPanel.setLayout(flowLayout3);

        jLabel3.setText("Mass tol: ");
        optionPanel.add(jLabel3);

        spMassTol.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(10.0d), Double.valueOf(0.0d), null, Double.valueOf(0.01d)));
        spMassTol.setPreferredSize(new java.awt.Dimension(50, 20));
        optionPanel.add(spMassTol);
        spinnerMap.put("spMassTol",spMassTol);

        spMassTol.addChangeListener(new MassTolListener());

        cbMassUnit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ppm", "Da" }));
        optionPanel.add(cbMassUnit);
        cbMassUnit.addActionListener(new MassUnitListener());

        comboMap.put("cbMassUnit", cbMassUnit);
        optionPanel.add(jLabel9);
        optionPanel.add(jLabel10);
        optionPanel.add(jLabel11);

        cbSpectrumTag.setSelected(true);
        cbSpectrumTag.setText("Output Spectrum Tag File");
        optionPanel.add(cbSpectrumTag);
        cbSpectrumTag.addChangeListener(new SpectrumTagListener());
        checkBoxMap.put("cbSpectrumTag", cbSpectrumTag);

        jPanel1.add(optionPanel);

        java.awt.FlowLayout flowLayout4 = new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 10, 10);
        flowLayout4.setAlignOnBaseline(true);
        outputPanel.setLayout(flowLayout4);

        jLabel8.setText("Output Files Directory:");
        outputPanel.add(jLabel8);

        tfOutputDir.setPreferredSize(new java.awt.Dimension(338, 25));
        outputPanel.add(tfOutputDir);
        textComponentMap.put("tfOutputDir", tfOutputDir);

        btOutputDir.setText("Browse");
        outputPanel.add(btOutputDir);
        btOutputDir.addActionListener(new OutputDirectoryListener());

        jPanel1.add(outputPanel);

        jPanel7.setLayout(new java.awt.BorderLayout());

        btRun.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btRun.setText("Start");
        jPanel7.add(btRun, java.awt.BorderLayout.CENTER);
        btRun.addActionListener(new RunSearchListener());
        jPanel7.add(jProgressBar1, java.awt.BorderLayout.PAGE_END);

        jPanel1.add(jPanel7);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }

        });
    }

    public static JTextComponent getTextComponentByName(String name) {
        if (textComponentMap.containsKey(name)) {
            return textComponentMap.get(name);
        } else {
            return null;
        }
    }

    public static JSpinner getSpinnerByName(String name) {
        if (spinnerMap.containsKey(name)) {
            return spinnerMap.get(name);
        } else {
            return null;
        }
    }

    public static JComboBox getComboBoxByName(String name) {
        if (comboMap.containsKey(name)) {
            return comboMap.get(name);
        } else {
            return null;
        }
    }

    public static JCheckBox getCheckBoxByName(String name) {
        if (checkBoxMap.containsKey(name)) {
            return checkBoxMap.get(name);
        } else {
            return null;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btFastaFile;
    private javax.swing.JButton btMgfFile;
    private javax.swing.JButton btOutputDir;
    private javax.swing.JButton btRun;
    private javax.swing.JComboBox cbMassUnit;
    private javax.swing.JCheckBox cbSpectrumTag;
    private javax.swing.JPanel fastaPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JPanel mgfPanel;
    private javax.swing.JPanel optionPanel;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JSpinner spMassTol;
    private javax.swing.JTextField tfFastaFile;
    private javax.swing.JTextField tfMgfFile;
    private javax.swing.JTextField tfOutputDir;
    // End of variables declaration//GEN-END:variables
}
