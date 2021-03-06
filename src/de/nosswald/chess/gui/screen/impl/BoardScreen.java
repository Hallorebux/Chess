package de.nosswald.chess.gui.screen.impl;

import de.nosswald.chess.Chess;
import de.nosswald.chess.gui.RelativeSize;
import de.nosswald.chess.gui.element.impl.BoardElement;
import de.nosswald.chess.gui.screen.Screen;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public final class BoardScreen extends Screen
{
    public BoardScreen()
    {
        elements.add(
                new BoardElement(Chess.getInstance().getBoard(), new RelativeSize(.05F), new RelativeSize(.05F), new RelativeSize(.9F))
        );
    }
}
