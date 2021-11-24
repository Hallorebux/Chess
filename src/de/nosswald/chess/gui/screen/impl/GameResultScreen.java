package de.nosswald.chess.gui.screen.impl;

import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.gui.AbsoluteSize;
import de.nosswald.chess.gui.Anchor;
import de.nosswald.chess.gui.RelativeSize;
import de.nosswald.chess.gui.element.impl.LabelElement;
import de.nosswald.chess.gui.element.impl.SimpleButtonElement;
import de.nosswald.chess.gui.element.impl.BoardElement;
import de.nosswald.chess.gui.screen.Screen;

import java.awt.*;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public final class GameResultScreen extends Screen
{
    public GameResultScreen(Board board)
    {
        final String message = board.getNextSide() != null ? board.getNextSide().flip() + " has won the match" : "Stalemate";

        this.elements.add(
                new LabelElement(message, new RelativeSize(0), new AbsoluteSize(30), new RelativeSize(1), new RelativeSize(.15F), Anchor.CENTER, Anchor.CENTER)
                        .setColor(Color.BLACK)
                        .setFont(new Font("Verdana", Font.PLAIN, new AbsoluteSize(24).get(0)))
        );

        this.elements.add(
                new BoardElement(board, new RelativeSize(.25F), new RelativeSize(.25F), new RelativeSize(.5F))
        );

        this.elements.add(
                new SimpleButtonElement("Main Menu", new RelativeSize(.25F), new RelativeSize(.8F), new RelativeSize(.5F), new RelativeSize(.1F))
                        .setAction(() -> Chess.getInstance().getFrame().setScreen(new MainMenuScreen()))
        );
    }
}
