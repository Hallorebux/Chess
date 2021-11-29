package de.nosswald.chess;

import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.Piece;
import de.nosswald.chess.game.piece.impl.*;
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

    public static void printBoard(Board board)
    {
        for (int r = 0; r < 8; r++)
        {
            System.out.println("----------------");
            for (int c = 0; c < 8; c++)
            {
                System.out.print('|');
                Piece p = board.getPiece(new Position(c, r));
                char pc = p instanceof King ? 'K'
                        : p instanceof Rook ? 'R'
                        : p instanceof Knight ? 'N'
                        : p instanceof Bishop ? 'B'
                        : p instanceof Queen ? 'Q'
                        : p instanceof Pawn ? 'P'
                        : ' ';
                if (p != null && p.getSide() == Side.BLACK)
                    pc = Character.toLowerCase(pc);
                System.out.print(pc);
            }
            System.out.println('|');
        }
        System.out.println("----------------");
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
