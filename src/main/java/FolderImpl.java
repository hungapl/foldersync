import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class FolderImpl implements Folder {

    private final java.io.File folder;

    public FolderImpl(java.io.File folderPath) {
        folder = folderPath;
    }

    public FolderImpl(String folderPath) {
        this(new java.io.File(folderPath));
    }

    public Collection<File> getFiles() {
        return FileUtils.listFiles(folder, null, true)
                .stream().map(f -> new File(f)).collect(Collectors.toList());
    }

    @Override
    public void copyFile(File file) throws IOException {
        String tempFile = folder.getPath() + java.io.File.separator + file.name + ".temp";
        java.io.File tempF = new java.io.File(tempFile);
        java.io.File destF = new java.io.File(folder.getPath() + java.io.File.separator + file.name);
        FileUtils.deleteQuietly(tempF);
        FileUtils.copyFile(file.getFile(), tempF);
        FileUtils.moveFile(tempF, destF);
    }

    public Folder getChildFolder(String folderName) {

        java.io.File childFolder = new java.io.File(this.folder.getPath() + java.io.File.separator + folderName);
        if (!childFolder.exists()) {
            childFolder.mkdir();
        }
        return new FolderImpl(childFolder);
    }

    @Override
    public String getPath() {
        return this.folder.getPath();
    }
}
