import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Move;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.impl.King;
import de.nosswald.chess.game.piece.impl.Pawn;
import de.nosswald.chess.game.piece.impl.Queen;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class PromotionTests
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
    public void promotionBasicKPvK()
    {
        board.getPieces().add(new King(Side.BLACK, new Position(0, 0)));
        board.getPieces().add(new King(Side.WHITE, new Position(1, 2)));
        Pawn p = new Pawn(Side.WHITE, new Position(7, 1));
        board.getPieces().add(p);

        Assertions.assertEquals(1, p.getPossibleMoves().stream().count());
        Move m = p.getPossibleMoves().get(0);
        Assertions.assertEquals(Move.Flag.PROMOTION, m.getFlag());
        board.makeMove(m, false);

        Assertions.assertTrue(board.getPieces().stream().noneMatch(x -> x instanceof Pawn));

        Assertions.assertEquals(1, board.getPieces().stream().filter(x -> x instanceof Queen).count());

        Assertions.assertTrue(board.isCheckMate(Side.BLACK));
    }
}
