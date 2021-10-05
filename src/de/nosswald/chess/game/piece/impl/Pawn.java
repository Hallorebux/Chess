package de.nosswald.chess.game.piece.impl;

import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.Piece;

import java.util.*;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public class Pawn extends Piece
{
    public Pawn(Side side, int col, int row)
    {
        super("pawn_" + side.name().toLowerCase(Locale.ROOT) + ".png", side, col, row);
    }

    @Override
    public void setPosition(int col, int row)
    {
        super.setPosition(col, row);

        this.board.getPieces().removeIf(piece -> piece.getSide() != this.side
                && piece.getLastRow() == (piece.getSide() == Side.WHITE ? 6 : 1)
                && piece.getRow() == (piece.getSide() == Side.WHITE ? 4 : 3)
                && this.col == piece.getCol() && this.row == piece.getRow() + (this.side == Side.WHITE ? -1 : 1)
        );

        if (row == (this.side == Side.WHITE ? 0 : 7))
        {
            // TODO make piece selectable
            this.board.getPieces().remove(this);
            this.board.getPieces().add(new Queen(this.side, col, row));
        }
    }

    @Override
    public List<int[]> getPossibleMoves()
    {
        final List<int[]> moves = new ArrayList<>();
        final boolean isFirstMove = this.side == Side.WHITE ? this.row == 6 : this.row == 1;
        final int rowForward = this.side == Side.WHITE ? this.row - 1 : this.row + 1;
        final int rowFirst = this.side == Side.WHITE ? this.row - 2 : this.row + 2;

        // single step forward
        if (!this.board.hasPiece(this.col, rowForward) && this.onBoard(this.col, rowForward))
            moves.add(new int[]{ this.col, rowForward });

        // double step forward
        if (isFirstMove && !this.board.hasPiece(this.col, rowForward) && !this.board.hasPiece(this.col, rowFirst)
                && this.onBoard(this.col, rowFirst))
            moves.add(new int[]{ this.col, rowFirst });

        // opponent attack right
        if (this.onBoard(this.col + 1, rowForward) && this.board.hasPiece(this.col + 1, rowForward)
                && this.board.getPiece(this.col + 1, rowForward).getSide() != this.side)
            moves.add(new int[]{ this.col + 1, rowForward });

        // opponent attack left
        if (this.onBoard(this.col - 1, rowForward) && this.board.hasPiece(this.col - 1, rowForward)
                && this.board.getPiece(this.col - 1, rowForward).getSide() != this.side)
            moves.add(new int[]{ this.col - 1, rowForward });

        //en passant
        Piece targetPiece;
        int[] lastTurn;
        for (int i = -1; i < 3; i += 2)
        {
            targetPiece = this.board.getPiece(this.col + i, this.row);

            if(this.board.getHistory().isEmpty())
                break;
            lastTurn = this.board.getHistory().get(this.board.getHistory().size() - 1);

            if (targetPiece instanceof Pawn && targetPiece.getSide() != this.side
                    && targetPiece.getLastRow() == (targetPiece.getSide() == Side.WHITE ? 6 : 1)
                    && targetPiece.getRow() == (targetPiece.getSide() == Side.WHITE ? 4 : 3)
                    && lastTurn[2] == targetPiece.getCol() && lastTurn[3] == targetPiece.getRow())
                moves.add(new int[]{ this.col + i, rowForward});
        }

        return filterLegalMoves(moves);
    }
}
