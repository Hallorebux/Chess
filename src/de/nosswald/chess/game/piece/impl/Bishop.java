package de.nosswald.chess.game.piece.impl;

import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.Piece;

import java.awt.*;

/**
 * @author Noah Gerber
 */
public class Bishop extends Piece
{
    public Bishop(Side side, int col, int row)
    {
        super(side, col, row);
    }

    @Override
    public void paint(Graphics2D graphics, int size)
    {
        // TODO draw image
    }
}
