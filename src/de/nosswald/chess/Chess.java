package de.nosswald.chess;

import de.nosswald.chess.game.Board;
import de.nosswald.chess.gui.Window;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public class Chess
{
    public static final String APP_NAME = "Chess";
    public static final String APP_VERSION = "0.1";
    public static final String[] APP_AUTHORS = { "Noah Gerber", "Nils Osswald" };

    public static final boolean DEBUG_MODE = true;

    private static Chess instance;

    private Board board;
    private Window window;

    public Chess()
    {
        instance = this;
        window = new Window();
        board = new Board();
    }

    public Board getBoard()
    {
        return board;
    }

    public static Chess getInstance()
    {
        return instance;
    }
}