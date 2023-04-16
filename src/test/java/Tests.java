import org.example.Ls;


import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {


    @Test
    void testGetPermissionsString() {
        Ls ls = new Ls();
        assertEquals("rwx", ls.getPermissionsString(new File("C:\\Users\\user\\IdeaProjects\\lab2\\test")));
        assertEquals("---", ls.getPermissionsString(new File("test2.txt")));
    }

    @Test
    void testGetSizeByte() {
        Ls ls = new Ls();
        assertEquals("16", ls.getSizeString(
                new File("C:\\Users\\user\\IdeaProjects\\lab2\\src\\main\\java\\org\\example\\ex1")
        ));
        assertEquals("0", ls.getSizeString(new File("test2.txt")));
    }

    @Test
    void testGetHumanReadableSize() {
        Ls ls = new Ls();
        assertEquals("4 KB", ls.getHumanReadableSizeString(new File(
                "C:\\Users\\user\\IdeaProjects\\lab2\\src\\main\\java\\org\\example\\Ls.java"
        )));
        assertEquals("0 B", ls.getHumanReadableSizeString(new File("test2.txt")));
    }

    @Test
    void testGetLastModifiedString() {
        Ls ls = new Ls();
        assertEquals("2023-04-15 00:34:22", ls.getLastModifiedString(new File(
                "C:\\Users\\user\\IdeaProjects\\lab2\\src\\main\\java\\org\\example\\ex3"
        )));
        assertEquals("The file doesn't exist", ls.getLastModifiedString(new File("test2.txt")));
    }

    @Test
    void testExecute() throws Exception {
        Ls ls = new Ls();


        String[] args = {"-h", "test.txt"};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        ls.execute(args);
        assertEquals("test.txt The file doesn't exist", output.toString());


        args = new String[]{"-l", "C:\\Users\\user\\IdeaProjects\\lab2\\src\\main\\java\\org\\example\\ex1"};
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        ls.execute(args);
        assertEquals(ls.longFormat("ex1"), output.toString());
    }

    @Test
    void testExecute2() throws Exception {
        Ls ls = new Ls();

        String[] args = {"-h", "C:\\Users\\user\\IdeaProjects\\lab2\\src\\main\\java\\org\\example\\ex2"};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        ls.execute(args);
        assertEquals(ls.humanReadable("ex2"), output.toString());


        args = new String[]{"-l", "C:\\Users\\user\\IdeaProjects\\lab2\\gradle\\wrapper"};
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

        String[] args = {"C:\\Users\\user\\IdeaProjects\\lab2\\src\\main\\java\\org\\example"};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        ls.execute(args);
        assertEquals("ex1\nex2\nex3\nLs.java\nMain.java\n", output.toString());
    }
}
