package project.lab6.utils;

import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Images {
    private Images() {
    }

    private static String getPath(String folderName, Long id) {
        return String.format("data/%s/%s", folderName, id);
    }

    public static Image getImage(String folderName, String defaultImagePath, Long id) {
        String path = getPath(folderName, id);
        if (!Files.exists(Path.of(path)))
            path = defaultImagePath;
        else
            path = Path.of(path).toAbsolutePath().toString();
        return new Image(path);
    }

    /**
     * Saves a image in the specified folder with the specified id
     *
     * @param imagePath The path to the image to save
     */
    public static void saveImage(String folderName, Long id, String imagePath) throws IOException {
        //This saving in practice is just a copy
        String destinationPath = getPath(folderName, id);
        if (!Files.isDirectory(Path.of("data/" + folderName)))
            Files.createDirectories(Path.of("data/" + folderName));
        Files.copy(Path.of(imagePath), Path.of(destinationPath), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void deleteImage(String folderName, Long id) throws IOException {
        var path = getPath(folderName, id);
        Files.delete(Path.of(path));
    }
}
