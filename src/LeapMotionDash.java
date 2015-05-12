import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.DoubleSummaryStatistics;

/**
 * Created by NCS Customer on 3/31/2015.
 */
public class LeapMotionDash extends JFrame {

    public JTextField numberOfFingersTextField;
    private JPanel mainPanel;
    public JTextField zPosTextField;
    public JTextField yPosTextField;
    public JTextField xPosTextField;
    private JButton calibrateButton;
    private JTextField xOutputText;
    private JTextField yOutputText;
    private JTextField zOutputText;
    private JTextField yawRawText;
    private JTextField rotateOutputText;
    SHARPLeapMotionDrive controller;


    public void update()
    {
            numberOfFingersTextField.setText(Integer.toString(controller.valueMap.get("Fingers Extended").intValue()));
            xPosTextField.setText(Double.toString(controller.valueMap.get("X Position")));
            yPosTextField.setText(Double.toString(controller.valueMap.get("Y Position")));
            zPosTextField.setText(Double.toString(controller.valueMap.get("Z Position")));

            yawRawText.setText(Double.toString(controller.valueMap.get("Palm Roll")));

            xOutputText.setText(Double.toString(controller.table.getNumber("X Output")));
            yOutputText.setText(Double.toString(controller.table.getNumber("Y Output")));
            zOutputText.setText(Double.toString(controller.table.getNumber("Z Output")));

            rotateOutputText.setText(Double.toString(controller.table.getNumber("Rotate Output")));
    }

    public void setController(SHARPLeapMotionDrive controller)
    {
        this.controller = controller;
    }

    public LeapMotionDash()
    {
        super("Hello World");
        setContentPane(mainPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        init();
        calibrateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.setOrigin();
                super.mouseClicked(e);

            }
        });
    }

    private void createUIComponents() {
        numberOfFingersTextField = new JTextField();
        xPosTextField = new JTextField();
        yPosTextField = new JTextField();
        zPosTextField = new JTextField();
        rotateOutputText = new JTextField();
        yawRawText = new JTextField();
        mainPanel = new JPanel();

    }

    private void init()
    {

    }
}
