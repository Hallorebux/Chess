import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.impl.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CheckmateTests
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
    public void checkmateBasicKQvK(int col)
    {
        board.getPieces().add(new King(Side.BLACK, new Position(col, 0)));
        board.getPieces().add(new Queen(Side.WHITE, new Position(col, 1)));
        board.getPieces().add(new King(Side.WHITE, new Position(col, 2)));

        Assertions.assertTrue(board.isCheckMate(Side.BLACK));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5})
    public void checkmateBasicKvKR(int row){
        board.getPieces().add(new King(Side.BLACK, new Position(0, row)));
        board.getPieces().add(new King(Side.WHITE, new Position(2, row)));
        board.getPieces().add(new Rook(Side.WHITE, new Position(0, 0)));

        Assertions.assertTrue(board.isCheckMate(Side.BLACK));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5, 6, 7})
    public void checkmateBasicKVBB(int offset)
    {
        board.getPieces().add(new King(Side.BLACK, new Position(0, 0)));
        board.getPieces().add(new King(Side.WHITE, new Position(1, 2)));
        board.getPieces().add(new Bishop(Side.WHITE, new Position(offset, offset)));
        board.getPieces().add(new Bishop(Side.WHITE, new Position(offset, offset - 1)));

        Assertions.assertTrue(board.isCheckMate(Side.BLACK));
    }

    @Test
    public void checkmateBasicKRvKPPP()
    {
        board.getPieces().add(new King(Side.BLACK, new Position(6, 0)));
        board.getPieces().add(new Pawn(Side.BLACK, new Position(7, 1)));
        board.getPieces().add(new Pawn(Side.BLACK, new Position(6, 1)));
        board.getPieces().add(new Pawn(Side.BLACK, new Position(5, 1)));
        board.getPieces().add(new King(Side.WHITE, new Position(0, 7)));
        board.getPieces().add(new Rook(Side.WHITE, new Position(0, 0)));

        Assertions.assertTrue(board.isCheckMate(Side.BLACK));
    }
}
