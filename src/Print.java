import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.TrayIcon.MessageType;


public class Print extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<String> list1;
    private JList<String> list2;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private TrayIcon trayIcon;

    Print() {
        list1.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list1.setModel(getPrinterListModel());
        list2.setModel(listModel);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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
    }

    private static DefaultListModel<String> getPrinterListModel() {

        DefaultListModel<String> listModel = new DefaultListModel<>();
        PrintService[] findAllPrinters = findAllPrinters();
        for (int i = 0; i < findAllPrinters.length; i++) {
            PrintService printService = findAllPrinters[i];
            listModel.addElement((i+1) + ". " + printService.getName());
        }

        return listModel;
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
        System.exit(0);
    }

    private PrintService findPrinterByName(String printerName) {
        PrintService[] printServices = findAllPrinters();

        for (PrintService printService : printServices) {
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }

        return  PrintServiceLookup.lookupDefaultPrintService();
    }

    private static PrintService[] findAllPrinters() {
        return PrintServiceLookup.lookupPrintServices((DocFlavor) null, (AttributeSet) null);
    }

    public boolean printUsingPrinter(String printerName, String content) throws PrintException {
        PrintService printer = findPrinterByName(printerName);
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        DocPrintJob job = printer.createPrintJob();
        Doc doc = new SimpleDoc(content.getBytes(),flavor,null);
        job.print(doc, null);
        addLog("Print Successful", "DEBUG");
        return true;
    }

    public void addLog(String log, String type) {
        listModel.addElement(type + ": " + log);
        if (type.equals("INFO")) {
            trayIcon.displayMessage("Print Utility", log, MessageType.INFO);
        }
        if (type.equals("ERROR")) {
            trayIcon.displayMessage("Print Utility", log, MessageType.ERROR);
        }
    }

    public void setTrayIcon() throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("printer.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getToolkit().createImage(getClass().getResource("icon.png"));

        trayIcon = new TrayIcon(image, "Print Utility");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("Print Utility");
        tray.add(trayIcon);

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setAlwaysOnTop(true);
                setVisible(true);
            }
        });
    }
}
