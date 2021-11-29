package de.nosswald.chess.game.ai;

import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Move;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.Piece;
import de.nosswald.chess.logger.LoggerLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public final class OpponentPlayer
{
    private final Side side;

    /**
     * @param side the {@link Side} to control
     */
    public OpponentPlayer(Side side)
    {
        this.side = side;
    }

    /**
     * Responds with the best possible {@link Move}
     */
    public void awaitResponse()
    {
        // perform first possible move
        final Board board = Chess.getInstance().getBoard();
        final List<Piece> piecesClone = new ArrayList<>(board.getPieces());
        final Piece piece = piecesClone
                .stream().filter(p -> p.getSide() == side)
                .filter(p -> !p.getPossibleMoves().isEmpty())
                .findFirst().orElse(null);
        final Move move = piece.getPossibleMoves().stream()
                .findFirst().orElse(null);

        // send the move to the board
        Chess.getLogger().printFormat(LoggerLevel.DEBUG,
                "AI moved %s from %s to %s", piece.getClass().getSimpleName(),
                piece.getPosition(), move.getTo().toString()
        );
        board.makeMove(move, false);
    }
}
