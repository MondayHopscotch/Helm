package com.bitdecay.game.desktop.editor.file;

import com.bitdecay.game.persist.JsonUtils;
import com.bitdecay.game.world.LevelDefinition;

import javax.swing.*;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.io.*;

public class FileUtils {

    public static String lastTouchedFileName = "";

    private static String lastTouchedDirectory = ".";

    public static String saveLevelToFile(LevelDefinition levelDef) {
        return saveToFile(JsonUtils.marshalLevel(levelDef));
    }

    public static String saveToFile(String json) {
        JFileChooser fileChooser = new JFileChooser(lastTouchedDirectory) {
            @Override
            protected JDialog createDialog(Component parent) throws HeadlessException {
                JDialog dialog = super.createDialog(parent);
                dialog.setAlwaysOnTop(true);
                dialog.setModalityType(ModalityType.APPLICATION_MODAL);
                dialog.setModal(true);
                return dialog;
            }
        };
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Save As");
        fileChooser.setApproveButtonText("Save");
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            lastTouchedFileName = fileChooser.getSelectedFile().getName();
            lastTouchedDirectory = fileChooser.getSelectedFile().getParent();
            try {
                FileWriter writer = new FileWriter(fileChooser.getSelectedFile());
                writer.write(json);
                writer.flush();
                writer.close();
                return fileChooser.getSelectedFile().getName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static LevelDefinition loadLevelFromFile() {
        String asJson = loadFile();
        if (asJson == null) {
            return null;
        } else {
            return JsonUtils.unmarshalLevel(asJson);
        }
    }

    public static String loadFile() {
        JFileChooser fileChooser = new JFileChooser(lastTouchedDirectory);
        fileChooser.setApproveButtonText("Load");

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            lastTouchedFileName = selectedFile.getName();
            lastTouchedDirectory = selectedFile.getParent();
            return loadFile(selectedFile);
        }
        return null;
    }

    public static String loadFile(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            StringBuffer json = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                json.append(line);
                line = reader.readLine();
            }
            if (json.length() > 0) {
                return json.toString();
            } else {
                System.out.println("File was empty. Could not load.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }


    public static void setFileChooserWorkingDirectory(String workingDirectory){
        lastTouchedDirectory = workingDirectory;
        lastTouchedFileName = "";
    }
}