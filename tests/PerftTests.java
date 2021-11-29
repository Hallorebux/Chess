import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.game.Move;
import de.nosswald.chess.game.Side;
import de.nosswald.chess.game.piece.Piece;
import de.nosswald.chess.game.piece.impl.King;
import de.nosswald.chess.gui.screen.impl.BoardScreen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class PerftTests {
    private Board board;

    @BeforeAll
    public static void setupBoard()
    {
        Chess.DEBUG_MODE = false;
        new Chess(false);
    }

    @BeforeEach
    public void before()
    {
        board = new Board();
        Chess.getInstance().setBoard(board);
    }

    @ParameterizedTest
    @MethodSource
    public void startPositionPerft(
            Long depth,
            Long nodes,
            Long captures,
            Long enPassant,
            Long castles,
            Long promotions,
            Long checks,
            Long checkmate
    ) {
        board.initialize();

        PerftResults results = new PerftResults();
        recPerft(depth, results);

        Assertions.assertEquals(captures, results.getCaptures());
        Assertions.assertEquals(enPassant, results.getEnPassant());
        Assertions.assertEquals(castles, results.getCastles());
        Assertions.assertEquals(promotions, results.getPromotions());
        Assertions.assertEquals(checks, results.getChecks());
        Assertions.assertEquals(checkmate, results.getCheckmate());
        Assertions.assertEquals(nodes, results.getNodes());
    }

    @ParameterizedTest
    @MethodSource
    public void perft2(
            Long depth,
            Long nodes,
            Long captures,
            Long enPassant,
            Long castles,
            Long promotions,
            Long checks,
            Long checkmate
    ) {


        PerftResults results = new PerftResults();
        recPerft(depth, results);

        Assertions.assertEquals(captures, results.getCaptures());
        Assertions.assertEquals(enPassant, results.getEnPassant());
        Assertions.assertEquals(castles, results.getCastles());
        Assertions.assertEquals(promotions, results.getPromotions());
        Assertions.assertEquals(checks, results.getChecks());
        Assertions.assertEquals(checkmate, results.getCheckmate());
        Assertions.assertEquals(nodes, results.getNodes());
    }

    private final class PerftResults
    {
        private Long nodes = 0L;
        private Long captures = 0L;
        private Long castles = 0L;
        private Long promotions = 0L;
        private Long checks = 0L;
        private Long checkmate = 0L;
        private Long enPassant = 0L;

        public Long getEnPassant() {
            return enPassant;
        }

        public void setEnPassant(Long enPassant){
            this.enPassant =enPassant;
        }

        public Long getNodes() {
            return nodes;
        }

        public void setNodes(Long nodes) {
            this.nodes = nodes;
        }

        public Long getCaptures() {
            return captures;
        }

        public void setCaptures(Long captures) {
            this.captures = captures;
        }

        public Long getCastles() {
            return castles;
        }

        public void setCastles(Long castles) {
            this.castles = castles;
        }

        public Long getPromotions() {
            return promotions;
        }

        public void setPromotions(Long promotions) {
            this.promotions = promotions;
        }

        public Long getChecks() {
            return checks;
        }

        public void setChecks(Long checks) {
            this.checks = checks;
        }

        public Long getCheckmate() {
            return checkmate;
        }

        public void setCheckmate(Long checkmate) {
            this.checkmate = checkmate;
        }
    }

    private void recPerft(long depth, PerftResults perftResults) {
        if (depth == 0)
        {
            perftResults.setNodes(perftResults.getNodes() + 1);
            return;
        }

        Side s = board.getNextSide();
        for (Object p : board.getPieces().toArray())
        {
            if (((Piece)p).getSide() != s) continue;
            for (Move m : ((Piece)p).getPossibleMoves())
            {
                board.makeMove(m, true);
                boolean checkmate = board.isCheckMate(board.getNextSide());
                if (board.isInCheck(board.getNextSide())) perftResults.setChecks(perftResults.getChecks() + 1);
                if (checkmate) {
                    perftResults.setCheckmate(perftResults.getCheckmate() + 1);
                }
                else {
                    recPerft(depth - 1, perftResults);
                }
                board.unmakeMove(m, true);
                if (m.getCapturedPiece() != null) perftResults.setCaptures(perftResults.getCaptures() + 1);
                if (m.getFlag() == Move.Flag.EN_PASSANT) perftResults.setEnPassant(perftResults.getEnPassant() + 1);
                if (m.getFlag() == Move.Flag.CASTLING) perftResults.setCastles(perftResults.getCastles() + 1);
                if (m.getFlag() == Move.Flag.PROMOTION) perftResults.setPromotions(perftResults.getPromotions() + 1);
            }
        }
    }

    private static Stream<Arguments> startPositionPerft() {
        return Stream.of(
                Arguments.arguments(0L, 1L, 0L, 0L, 0L, 0L, 0L, 0L),
                Arguments.arguments(1L, 20L, 0L, 0L, 0L, 0L, 0L, 0L),
                Arguments.arguments(2L, 400L, 0L, 0L, 0L, 0L, 0L, 0L),
                Arguments.arguments(3L, 8902L, 34L, 0L, 0L, 0L, 12L, 0L),
                Arguments.arguments(4L, 197281L, 1576L, 0L, 0L, 0L, 469L, 8L),
                Arguments.arguments(5L, 4865609L, 82719L, 258L, 0L, 0L, 27351L, 347L),
                Arguments.arguments(6L, 119060324L, 2812008L, 5248L, 0L, 0L, 809099L, 10828L),
                Arguments.arguments(7L, 3195901860L, 108329926L, 319617L, 883453L, 0L, 33103848L, 435767L)
        );
    }
}
