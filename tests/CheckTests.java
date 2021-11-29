import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.impl.Bishop;
import de.nosswald.chess.game.piece.impl.King;
import de.nosswald.chess.game.piece.impl.Knight;
import de.nosswald.chess.game.piece.impl.Pawn;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CheckTests {
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
    public void simpleCheck()
    {
        board.getPieces().add(new King(Side.BLACK, new Position(7, 6)));
        board.getPieces().add(new Bishop(Side.BLACK, new Position(7, 7)));
        King k = new King(Side.WHITE, new Position(0, 0));
        Pawn p = new Pawn(Side.WHITE, new Position(0, 7));
        board.getPieces().add(k);
        board.getPieces().add(p);

        Assertions.assertEquals(0, p.getPossibleMoves().size());
        Assertions.assertEquals(2, k.getPossibleMoves().size());
    }

    @Test
    public void simpleCheckBlock()
    {
        board.getPieces().add(new King(Side.BLACK, new Position(7, 6)));
        board.getPieces().add(new Bishop(Side.BLACK, new Position(7, 7)));
        King k = new King(Side.WHITE, new Position(0, 0));
        Pawn p = new Pawn(Side.WHITE, new Position(1, 2));
        board.getPieces().add(k);
        board.getPieces().add(p);

        Assertions.assertEquals(1, p.getPossibleMoves().size());
        Assertions.assertEquals(2, k.getPossibleMoves().size());
    }

    @Test
    public void simpleCheckTakingKing()
    {
        board.getPieces().add(new King(Side.BLACK, new Position(7, 6)));
        board.getPieces().add(new Bishop(Side.BLACK, new Position(1, 1)));
        King k = new King(Side.WHITE, new Position(0, 0));
        Pawn p = new Pawn(Side.WHITE, new Position(1, 2));
        board.getPieces().add(k);
        board.getPieces().add(p);

        Assertions.assertEquals(0, p.getPossibleMoves().size());
        Assertions.assertEquals(3, k.getPossibleMoves().size());
    }

    @Test
    public void simpleCheckTakingPawn()
    {
        board.getPieces().add(new King(Side.BLACK, new Position(7, 6)));
        board.getPieces().add(new Bishop(Side.BLACK, new Position(6, 6)));
        King k = new King(Side.WHITE, new Position(0, 0));
        Pawn p = new Pawn(Side.WHITE, new Position(7, 7));
        board.getPieces().add(k);
        board.getPieces().add(p);

        Assertions.assertEquals(1, p.getPossibleMoves().size());
        Assertions.assertEquals(2, k.getPossibleMoves().size());
    }

    @Test
    public void doubleCheck()
    {
        board.getPieces().add(new King(Side.BLACK, new Position(7, 6)));
        board.getPieces().add(new Bishop(Side.BLACK, new Position(7, 7)));
        board.getPieces().add(new Knight(Side.BLACK, new Position(2, 1)));
        King k = new King(Side.WHITE, new Position(0, 0));
        Pawn p = new Pawn(Side.WHITE, new Position(0, 7));
        board.getPieces().add(k);
        board.getPieces().add(p);

        Assertions.assertEquals(0, p.getPossibleMoves().size());
        Assertions.assertEquals(2, k.getPossibleMoves().size());
    }

    @Test
    public void doubleCheckBlock()
    {
        board.getPieces().add(new King(Side.BLACK, new Position(7, 6)));
        board.getPieces().add(new Bishop(Side.BLACK, new Position(7, 7)));
        board.getPieces().add(new Knight(Side.BLACK, new Position(2, 1)));
        King k = new King(Side.WHITE, new Position(0, 0));
        Pawn p = new Pawn(Side.WHITE, new Position(1, 2));
        board.getPieces().add(k);
        board.getPieces().add(p);

        Assertions.assertEquals(0, p.getPossibleMoves().size());
        Assertions.assertEquals(2, k.getPossibleMoves().size());
    }

    @Test
    public void doubleCheckTakingKing()
    {
        board.getPieces().add(new King(Side.BLACK, new Position(7, 6)));
        board.getPieces().add(new Bishop(Side.BLACK, new Position(1, 1)));
        board.getPieces().add(new Knight(Side.BLACK, new Position(2, 1)));
        King k = new King(Side.WHITE, new Position(0, 0));
        Pawn p = new Pawn(Side.WHITE, new Position(1, 2));
        board.getPieces().add(k);
        board.getPieces().add(p);

        Assertions.assertEquals(0, p.getPossibleMoves().size());
        Assertions.assertEquals(3, k.getPossibleMoves().size());
    }

    @Test
    public void doubleCheckTakingPawn()
    {
        board.getPieces().add(new King(Side.BLACK, new Position(7, 6)));
        board.getPieces().add(new Bishop(Side.BLACK, new Position(6, 6)));
        board.getPieces().add(new Knight(Side.BLACK, new Position(2, 1)));
        King k = new King(Side.WHITE, new Position(0, 0));
        Pawn p = new Pawn(Side.WHITE, new Position(7, 7));
        board.getPieces().add(k);
        board.getPieces().add(p);

        Assertions.assertEquals(0, p.getPossibleMoves().size());
        Assertions.assertEquals(2, k.getPossibleMoves().size());
    }
}
