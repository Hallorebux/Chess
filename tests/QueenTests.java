import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.impl.King;
import de.nosswald.chess.game.piece.impl.Queen;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class QueenTests
{
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
    public void queenMoveCountTests()
    {
        Queen q = new Queen(Side.WHITE, new Position(3, 4));
        board.getPieces().add(q);
        board.getPieces().add(new King(Side.WHITE, new Position(1, 7)));
        board.getPieces().add(new King(Side.BLACK, new Position(1, 0)));

        Assertions.assertEquals(27, q.getPossibleMoves().size());
    }
}
