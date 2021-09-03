import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonStart;
    private JButton buttonStop;
    private JCheckBox checkBox_AutoStop;
    private JTextField textField_TimeValue;

    private boolean moverTimerEnabled = false;

    private int MAX_HEIGHT = 0;
    private int MAX_WIDTH = 0;
    private Robot robot = null;
    private Random rand = new Random();
    private TimerTask taskMove = null;
    private Timer timerAutoStop = null;
    private SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    // Т.к. у Skype таймаут 5 минут
    final private long PERIOD = 4L * 60 * 1000;

    public boolean isMoverTimerEnabled() {
        return moverTimerEnabled;
    }

    public void setTimerEnable() {
        this.moverTimerEnabled = true;
    }

    public void setTimerDiable() {
        this.moverTimerEnabled = false;
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

    public void autoStopTimerCreate(String timeValue) {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                printLog("autoStopTimerCreate.Go...");
                buttonStopAction();
            }
        };

        // https://spec-zone.ru/RU/Java/Docs/7/api/java/util/Timer.html
        timerAutoStop = new Timer("AutoStop timer");
        SimpleDateFormat formatForDateStop = new SimpleDateFormat("dd.MM.yyyy");
        Date dateStop = new Date();
        String sDateStop = formatForDateStop.format(dateStop) + " " + timeValue;
        printLog("Auto timer On. Next stop at: " + sDateStop);
        formatForDateStop.applyPattern("dd.MM.yyyy HH:mm");
        try {
            dateStop = formatForDateStop.parse(sDateStop);
        } catch (ParseException e) {
            printLog("Указано не корректное время: AutoStopTimer -> " + timeValue);
            return;
        }
        timerAutoStop.schedule(repeatedTask, dateStop);
    }

    public void autoStopTimerOff() {
        printLog("Auto timer Off");
        if (timerAutoStop != null) {
            timerAutoStop.cancel();
            timerAutoStop = null;
            //checkBox_AutoStop.setSelected(false);
        }
    }

    void buttonStartAction() {
        // Нажатие кнопка "Старт"
        printLog("Start");
        setTimerEnable();
        setEnableButtons();
        taskMove = createTimer();

        boolean bStarted = checkBox_AutoStop.isSelected();
        if (bStarted) {
            // Включаем "Авто выход"
            autoStopTimerCreate(textField_TimeValue.getText());
        }

        checkBox_AutoStop.setEnabled(false);
        textField_TimeValue.setEnabled(false);

    }

    void buttonStopAction() {
        // Нажатие кнопка "Стоп"
        printLog("Stop");
        setTimerDiable();
        if (taskMove != null) {
            taskMove.cancel();
            taskMove = null;
        }
        setEnableButtons();

        // Выключаем "Авто выход"
        autoStopTimerOff();

//        checkBox_AutoStop.setSelected(false);
        checkBox_AutoStop.setEnabled(true);
//        textField_TimeValue.setEnabled(true);
    }

    void checkBox_AutoStopAction() {
        // Переключатель "Авто"
        boolean bStarted = checkBox_AutoStop.isSelected();
        //printLog("addActionListener => " + bStarted);
        if (bStarted) {
//            // Включаем "Авто выход"
//            autoStopTimerCreate(textField_TimeValue.getText());
            buttonStart.setEnabled(true);
            if (!checkTimeString(textField_TimeValue.getText())) {
                printLog("Указано не корректное время: AutoStopTimer");
                checkBox_AutoStop.setSelected(false);
                return;
            }
            textField_TimeValue.setEnabled(false);
        } else {
//            // Выключаем "Авто выход"
//            autoStopTimerOff();
            buttonStart.setEnabled(false);
            textField_TimeValue.setEnabled(true);
        }
    }

    boolean checkTimeString(String timeValue) {
        Pattern pattern = Pattern.compile("^[0-2][0-9]:[0-6][0-9]$");
        Matcher matcher = pattern.matcher(timeValue);
        return matcher.find();
    }

    ;

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
                buttonStartAction();
            }
        });

        buttonStop.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                buttonStopAction();
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

        checkBox_AutoStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBox_AutoStopAction();
            }
        });

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
        checkBox_AutoStopAction();

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
        buttonStart.setEnabled(!moverTimerEnabled);
        buttonStop.setEnabled(moverTimerEnabled);
    }

    public TimerTask createTimer() {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
//                System.out.println("Go...");
                if (isMoverTimerEnabled()) {
                    moveCursor();
//                } else {
//                    System.out.println("Timer break");
//                    setEnableButtons();
//                    cancel();
                }
            }
        };
//        System.out.println("Timer create... ");
        Timer timer = new Timer("MainWindow MoverTimer");
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
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        checkBox_AutoStop = new JCheckBox();
        checkBox_AutoStop.setSelected(true);
        checkBox_AutoStop.setText("Авто остановка");
        panel3.add(checkBox_AutoStop, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField_TimeValue = new JTextField();
        textField_TimeValue.setText("18:10");
        panel3.add(textField_TimeValue, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    /*
    Во-первых, для понимания процесса лучше начать с того. что зайти в меню IDEA
    «File -> Settings» — там «Editor -> GUI Designer» и установить флажок Generate GUI Into: в Java source code.
    (это немного поможет пониманию процесса на первом этапе — потом можно будет убрать обратно).
     */

}
