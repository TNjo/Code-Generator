import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class CodeGenUI extends JFrame {
    private JTextField configFilePathField;
    private JButton selectFileButton;
    private JButton downloadTemplateButton;
    private JButton generateButton;

    public CodeGenUI() {
        initialize();
    }

    private void initialize() {
        this.setBounds(100, 100, 450, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);

        JLabel lblConfigFilePath = new JLabel("Config File Path:");
        lblConfigFilePath.setBounds(10, 20, 100, 25);
        this.getContentPane().add(lblConfigFilePath);

        configFilePathField = new JTextField();
        configFilePathField.setBounds(120, 20, 150, 25);
        this.getContentPane().add(configFilePathField);
        configFilePathField.setColumns(10);

        selectFileButton = new JButton("Select File");
        selectFileButton.setBounds(280, 20, 120, 25);
        this.getContentPane().add(selectFileButton);

        downloadTemplateButton = new JButton("Download Template");
        downloadTemplateButton.setBounds(10, 60, 200, 25);
        this.getContentPane().add(downloadTemplateButton);

        generateButton = new JButton("Generate");
        generateButton.setBounds(220, 60, 120, 25);
        this.getContentPane().add(generateButton);


        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(CodeGenUI.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    configFilePathField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        downloadTemplateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadTemplate();
            }
        });
    }

    private void downloadTemplate() {
        try {

            File templateFile = new File("Configs/config.properties");

            if (templateFile.exists()) {
                // Specify the target file location
                File targetFile = new File("config.properties");
                // Use Files.copy to copy the content of the template file to the target file
                Files.copy(templateFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(this, "Template downloaded successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Template not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to download template.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public String getConfigFilePath() {
        return configFilePathField.getText();
    }

    public void addGenerateButtonListener(ActionListener listener) {
        generateButton.addActionListener(listener);
    }
}
