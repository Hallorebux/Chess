package de.nosswald.chess.game;

import com.sun.istack.internal.Nullable;
import de.nosswald.chess.Chess;
import de.nosswald.chess.game.piece.Piece;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public final class Move
{
    private final Position from, to;
    private final Flag flag;
    private final Piece movingPiece;
    @Nullable private final Piece capturedPiece;

    /**
     * @param from  the {@link Position} to move from
     * @param to    the {@link Position} to move to
     * @param capturedPiece
     */
    public Move(Position from, Position to, Piece capturedPiece)
    {
        this.from = from;
        this.to = to;
        this.capturedPiece = capturedPiece;

        movingPiece = Chess.getInstance().getBoard().getPiece(from);
        flag = Flag.NONE;
    }

    /**
     * @param from  the {@link Position} to move from
     * @param to    the {@link Position} to move to
     * @param flag  the {@link Flag}
     * @param capturedPiece
     */
    public Move(Position from, Position to, Flag flag, Piece capturedPiece)
    {
        this.from = from;
        this.to = to;
        this.flag = flag;
        this.capturedPiece = capturedPiece;

        movingPiece = Chess.getInstance().getBoard().getPiece(from);
    }



    public enum Flag
    {
        NONE,
        DOUBLE_FORWARD,
        PROMOTION,
        EN_PASSANT,
        CASTLING;
    }

    /**
     * @return The starting {@link Position}
     */
    public Position getFrom()
    {
        return from;
    }

    /**
     * @return The ending {@link Position}
     */
    public Position getTo()
    {
        return to;
    }

    /**
     * @return The {@link Flag}
     */
    public Flag getFlag()
    {
        return flag;
    }

    /**
     * @return The captured {@link Piece} (<code>null<code/> if no piece was captured)
     */
    public Piece getCapturedPiece()
    {
        return capturedPiece;
    }

    public Piece getMovingPiece()
    {
        return movingPiece;
    }
}
