import java.io.IOException;
import java.util.Collection;

public interface Folder {

    Collection<File> getFiles();
    void copyFile(File file) throws IOException;
    Folder getChildFolder(String folderName);
    String getPath();


}
