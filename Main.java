import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import ru.netology.pyas.savegame.GameProgress;

public class Main {
    
    private static final String SEP = File.separator;

    public static void main(String[] args) {
        String saveDir = "Game" + SEP + "savegames" + SEP;
        GameProgress[] progresses = {
            new GameProgress(100, 100, 1, 0.5),
            new GameProgress(50, 150, 3, -0.5),
            new GameProgress(1, 189, 5, 0)
        };
        String[] saveFiles = {
            saveDir + "save0.sav",
            saveDir + "save1.sav",
            saveDir + "save2.sav"
        };
        String zipPath = saveDir + "saves.zip";

        for (int i = 0; i < progresses.length; ++i) {
            saveGame(saveFiles[i], progresses[i]);
        }

        zipFiles(zipPath, saveFiles);
    }

    private static void saveGame(String path, GameProgress progress) {
        try (
            FileOutputStream fos = new FileOutputStream(path); 
            ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(progress);
        } catch (IOException ex) {
            System.err.println("Ошибка сохранения: " + ex.getMessage());
        }
    }

    private static void zipFiles(String path, String[] saveFiles) {
        try (
            FileOutputStream fos = new FileOutputStream(path); 
            ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            for (String saveFile : saveFiles) {
                File file = new File(saveFile);

                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zos.putNextEntry(entry);
                    byte[] buffer = new byte[(int)file.length()];
                    fis.read(buffer);
                    zos.write(buffer);
                    zos.closeEntry();
                }

                file.delete();
            }
        } catch (IOException | SecurityException ex) {
            System.err.println("Ошибка сохранения: " + ex.getMessage());
        }
    }

}
