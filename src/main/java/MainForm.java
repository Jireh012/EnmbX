import org.apache.log4j.chainsaw.Main;

import javax.swing.*;

/**
 * @author last_
 */
public class MainForm {
    private JButton startButton;
    private JTextField sourceAdd;
    private JLabel sourceName;
    private JPanel panel1;
    private JLabel aimsTypeName;
    private JRadioButton aimsType1;
    private JRadioButton aimsType2;
    private JCheckBox ModiType1;
    private JCheckBox ModiType2;
    private JCheckBox ModiType3;
    private JCheckBox ModiType4;

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
