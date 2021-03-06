package de.nosswald.chess.game;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import de.nosswald.chess.Chess;
import de.nosswald.chess.game.ai.OpponentPlayer;
import de.nosswald.chess.game.piece.Piece;
import de.nosswald.chess.game.piece.impl.*;
import de.nosswald.chess.gui.screen.impl.GameResultScreen;
import de.nosswald.chess.logger.LoggerLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public final class Board
{
    public static long getBitboard(Stream<Piece> pieceStream)
    {
        return pieceStream.map(p -> (long)(7 - p.getPosition().getRow()) * 8L + (long)p.getPosition().getCol())
                .map(i -> 1L << i)
                .collect(() -> new Long[]{0L}, (a, b) -> a[0] = a[0] | b, (a, b) -> a[0] = a[0] | b[0])[0];
    }

    /**
     * Contains every {@link Piece} on the {@link Board}
     */
    private final ArrayList<Piece> pieces = new ArrayList<>();

    /**
     * Contains every {@link Move} that has been done on the yet
     */
    private final ArrayList<Move> history = new ArrayList<>();

    /**
     * Stores the {@link Side} which moves next
     */
    private Side nextSide = Side.WHITE;

    /**
     * Stores the {@link Side} which is being controlled by the player
     */
    private final Side playerSide = //Math.random() > .5 ? Side.WHITE : Side.BLACK;
Side.WHITE;
    /**
     * Stores the {@link OpponentPlayer}
     */
    private final OpponentPlayer opponentPlayer = new OpponentPlayer(playerSide.flip());

    /**
     * Stores the currently selected {@link Piece} (<code>null</code> if nothing is selected)
     */
    @Nullable
    private Piece selected;

    private boolean gameOver;
    private boolean legitimacyChecking = true;

    /**
     * Initializes every {@link Piece} on the {@link Board}
     */
    public void initialize()
    {
        IntStream.range(0, 16).forEach(i -> {
            boolean b = i % 2 == 0;

            pieces.add(new Pawn(b ? Side.BLACK : Side.WHITE, new Position(i / 2, b ? 1 : 6)));

            if (i < 4)
            {
                pieces.add(new Rook(b ? Side.BLACK : Side.WHITE, new Position(i / 2 == 0 ? 0 : 7, b ? 0 : 7)));
                pieces.add(new Knight(b ? Side.BLACK : Side.WHITE, new Position(i / 2 == 0 ? 1 : 6, b ? 0 : 7)));
                pieces.add(new Bishop(b ? Side.BLACK : Side.WHITE, new Position(i / 2 == 0 ? 2 : 5, b ? 0 : 7)));
            }
            if (i < 2)
            {
                pieces.add(new Queen(b ? Side.BLACK : Side.WHITE, new Position(3, b ? 0 : 7)));
                pieces.add(new King(b ? Side.BLACK : Side.WHITE, new Position(4, b ? 0 : 7)));
            }
        });

        Chess.getLogger().print(LoggerLevel.INFO, "Added all pieces to the board");

        if (nextSide != playerSide)
            opponentPlayer.awaitResponse();
    }

    /**
     * Handles the {@link java.awt.event.MouseEvent} based on the given {@link Position}
     *
     * @param position the clicked {@link Position}
     */
    public void onClick(Position position)
    {
        if (nextSide != playerSide || gameOver)
            return;

        if (selected == null)
        {
            // select piece
            Piece clicked = getPiece(position);

            if (clicked != null && clicked.getSide() == nextSide)
            {
                selected = clicked;
                Chess.getLogger().printFormat(LoggerLevel.DEBUG,
                        "Selected %s on %s [%d possible moves]", clicked.getClass().getSimpleName(),
                        position.toString(), clicked.getPossibleMoves().size()
                );
            }
        }
        else
        {
            // move selected piece to the clicked column and row
            selected.getPossibleMoves().stream()
                    .filter(m -> m.getTo().equals(position))
                    .findFirst().ifPresent(m ->
                    {
                        Chess.getLogger().printFormat(LoggerLevel.DEBUG,
                                "Player moved %s from %s to %s", selected.getClass().getSimpleName(),
                                selected.getPosition().toString(), m.getTo().toString());
                        makeMove(m, false);

                        // opponents move
                        opponentPlayer.awaitResponse();
                    }
            );

            // unselect current piece
            selected = null;
        }
    }

    /**
     * Performs the given {@link Move} on the {@link Board}
     *
     * @param move      the {@link Move}
     * @param inSearch  whether this {@link Move} should be recorded in the history
     */
    public void makeMove(@NotNull Move move, boolean inSearch)
    {
        final Move.Flag flag = move.getFlag();
        final Piece movingPiece = getPiece(move.getFrom());

        pieces.remove(move.getCapturedPiece());

        // move piece
        movingPiece.setPosition(move.getTo());

        // handle special moves
        switch (flag)
        {
            case PROMOTION:
                pieces.remove(movingPiece); // remove pawn
                pieces.add(new Queen(movingPiece.getSide(), movingPiece.getPosition())); // TODO make piece selectable
                break;

            case EN_PASSANT:
                pieces.remove(getPiece(new Position(move.getTo().getCol(), move.getFrom().getRow())));
                break;

            case CASTLING:
                final int startRow = movingPiece.getSide() == Side.WHITE ? 7 : 0;

                // short castle
                if (move.getTo().getCol() == 6)
                    getPiece(new Position(7, startRow)).setPosition(new Position(5, startRow));
                // long castle
                else if (move.getTo().getCol() == 2)
                    getPiece(new Position(0, startRow)).setPosition(new Position(3, startRow));
                break;
        }

        // flip sides
        nextSide = nextSide.flip();
        Chess.getLogger().printFormat(LoggerLevel.DEBUG, "%s's turn", nextSide.toString());

        history.add(move);
        // add to history
        if (!inSearch) {
            // TODO fifty move counter / repetition position
            /*
             * !!! THIS SHOULD NOT BE HERE !!!
             * (used to check if the game is over)
             */
            if (isStalemate() || isCheckMate(nextSide)) {
                gameOver = true;
                selected = null;

                if (isStalemate()) {
                    nextSide = null; // to identify that the match has ended in a draw
                    Chess.getLogger().print(LoggerLevel.INFO, "The match has ended in a draw");
                } else
                    Chess.getLogger().printFormat(LoggerLevel.INFO,
                            "%s has won the match", nextSide.flip().toString());

                Chess.getInstance().getFrame().setScreen(new GameResultScreen(this));
            }
        }
    }

    /**
     * Undos the given {@link Move} on the {@link Board}
     *
     * @param move      the {@link Move}
     * @param inSearch  whether this {@link Move} should be recorded in the history
     */
    public void unmakeMove(Move move, boolean inSearch)
    {
        final Piece oldPiece = getPiece(move.getTo());
        final Piece capturedPiece = move.getCapturedPiece();

        // reset position
        oldPiece.setPosition(move.getFrom());

        // recover captured piece
        if (capturedPiece != null)
        {
            capturedPiece.setPosition(move.getTo());
            pieces.add(capturedPiece);
        }

        // handle special moves
        switch (move.getFlag())
        {
            case PROMOTION:
                final Piece originalPawn = move.getMovingPiece();

                originalPawn.setPosition(move.getFrom());
                pieces.remove(oldPiece);
                pieces.add(originalPawn);
                break;
            case EN_PASSANT:
                capturedPiece.setPosition(new Position(
                        capturedPiece.getPosition().getCol(),
                        capturedPiece.getPosition().getRow() + (capturedPiece.getSide() == Side.WHITE ? 1 : -1))
                );
                break;
            case CASTLING:
                final Piece rightRook = getPiece(new Position(move.getTo().getCol() + 1, move.getTo().getRow()));
                final Rook rook = (Rook) (rightRook instanceof Rook ?
                        getPiece(new Position(move.getTo().getCol() - 1, move.getTo().getRow())) : rightRook);
                rook.setPosition(new Position(rook.getPosition().getCol() == 3 ? 0 : 7, move.getTo().getRow()));
                break;
            default:
                break;
        }

        // set side
        nextSide = nextSide.flip();
        Chess.getLogger().printFormat(LoggerLevel.DEBUG, "%s's turn", nextSide.toString());

        history.remove(move);
        if (!inSearch)
        {
            // TODO fifty move counter / repetition position
        }
    }

    /**
     * @param side the {@link Side} to be checked
     * @return Whether the {@link Side}'s {@link King} is in check or not
     */
    public boolean isInCheck(Side side)
    {
        return isAttacked(pieces.stream().filter(p -> p instanceof King && p.getSide() == side).findFirst().get());
    }

    public boolean isAttacked(Piece piece) {return isAttacked(piece.getSide().flip(), piece.getPosition());}
    public boolean isAttacked(Side attacking, Position position)
    {
        return new ArrayList<>(pieces).stream()
                .filter(p -> p.getSide() == attacking)
                .anyMatch(p -> p.getPossibleMoves().stream()
                        .anyMatch(m -> m.getTo().equals(position)));
    }

    /**
     * @param side the {@link Side} to be checked
     * @return Whether it's checkmate for the given {@link Side} or not
     */
    public boolean isCheckMate(Side side)
    {
        List<Piece> piecesClone = new ArrayList<>(pieces);

        return piecesClone.stream()
                .filter(p -> p.getSide() == side)
                .allMatch(p -> p.getPossibleMoves().isEmpty()) && isInCheck(side);
    }

    /**
     * @return If both sides are unable to move or only both {@link King}'s are alive
     */
    public boolean isStalemate()
    {
        List<Piece> piecesClone = new ArrayList<>(pieces);

        return pieces.size() == 2 || piecesClone.stream()
                .filter(p -> p.getSide() == nextSide)
                .allMatch(p -> p.getPossibleMoves().isEmpty()) && !isInCheck(nextSide);
    }

    /**
     * @param position the {@link Position}
     * @return The {@link Piece} on the given {@link Position} (<code>null</code> if the field is empty)
     */
    @Nullable
    public Piece getPiece(Position position)
    {
        return pieces.stream().filter(p -> p.getPosition().equals(position)).findFirst().orElse(null);
    }

    /**
     * @param position the {@link Position}
     * @return Whether the given {@link Position} has a {@link Piece} or not
     */
    public boolean hasPiece(Position position)
    {
        return pieces.stream().anyMatch(p -> p.getPosition().equals(position));
    }

    /**
     * @return A {@link List} of all {@link Piece}'s on the {@link Board}
     */
    public ArrayList<Piece> getPieces()
    {
        return pieces;
    }

    /**
     * @return Every {@link Move} that has been done yet
     */
    public List<Move> getHistory()
    {
        return history;
    }

    /**
     * @return The {@link Side} which moves next
     */
    public Side getNextSide()
    {
        return nextSide;
    }

    /**
     * @return The currently selected {@link Piece} (<code>null</code> if nothing is selected)
     */
    public Piece getSelected()
    {
        return selected;
    }

    public void setLegitimacyChecking(boolean legitimacyChecking)
    {
        this.legitimacyChecking = legitimacyChecking;
    }

    public boolean isLegitimacyChecking()
    {
        return legitimacyChecking;
    }

    public boolean isBoardValid(){
        boolean hasOverlappingPieces = new ArrayList(pieces).stream()
                .anyMatch(pieceA -> pieces.stream()
                        .anyMatch(pieceB -> pieceA != pieceB && ((Piece)pieceA).getPosition().equals(pieceB.getPosition())));

        if (hasOverlappingPieces) return false;

        long blackPieces = pieces.stream().filter(p -> p.getSide() == Side.BLACK).count();
        long whitePieces = pieces.stream().filter(p -> p.getSide() == Side.WHITE).count();

        if (blackPieces > 16) return false;
        if (whitePieces > 16) return false;

        long blackKings = pieces.stream().filter(p -> p.getSide() == Side.BLACK && p instanceof King).count();
        long whiteKings = pieces.stream().filter(p -> p.getSide() == Side.WHITE && p instanceof King).count();

        if (blackKings != 1) return false;
        if (whiteKings != 1) return false;

        long blackPawns = pieces.stream().filter(p -> p.getSide() == Side.BLACK && p instanceof Pawn).count();
        long whitePawns = pieces.stream().filter(p -> p.getSide() == Side.WHITE && p instanceof Pawn).count();

        if (blackPawns > 8) return false;
        if (whitePawns > 8) return false;

        return true;
    }
}
