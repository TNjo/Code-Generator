import freemarker.template.TemplateException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;

public class CodeGen {
    private CodeGenUI codeGenUI;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CodeGen window = new CodeGen();
                window.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void run() {
        codeGenUI = new CodeGenUI();
        codeGenUI.setVisible(true);
        codeGenUI.addGenerateButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String configFilePath = codeGenUI.getConfigFilePath();
                generateFiles(configFilePath);
            }
        });
    }

    private void generateFiles(String configFilePath) {
        Properties properties = new Properties();

        try {
            File configFile = new File(configFilePath);
            System.out.println("Reading configuration from: " + configFile.getAbsolutePath());
            properties.load(new FileInputStream(configFile));

            CodeGenRules.generateClientFile(properties);
            CodeGenRules.generateProjectionFile(properties);

            JOptionPane.showMessageDialog(codeGenUI, "Files have been generated successfully.");
        } catch (TemplateException e) {
            JOptionPane.showMessageDialog(codeGenUI, "An error occurred during template processing.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(codeGenUI, "An error occurred while reading the configuration file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
