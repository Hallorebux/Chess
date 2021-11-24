package de.nosswald.chess.game.piece.impl;

import de.nosswald.chess.game.Move;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public final class King extends Piece
{
    private final int startRow;

    /**
     * @param side      the {@link Side}
     * @param position  the {@link Position}
     */
    public King(Side side, Position position)
    {
        super("king_" + side.name().toLowerCase(Locale.ROOT) + ".png", side, position);

        startRow = side == Side.WHITE ? 7 : 0;
    }

    /**
     * <code>|_|_|_|_|_|_|_|_|<br>
     * |_|_|_|_|_|_|_|_|<br>
     * |_|_|*|*|*|_|_|_|<br>
     * |_|_|*|K|*|_|_|_|<br>
     * |_|_|_|_|_|_|_|_|<br>
     * |_|_|_|k|_|_|_|_|<br>
     * |_|_|_|_|_|_|_|_|<br>
     * |_|_|_|_|_|_|_|_|</code>
     *
     * @return An unfiltered list of all possible {@link Move}'s
     */
    @Override
    public List<Move> getPossibleMoves()
    {
        // TODO fix castling bug
        final List<Move> moves = new ArrayList<>();

        // basic movement
        for (int colOffset = -1; colOffset <= 1; colOffset++)
        {
            for (int rowOffset = -1; rowOffset <= 1; rowOffset++)
            {
                if (colOffset != 0 || rowOffset != 0)
                {
                    Position to = new Position(this.position.getCol() + colOffset, this.position.getRow() + rowOffset);

                    if (this.onBoard(to))
                    {
                        if (this.board.hasPiece(to) && this.board.getPiece(to).getSide() == this.side)
                            continue;

                        moves.add(new Move(this.position, to, this.board.getPiece(to)));
                    }
                }
            }
        }

        // castling
        if (this.position.getCol() == 4 && this.position.getRow() == startRow && this.board.getHistory().stream().noneMatch(m -> m.getFrom().equals(new Position(4, startRow))))
        {
            // short castle
            if (this.board.getHistory().stream()
                    .noneMatch(m -> m.getFrom().getCol() == 7 && m.getFrom().getRow() == startRow)
                        && this.board.getPiece(new Position(7, startRow)) instanceof Rook
                        && this.board.getPiece(new Position(7, startRow)).getSide() == this.side
                        && IntStream.range(5, 7)
                            .noneMatch(col -> this.board.hasPiece(new Position(col, startRow)))
            )
                moves.add(new Move(this.position, new Position(6, startRow), Move.Flag.CASTLING, null));

            // long castle
            if (this.board.getHistory().stream()
                    .noneMatch(m -> m.getFrom().getCol() == 0 && m.getFrom().getRow() == startRow)
                        && this.board.getPiece(new Position(0, startRow)) instanceof Rook
                        && this.board.getPiece(new Position(0, startRow)).getSide() == this.side
                        && IntStream.range(1, 3)
                            .noneMatch(col -> this.board.hasPiece(new Position(col, startRow)))
            )
                moves.add(new Move(this.position, new Position(2, startRow), Move.Flag.CASTLING, null));
        }

        return filterLegalMoves(moves);
    }
}
