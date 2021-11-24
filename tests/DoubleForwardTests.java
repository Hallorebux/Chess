import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Move;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.impl.King;
import de.nosswald.chess.game.piece.impl.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class DoubleForwardTests
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

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    public void whiteDoubleMove(int col)
    {
        board.getPieces().add(new King(Side.BLACK, new Position(0, 0)));
        board.getPieces().add(new King(Side.WHITE, new Position(0, 7)));
        Pawn p = new Pawn(Side.WHITE, new Position(col, 6));
        board.getPieces().add(p);
        long count = p.getPossibleMoves().stream().filter(x -> x.getFlag() == Move.Flag.DOUBLE_FORWARD).count();
        Assertions.assertEquals(1, count);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    public void blackDoubleMove(int col)
    {
        board.getPieces().add(new King(Side.BLACK, new Position(0, 0)));
        board.getPieces().add(new King(Side.WHITE, new Position(0, 7)));
        Pawn p = new Pawn(Side.BLACK, new Position(col, 1));
        board.getPieces().add(p);
        long count = p.getPossibleMoves().stream().filter(x -> x.getFlag() == Move.Flag.DOUBLE_FORWARD).count();
        Assertions.assertEquals(1, count);
    }
}
