import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.impl.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class StalemateTests {
    // test things like Two kings is stalemate
    // king + knight vs king is stalemate, but king + knight vs king + pawn is not stalemate
    // etc.

    // examples from https://en.wikipedia.org/wiki/Stalemate but colors are swapped

    private Board board;

    @BeforeAll
    public static void setupBoard()
    {
        Chess.DEBUG_MODE = true;
        new Chess(false);
    }

    @BeforeEach
    public void before()
    {
        board = new Board();
        Chess.getInstance().setBoard(board);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    public void stalemateKvKP(int col)
    {
        board.getPieces().add(new King(Side.BLACK, new Position(col, 5)));
        board.getPieces().add(new Pawn(Side.BLACK, new Position(col, 6)));
        board.getPieces().add(new King(Side.WHITE, new Position(col, 7)));

        Assertions.assertTrue(board.isStalemate());
    }

    @Test
    public void stalemateKBvKR()
    {
        board.getPieces().add(new King(Side.WHITE, new Position(0, 0)));
        board.getPieces().add(new Bishop(Side.WHITE, new Position(1, 0)));
        board.getPieces().add(new Rook(Side.BLACK, new Position(7, 0)));
        board.getPieces().add(new King(Side.BLACK, new Position(1, 2)));

        Assertions.assertTrue(board.isStalemate());
    }

    @Test
    public void stalemateKvKR()
    {
        board.getPieces().add(new King(Side.WHITE, new Position(0, 7)));
        board.getPieces().add(new Rook(Side.BLACK, new Position(1, 6)));
        board.getPieces().add(new King(Side.BLACK, new Position(2, 5)));

        Assertions.assertTrue(board.isStalemate());
    }

    @Test
    public void stalemateKvKPB()
    {
        board.getPieces().add(new King(Side.WHITE, new Position(0, 0)));
        board.getPieces().add(new Pawn(Side.BLACK, new Position(0, 1)));
        board.getPieces().add(new King(Side.BLACK, new Position(0, 2)));
        board.getPieces().add(new Bishop(Side.BLACK, new Position(5, 4)));

        Assertions.assertTrue(board.isStalemate());
    }

    @Test
    public void stalemateKPvKQ()
    {
        board.getPieces().add(new King(Side.WHITE, new Position(7, 0)));
        board.getPieces().add(new Pawn(Side.WHITE, new Position(7, 1)));
        board.getPieces().add(new King(Side.BLACK, new Position(0, 0)));
        board.getPieces().add(new Queen(Side.BLACK, new Position(6, 2)));

        Assertions.assertTrue(board.isStalemate());
    }
}
