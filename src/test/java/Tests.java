import org.example.Ls;


import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {


    @Test
    void testGetPermissionsString() {
        Ls ls = new Ls();
        assertEquals("rwx", ls.getPermissionsString(new File("File/test")));
        assertEquals("---", ls.getPermissionsString(new File("test2.txt")));
    }

    @Test
    void testGetSizeByte() {
        Ls ls = new Ls();
        assertEquals("16", ls.getSizeString(
                new File("File/ex1")
        ));
        assertEquals("0", ls.getSizeString(new File("test2.txt")));
    }

    @Test
    void testGetHumanReadableSize() {
        Ls ls = new Ls();
        assertEquals("4 KB", ls.getHumanReadableSizeString(new File(
                "src/main/java/org/example/Ls.java"
        )));
        assertEquals("0 B", ls.getHumanReadableSizeString(new File("test2.txt")));
    }

    @Test
    void testGetLastModifiedString() throws IOException {
        Ls ls = new Ls();
        assertEquals("2023-04-15 00:34:22", ls.getLastModifiedString(new File(
                "File/ex3"
        )));
        assertThrows(IOException.class, () -> {ls.getLastModifiedString(new File("test2.txt"));});
    }

    @Test
    void testExecute() throws Exception {
        Ls ls = new Ls();

        String[] args = new String[]{"-l", "File/ex1"};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        ls.execute(args);
        assertEquals(ls.longFormat("ex1"), output.toString());
    }

    @Test
    void testExecute2() throws Exception {
        Ls ls = new Ls();

        String[] args = {"-h", "File/ex2"};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        ls.execute(args);
        assertEquals(ls.humanReadable("ex2"), output.toString());


        args = new String[]{"-l", "gradle/wrapper"};
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        ls.execute(args);
        assertEquals("""
                gradle-wrapper.jar 111 2023-04-02 20:08:03 60756
                gradle-wrapper.properties 111 2023-04-02 20:08:03 202
                """, output.toString());
    }

    @Test
    void testExecute3() throws Exception {
        Ls ls = new Ls();

        String[] args = {"src/main/java/org/example"};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        ls.execute(args);
        assertEquals("Ls.java\nMain.java\n", output.toString());
    }
}
