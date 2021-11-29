import com.winkelhagen.chess.syzygy.SyzygyBridge;
import com.winkelhagen.chess.syzygy.SyzygyConstants;
import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Position;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.Piece;
import de.nosswald.chess.game.piece.impl.*;
import de.nosswald.chess.logger.LoggerLevel;
import javafx.geometry.Pos;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TablebaseTests
{
    private Board board;

    @BeforeAll
    public static void setupBoard()
    {
        Chess.DEBUG_MODE = false;
        new Chess(false);
        SyzygyBridge.load("tablebase/");
        Chess.getLogger().printFormat(LoggerLevel.INFO, "Loaded Syzygy %s", SyzygyBridge.getSupportedSize());
    }

    @BeforeEach
    public void before()
    {
        board = new Board();
        Chess.getInstance().setBoard(board);
    }

    @ParameterizedTest
    @MethodSource
    public void tablebaseRandomBoard(int blackPieces, int whitePieces)
    {
        board.setLegitimacyChecking(false);
        do {
            board.getPieces().clear();

            Position pos1 = getRandomPosition(board);
            Position pos2;
            do {
                pos2 = getRandomPosition(board);
            }
            while (Math.abs(pos2.getCol() - pos1.getCol()) <= 1 || Math.abs(pos2.getRow() - pos1.getRow()) <= 1);
            board.getPieces().add(new King(Side.WHITE, pos1));
            board.getPieces().add(new King(Side.BLACK, pos2));

            for (int i = 0; i < blackPieces; i++)
                board.getPieces().add(getRandomPiece(Side.BLACK, getRandomPosition(board)));
            for (int i = 0; i < whitePieces; i++)
                board.getPieces().add(getRandomPiece(Side.WHITE, getRandomPosition(board)));
        } while(board.isInCheck(Side.BLACK));
        board.setLegitimacyChecking(true);

        Chess.printBoard(board);

        int result = SyzygyBridge.probeSyzygyDTZ(
                Board.getBitboard(board.getPieces().stream().filter(p -> p.getSide() == Side.WHITE)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p.getSide() == Side.BLACK)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p instanceof King)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p instanceof Queen)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p instanceof Rook)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p instanceof Bishop)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p instanceof Knight)),
                Board.getBitboard(board.getPieces().stream().filter(p -> p instanceof Pawn)),
                0,
                0, // TODO en passant square
                board.getNextSide() == Side.WHITE
        );
        Assertions.assertNotEquals(-1, result);


        int winDrawLoss = SyzygyConstants.winDrawLoss(result);
        int fromEncoded = SyzygyConstants.fromSquare(result);
        int toEncoded = SyzygyConstants.toSquare(result);
        int promoteInto = SyzygyConstants.promoteInto(result);
        int distanceToZero = SyzygyConstants.distanceToZero(result);

        Position from = new Position(fromEncoded % 8, 7 - (fromEncoded / 8));
        Position to = new Position(toEncoded % 8, 7 - (toEncoded / 8));
        Piece fromPiece = board.getPiece(from);
        System.out.printf("Evaluation is %s %s %s %s\n", winDrawLoss, from, to, distanceToZero);

        if (board.isStalemate())
        {
            Assertions.assertEquals(SyzygyConstants.TB_DRAW, winDrawLoss);
        }
        else if (board.isCheckMate(board.getNextSide().flip()))
        {
            Assertions.assertEquals(SyzygyConstants.TB_LOSS, winDrawLoss);
        }
        else if (board.isCheckMate(board.getNextSide()))
        {
            Assertions.assertEquals(SyzygyConstants.TB_WIN, winDrawLoss);
        }
        else
        {
            Assertions.assertNotEquals(null, fromPiece);
            Assertions.assertTrue(fromPiece.getPossibleMoves().stream().anyMatch(p -> p.getTo().equals(to)));
        }
    }

    private static Random rnd = new Random(73981);
    private static Position getRandomPosition(Board b)
    {
        Position p;
        do {
            p = new Position(rnd.nextInt(8), rnd.nextInt(8));
        } while(b.getPiece(p) != null);
        return p;
    }

    private static Piece getRandomPiece(Side side, Position position)
    {
        switch (rnd.nextInt(5))
        {
            case 0: return (position.getRow() == 0 || position.getRow() == 7) ? new Queen(side, position) : new Pawn(side, position);
            case 1: return new Rook(side, position);
            case 2: return new Knight(side, position);
            case 3: return new Bishop(side, position);
            case 4: return new Queen(side, position);
            default: return null;
        }
    }

    private static Stream<Arguments> tablebaseRandomBoard()
    {
        // Hallo Kai aus der vergangenheit hier. Nein das ist nicht empty.
        // Das repeatet den stream quasi
        // IntelliJ analysis ist einfach schlecht.
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(0, 1),
                Arguments.of(0, 2),
                Arguments.of(1, 0),
                Arguments.of(1, 1),
                Arguments.of(1, 2),
                Arguments.of(2, 1),
                Arguments.of(0, 3),
                Arguments.of(3, 0)
        ).collect(() -> new ArrayList<Arguments>(), (a, b) -> {
            for (int i = 0; i < 1000; i++) a.add(b);
        }, (a, b) -> a.addAll(b)).stream();
    }
}
