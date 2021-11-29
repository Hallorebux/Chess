package de.nosswald.chess;

import de.nosswald.chess.game.Board;
import de.nosswald.chess.gui.Frame;
import de.nosswald.chess.gui.screen.impl.BoardScreen;
import de.nosswald.chess.gui.screen.impl.MainMenuScreen;
import de.nosswald.chess.logger.Logger;
import de.nosswald.chess.logger.LoggerLevel;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public final class Chess
{
    public static final String APP_NAME = "Chess";
    public static final String APP_VERSION = "0.1";
    public static final String[] APP_AUTHORS = { "Nils Osswald", "Noah Gerber" };

    public static boolean DEBUG_MODE;

    private static final Logger logger = new Logger();
    private static Chess instance;

    private Frame frame;
    private Board board;

    public Chess(boolean createFrame)
    {
        instance = this;

        logger.print(LoggerLevel.DEBUG, "Using debug mode");

        if (createFrame) {
            // create user interface
            logger.print(LoggerLevel.INFO, "Creating user interface..");
            frame = new Frame();

            // open main menu
            frame.setScreen(new MainMenuScreen());
        }

        // add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.print(LoggerLevel.INFO, "Shutting down..");
        }));
    }

    public static Chess getInstance()
    {
        return instance;
    }

    public void showBoard()
    {
        if (getFrame() != null) throw new RuntimeException();

        frame = new Frame();
        frame.setScreen(new BoardScreen());
    }

    public static Logger getLogger()
    {
        return logger;
    }

    public Frame getFrame()
    {
        return frame;
    }

    public Board getBoard()
    {
        return board;
    }

    public void setBoard(Board board)
    {
        logger.print(LoggerLevel.INFO, "Creating new chess board..");
        this.board = board;
    }
}
