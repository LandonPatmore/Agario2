package Utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class Globals {

    public static void setDebug(boolean bool) {
        if (bool) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        } else {
            Gdx.app.setLogLevel(Application.LOG_NONE);
        }
    }

    public static String title = "";
    public static int height = 0;
    public static int width = 0;

    public static final int SIZE = 5;
    public static final int C_SIZE = 2;
}
