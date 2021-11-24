import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Move;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.impl.King;
import de.nosswald.chess.game.piece.impl.Pawn;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class EnPassantTests {
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

    @Test
    public void basicEnPassantRight()
    {
        Pawn a = new Pawn(Side.WHITE, new Position(0, 3));
        Pawn b = new Pawn(Side.BLACK, new Position(1, 1));

        board.getPieces().add(new King(Side.BLACK, new Position(0, 0)));
        board.getPieces().add(new King(Side.WHITE, new Position(0, 7)));
        board.getPieces().add(a);
        board.getPieces().add(b);

        Move doubleMove = b.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.DOUBLE_FORWARD).findFirst().get();
        board.makeMove(doubleMove, false);
        long enPassantCount = a.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.EN_PASSANT).count();
        Assertions.assertEquals(1, enPassantCount);
    }

    @Test
    public void basicEnPassantLeft()
    {
        Pawn a = new Pawn(Side.WHITE, new Position(7, 3));
        Pawn b = new Pawn(Side.BLACK, new Position(6, 1));

        board.getPieces().add(new King(Side.BLACK, new Position(0, 0)));
        board.getPieces().add(new King(Side.WHITE, new Position(0, 7)));
        board.getPieces().add(a);
        board.getPieces().add(b);

        Move doubleMove = b.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.DOUBLE_FORWARD).findFirst().get();
        board.makeMove(doubleMove, false);
        long enPassantCount = a.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.EN_PASSANT).count();
        Assertions.assertEquals(1, enPassantCount);
    }
}
