package com.mygdx.game.desktop;

import Game.Agario;
import Utils.Globals;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        setConfig();
        config.title = Globals.title;
        config.height = Globals.height;
        config.width = Globals.width;
        new LwjglApplication(new Agario(), config);
    }

    private static void setConfig() {
        Globals.title = "Agario";
        Globals.height = 800;
        Globals.width = 1200;
    }
}
