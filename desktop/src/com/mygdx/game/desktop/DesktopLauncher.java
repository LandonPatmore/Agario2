package com.mygdx.game.desktop;

import Game.Agario;
import Utils.Config;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    private static LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

    public static void main(String[] arg) {
        grabProperties();
        new LwjglApplication(new Agario(), config);
    }

    private static void grabProperties(){
        Config.readFile();

        config.title = Config.title;
        config.height = Config.getHeight();
        config.width = Config.getWidth();
    }
}
