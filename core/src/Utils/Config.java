package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties propFile = new Properties();
    private static InputStream input;

    public static final String title = "Agar.io 2";

    public static void readFile(){
        try {
            input = new FileInputStream("core/config/config.properties");
            propFile.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getProperty(String p){
        return propFile.getProperty(p);
    }

    public static int getNumberProperty(String p){
        return Integer.parseInt(propFile.getProperty(p));
    }

    public static float getFloatProperty(String p){
        return Float.parseFloat(propFile.getProperty(p));
    }

    public static int getHeight(){
        String p = getProperty("height");
        if(p == null){
            return 800;
        } else {
            return Integer.parseInt(p);
        }
    }

    public static int getWidth(){
        String p = getProperty("width");
        if(p == null){
            return 1280;
        } else {
            return Integer.parseInt(p);
        }
    }


    public static boolean getDebug() {
        String p = getProperty("debug");
        return p != null && Boolean.parseBoolean(p);
    }
}
