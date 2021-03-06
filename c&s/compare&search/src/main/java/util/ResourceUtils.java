package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author bibek on 12/2/17
 * @project compare&search
 */
public class ResourceUtils {
    private ResourceUtils() {

    }

    private Properties getProperties() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = Thread.currentThread()
                    .getContextClassLoader().getResourceAsStream("resource.properties");
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    public static Properties getProp() {
        return new ResourceUtils().getProperties();
    }
}
