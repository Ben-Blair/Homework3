import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.security.Permission;

public class TestUtils {
    public static String NameFileContext;
    public static String NameFile;

    // class constant for name file (Change the value only. Do NOT change the names of the constant)
    // Test with both "names.txt" and "names2.txt" (Before submission, change back to "names.txt")
    static final String NAME_FILENAME_1 = "names.txt";
    static final Integer STARTING_YEAR_1 = 1890;
    static final Integer DECADE_WIDTH_1 = 60;
    static final Integer LEGEND_HEIGHT_1 = 30;

    static final String NAME_FILENAME_2 = "names2.txt";
    static final Integer STARTING_YEAR_2 = 1863;
    static final Integer DECADE_WIDTH_2 = 50;
    static final Integer LEGEND_HEIGHT_2= 20;

    public static void cleanAllPngFiles() throws IOException {
        System.out.println("Entering TestUtils.cleanAllPngFiles");

        Files.deleteIfExists((new File("names-abbie-f-actual.png")).toPath());
        Files.deleteIfExists((new File("names-don-m-actual.png")).toPath());
        Files.deleteIfExists((new File("names-ethel-f-actual.png")).toPath());
        Files.deleteIfExists((new File("names-michelle-f-actual.png")).toPath());
        Files.deleteIfExists((new File("names-stuart-m-actual.png")).toPath());
        Files.deleteIfExists((new File("names2-abbie-f-actual.png")).toPath());
        Files.deleteIfExists((new File("names2-don-m-actual.png")).toPath());
        Files.deleteIfExists((new File("names2-ethel-f-actual.png")).toPath());
        Files.deleteIfExists((new File("names2-michelle-f-actual.png")).toPath());
        Files.deleteIfExists((new File("names2-stuart-m-actual.png")).toPath());

        System.out.println("Exiting TestUtils.cleanAllPngFiles");
    }

    public static void setStaticField(Class<?> clazz, String fieldName, Object value) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            fail(String.format("There is not a field named: %s", fieldName));
        }
        field.setAccessible(true);

        try {
            field.set(null, value);
        } catch (IllegalAccessException e) {
            fail(String.format("Could not set value of field: %s %s Make sure the field does not have " +
                    "the FINAL modifier", fieldName, System.lineSeparator()));
        }
    }

    public static void setNames1Variables(){
        setStaticField(BabyNames.class,"nameFilename", NAME_FILENAME_1);
        setStaticField(BabyNames.class,"startingYear", STARTING_YEAR_1);
        setStaticField(BabyNames.class,"decadeWidth", DECADE_WIDTH_1);
        setStaticField(BabyNames.class,"legendHeight", LEGEND_HEIGHT_1);

        NameFileContext = NAME_FILENAME_1;
        NameFile = NameFileContext.split("\\.")[0];
    }

    public static void setNames2Variables(){
        setStaticField(BabyNames.class,"nameFilename", NAME_FILENAME_2);
        setStaticField(BabyNames.class,"startingYear", STARTING_YEAR_2);
        setStaticField(BabyNames.class,"decadeWidth", DECADE_WIDTH_2);
        setStaticField(BabyNames.class,"legendHeight", LEGEND_HEIGHT_2);

        NameFileContext = NAME_FILENAME_2;
        NameFile = NameFileContext.split("\\.")[0];
    }

    public static void forbidSystemExitCall() {
        System.out.println("Entering TestUtils.forbidSystemExitCall");
        final SecurityManager securityManager = new SecurityManager() {
            public void checkPermission(Permission permission) {
                if (permission.getName().contains("exitVM")) {
                    throw new ExitTrappedException();
                }
            }
        };
        System.setSecurityManager(securityManager);
    }

    public static class ExitTrappedException extends SecurityException {
    }

    public static void enableSystemExit() {
        System.out.println("Entering TestUtils.enableSystemExit");
        System.setSecurityManager(null);
    }
}