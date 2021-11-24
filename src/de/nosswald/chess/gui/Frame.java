package de.nosswald.chess.gui;

import com.sun.istack.internal.NotNull;
import de.nosswald.chess.Chess;
import de.nosswald.chess.gui.screen.Screen;
import de.nosswald.chess.logger.LoggerLevel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public final class Frame extends JFrame
{
    private Screen currentScreen;

    public Frame()
    {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int screenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
        final int frameWidth = (int)(screenSize.width * .5F);
        final int frameHeight = (int)(screenSize.height * .65F);

        Chess.getLogger().printFormat(LoggerLevel.DEBUG,
                "Determined screen size: %dx%d", (int)screenSize.getWidth(), (int)screenSize.getHeight());
        Chess.getLogger().printFormat(LoggerLevel.DEBUG,
                "Determined screen resolution: %d", screenResolution);
        Chess.getLogger().printFormat(LoggerLevel.DEBUG,
                "Using frame size of %dx%d", frameWidth, frameHeight);

        this.setTitle(Chess.APP_NAME);
        this.setSize(frameWidth, frameHeight);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // center frame on screen
        if (!Chess.DEBUG_MODE) this.setUndecorated(true);
        this.setLayout(null);

        Chess.getLogger().print(LoggerLevel.INFO, "Successfully created game frame");
        this.setVisible(true);
    }

    /**
     * Changes the current {@link Screen}
     *
     * @param screen the {@link Screen}
     */
    public void setScreen(@NotNull Screen screen)
    {
        Chess.getLogger().printFormat(LoggerLevel.DEBUG,
                "Switching screen to %s..", screen.getClass().getSimpleName());

        currentScreen = screen;
        this.setContentPane(screen);

        // paint and validate the new screen
        this.repaint();
        this.revalidate();
    }

    /**
     * @return The {@link Screen} that is currently being displayed on the {@link Frame}
     */
    public Screen getCurrentScreen()
    {
        return currentScreen;
    }
}
