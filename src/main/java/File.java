public class File {

    private java.io.File file;

    long length;
    String name;

    public File(java.io.File f) {
        this.file = f;
        this.length = f.length();
        this.name = f.getName();
    }

    public boolean isSameLength(File anotherFile) {
        return this.name == anotherFile.name && this.length == anotherFile.length;
    }

    public boolean isSameName(File anotherFile) {
        return this.name == anotherFile.name;
    }

    public java.io.File getFile() {
        return this.file;
    }
}
