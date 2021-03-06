import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonStart;
    private JButton buttonStop;

    private boolean timerEnabled = false;

    private int MAX_HEIGHT = 0;
    private int MAX_WIDTH = 0;
    private Robot robot = null;
    private Random rand = new Random();
    private TimerTask task;
    private SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    // Т.к. у Skype таймаут 5 минут
    final private long PERIOD = 5L * 60 * 1000;

    public boolean isTimerEnabled() {
        return timerEnabled;
    }

    public void setTimerEnable() {
        this.timerEnabled = true;
    }

    public void setTimerDiable() {
        this.timerEnabled = false;
    }

    public int getMaxHeight() {
        return MAX_HEIGHT;
    }

    public int getMaxWidth() {
        return MAX_WIDTH;
    }

    public void setMaxHeight(int MAX_HEIGHT) {
        this.MAX_HEIGHT = MAX_HEIGHT;
    }

    public void setMaxWidth(int MAX_WIDTH) {
        this.MAX_WIDTH = MAX_WIDTH;
    }

    public MainWindow() {
        setEnableButtons();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);
        printLog("Program Executed");

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        buttonStart.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                printLog("Start");
                setTimerEnable();
                setEnableButtons();
                task = createTimer();
            }
        });

        buttonStop.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                printLog("Stop");
                setTimerDiable();
                task.cancel();
                setEnableButtons();
//                buttonStop.setEnabled(false);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Get Srceen parameters
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        setMaxWidth(gd.getDisplayMode().getWidth());
        setMaxHeight(gd.getDisplayMode().getHeight());

        // Работ
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // Инициализация

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
        printLog("Exit");
    }

    public static void main(String[] args) {
        System.out.println("main...");

//        MainWindow dialog = new MainWindow();
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        MainWindow dialog = new MainWindow();
                        dialog.pack();
                        dialog.setVisible(true);
                        System.exit(0);
                    }
                }
        );

    }

    public void setEnableButtons() {
        buttonStart.setEnabled(!timerEnabled);
        buttonStop.setEnabled(timerEnabled);
    }

    public TimerTask createTimer() {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
//                System.out.println("Go...");
                if (isTimerEnabled()) {
                    moveCursor();
//                } else {
//                    System.out.println("Timer break");
//                    setEnableButtons();
//                    cancel();
                }
            }
        };
//        System.out.println("Timer create... ");
        Timer timer = new Timer("MainWindow timer");
        timer.scheduleAtFixedRate(repeatedTask, 0L, PERIOD);

        return repeatedTask;
    }

    private void moveCursor() {
        int height = rand.nextInt(getMaxHeight());
        int width = rand.nextInt(getMaxWidth());

        // Move the cursor
        printLog(String.format("Move to %4d x %4d", height, width));
        if (robot != null) {
            robot.mouseMove(height, width);
        }

    }

    private void printLog(String msg) {
        Date dateNow = new Date();
        System.out.println(String.format("%s | %s", formatForDateNow.format(dateNow), msg));
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Выход");
        panel2.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonStart = new JButton();
        buttonStart.setText("Старт");
        panel1.add(buttonStart, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonStop = new JButton();
        buttonStop.setText("Стоп");
        panel1.add(buttonStop, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
