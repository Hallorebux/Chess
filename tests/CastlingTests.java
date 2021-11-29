import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Move;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.impl.King;
import de.nosswald.chess.game.piece.impl.Queen;
import de.nosswald.chess.game.piece.impl.Rook;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CastlingTests
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
    public void longCastleBasicInvalid()
    {
        King k = new King(Side.WHITE, new Position(3, 7));
        Rook r = new Rook(Side.WHITE, new Position(0, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new King(Side.BLACK, new Position(3, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(0, count);
    }

    @Test
    public void longCastleBasicInvalidPiece()
    {
        King k = new King(Side.WHITE, new Position(4, 7));
        Queen r = new Queen(Side.WHITE, new Position(0, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new King(Side.BLACK, new Position(3, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(0, count);
    }

    @Test
    public void longCastleBasic()
    {
        King k = new King(Side.WHITE, new Position(4, 7));
        Rook r = new Rook(Side.WHITE, new Position(0, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new King(Side.BLACK, new Position(4, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(1, count);

        board.makeMove(k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).findFirst().get(), false);
        Assertions.assertEquals(2, k.getPosition().getCol());
        Assertions.assertEquals(7, k.getPosition().getRow());
        Assertions.assertEquals(3, r.getPosition().getCol());
        Assertions.assertEquals(7, r.getPosition().getRow());
    }

    @Test
    public void longCastleBasic2()
    {
        King k = new King(Side.WHITE, new Position(4, 7));
        Rook r = new Rook(Side.WHITE, new Position(0, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new Rook(Side.BLACK, new Position(7, 0)));
        board.getPieces().add(new Rook(Side.BLACK, new Position(0, 0)));
        board.getPieces().add(new King(Side.BLACK, new Position(4, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(1, count);

        board.makeMove(k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).findFirst().get(), false);
        Assertions.assertEquals(2, k.getPosition().getCol());
        Assertions.assertEquals(7, k.getPosition().getRow());
        Assertions.assertEquals(3, r.getPosition().getCol());
        Assertions.assertEquals(7, r.getPosition().getRow());
    }

    @Test
    public void longCastleThroughCheck1()
    {
        King k = new King(Side.WHITE, new Position(4, 7));
        Rook r = new Rook(Side.WHITE, new Position(0, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new Rook(Side.BLACK, new Position(1, 0)));
        board.getPieces().add(new King(Side.BLACK, new Position(3, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(0, count);
    }

    @Test
    public void longCastleThroughCheck2()
    {
        King k = new King(Side.WHITE, new Position(4, 7));
        Rook r = new Rook(Side.WHITE, new Position(0, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new Rook(Side.BLACK, new Position(2, 0)));
        board.getPieces().add(new King(Side.BLACK, new Position(3, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(0, count);
    }

    @Test
    public void longCastleThroughCheck3()
    {
        King k = new King(Side.WHITE, new Position(4, 7));
        Rook r = new Rook(Side.WHITE, new Position(0, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new Rook(Side.BLACK, new Position(3, 0)));
        board.getPieces().add(new King(Side.BLACK, new Position(3, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(0, count);
    }

    @Test
    public void longCastleInCheck()
    {
        King k = new King(Side.WHITE, new Position(4, 7));
        Rook r = new Rook(Side.WHITE, new Position(0, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new Rook(Side.BLACK, new Position(4, 1)));
        board.getPieces().add(new King(Side.BLACK, new Position(4, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(0, count);
    }

    @Test
    public void shortCastleBasicInvalid()
    {
        King k = new King(Side.WHITE, new Position(3, 7));
        Rook r = new Rook(Side.WHITE, new Position(7, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new King(Side.BLACK, new Position(3, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(0, count);
    }

    @Test
    public void shortCastleBasicInvalidPiece()
    {
        King k = new King(Side.WHITE, new Position(4, 7));
        Queen r = new Queen(Side.WHITE, new Position(7, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new King(Side.BLACK, new Position(3, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(0, count);
    }

    @Test
    public void shortCastleBasic1()
    {
        King k = new King(Side.WHITE, new Position(4, 7));
        Rook r = new Rook(Side.WHITE, new Position(7, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new King(Side.BLACK, new Position(3, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(1, count);

        board.makeMove(k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).findFirst().get(), false);
        Assertions.assertEquals(6, k.getPosition().getCol());
        Assertions.assertEquals(7, k.getPosition().getRow());
        Assertions.assertEquals(5, r.getPosition().getCol());
        Assertions.assertEquals(7, r.getPosition().getRow());
    }

    @Test
    public void shortCastleBasic2()
    {
        King k = new King(Side.WHITE, new Position(4, 7));
        Rook r = new Rook(Side.WHITE, new Position(7, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new Rook(Side.BLACK, new Position(0, 0)));
        board.getPieces().add(new Rook(Side.BLACK, new Position(7, 0)));
        board.getPieces().add(new King(Side.BLACK, new Position(4, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(1, count);

        board.makeMove(k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).findFirst().get(), false);
        Assertions.assertEquals(6, k.getPosition().getCol());
        Assertions.assertEquals(7, k.getPosition().getRow());
        Assertions.assertEquals(5, r.getPosition().getCol());
        Assertions.assertEquals(7, r.getPosition().getRow());
    }

    @Test
    public void shortCastleThroughCheck1()
    {
        King k = new King(Side.WHITE, new Position(4, 7));
        Rook r = new Rook(Side.WHITE, new Position(7, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new Rook(Side.BLACK, new Position(5, 0)));
        board.getPieces().add(new King(Side.BLACK, new Position(3, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(0, count);
    }

    @Test
    public void shortCastleThroughCheck2()
    {
        King k = new King(Side.WHITE, new Position(4, 7));
        Rook r = new Rook(Side.WHITE, new Position(7, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new Rook(Side.BLACK, new Position(6, 0)));
        board.getPieces().add(new King(Side.BLACK, new Position(3, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(0, count);
    }

    @Test
    public void shortCastleInCheck()
    {
        King k = new King(Side.WHITE, new Position(4, 7));
        Rook r = new Rook(Side.WHITE, new Position(7, 7));
        board.getPieces().add(k);
        board.getPieces().add(r);
        board.getPieces().add(new Rook(Side.BLACK, new Position(4, 1)));
        board.getPieces().add(new King(Side.BLACK, new Position(5, 0)));

        long count = k.getPossibleMoves().stream().filter(p -> p.getFlag() == Move.Flag.CASTLING).count();
        Assertions.assertEquals(0, count);
    }
}
