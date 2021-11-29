import com.winkelhagen.chess.syzygy.SyzygyBridge;
import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.Piece;
import de.nosswald.chess.game.piece.impl.*;
import de.nosswald.chess.logger.LoggerLevel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TablebaseTests
{
    private Board board;

    @BeforeAll
    public static void setupBoard()
    {
        Chess.DEBUG_MODE = true;
        new Chess(false);
        SyzygyBridge.load("tablebase/");
        Chess.getLogger().printFormat(LoggerLevel.INFO, "Loaded Syzygy %s", SyzygyBridge.getSupportedSize());
    }

    @BeforeEach
    public void before()
    {
        board = new Board();
        Chess.getInstance().setBoard(board);
    }

    @ParameterizedTest
    @MethodSource
    public void tablebaseRandomBoard(int blackPieces, int whitePieces)
    {
        board.getPieces().add(new King(Side.WHITE, getRandomPosition(board)));
        board.getPieces().add(new King(Side.BLACK, getRandomPosition(board)));

        for (int i = 0; i < blackPieces; i++)
            board.getPieces().add(getRandomPiece(Side.BLACK, getRandomPosition(board)));
        for (int i = 0; i < whitePieces; i++)
            board.getPieces().add(getRandomPiece(Side.WHITE, getRandomPosition(board)));

        SyzygyBridge.probeSyzygyDTZ(Board.getBitboard(board.getPieces().stream().filter(p -> p.getSide() == Side.WHITE)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p.getSide() == Side.BLACK)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p instanceof King)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p instanceof Queen)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p instanceof Rook)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p instanceof Bishop)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p instanceof Knight)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p instanceof Pawn)),
                0,
                0, // TODO en passant square
                board.getNextSide() == Side.WHITE
        );
    }

    private static Random rnd = new Random();
    private static Position getRandomPosition(Board b)
    {
        Position p;
        do {
            p = new Position(rnd.nextInt(8), rnd.nextInt(8));
        } while(b.getPiece(p) == null);
        return p;
    }

    private static Piece getRandomPiece(Side side, Position position)
    {
        switch (rnd.nextInt(5))
        {
            case 0: return new Pawn(side, position);
            case 1: return new Rook(side, position);
            case 2: return new Knight(side, position);
            case 3: return new Bishop(side, position);
            case 4: return new Queen(side, position);
            default: return null;
        }
    }

    private static Stream<Arguments> tablebaseRandomBoard()
    {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(0, 1),
                Arguments.of(0, 2),
                Arguments.of(1, 0),
                Arguments.of(1, 1)
        );
    }
}
