import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Move;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.impl.King;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class KingTests {

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
    public void kingFreeMovement() {
        board.getPieces().add(new King(Side.BLACK, new Position(0, 0)));
        King whiteKing = new King(Side.WHITE, new Position(3, 3));
        board.getPieces().add(whiteKing);

        List<Move> moves = whiteKing.getPossibleMoves();
        Assertions.assertEquals(8, moves.size());
    }

    @Test
    public void kingRestrictedMovement1() {
        board.getPieces().add(new King(Side.BLACK, new Position(0, 0)));
        King whiteKing = new King(Side.WHITE, new Position(0, 2));
        board.getPieces().add(whiteKing);

        List<Move> moves = whiteKing.getPossibleMoves();
        Assertions.assertEquals(3, moves.size());
    }

    @Test
    public void kingRestrictedMovement2() {
        board.getPieces().add(new King(Side.BLACK, new Position(3, 3)));
        King whiteKing = new King(Side.WHITE, new Position(0, 0));
        board.getPieces().add(whiteKing);

        List<Move> moves = whiteKing.getPossibleMoves();
        Assertions.assertEquals(3, moves.size());
    }
}
