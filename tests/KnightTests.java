import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.impl.Bishop;
import de.nosswald.chess.game.piece.impl.King;
import de.nosswald.chess.game.piece.impl.Knight;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class KnightTests {
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
    public void knightInvalidJump()
    {
        Knight n = new Knight(Side.WHITE, new Position(0, 0));
        board.getPieces().add(new King(Side.WHITE, new Position(1, 2)));
        board.getPieces().add(new King(Side.BLACK, new Position(7,7)));
        board.getPieces().add(n);

        Assertions.assertEquals(1, n.getPossibleMoves().size());
    }

    @Test
    public void knightMoveInCheck() {
        Knight n = new Knight(Side.BLACK, new Position(0, 0));
        board.getPieces().add(n);
        board.getPieces().add(new King(Side.BLACK, new Position(5, 0)));
        board.getPieces().add(new Bishop(Side.WHITE, new Position(0, 5)));
        board.getPieces().add(new King(Side.WHITE, new Position(7, 7)));

        Assertions.assertEquals(0, n.getPossibleMoves().size());
    }
}
