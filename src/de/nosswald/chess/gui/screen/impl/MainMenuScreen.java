package de.nosswald.chess.gui.screen.impl;

import de.nosswald.chess.Chess;
import de.nosswald.chess.game.Board;
import de.nosswald.chess.gui.RelativeSize;
import de.nosswald.chess.gui.element.impl.MainMenuButtonElement;
import de.nosswald.chess.gui.screen.Screen;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public final class MainMenuScreen extends Screen
{
    public MainMenuScreen()
    {
        Board board = new Board();
        this.elements.add(
                new MainMenuButtonElement("New Game", new RelativeSize(.25F), new RelativeSize(.25F), new RelativeSize(.5F), new RelativeSize(.1F))
                        .setAction(() -> {
                            Chess.getInstance().setBoard(board);
                            board.initialize();
                            Chess.getInstance().getFrame().setScreen(new BoardScreen());
                        }
        ));
        this.elements.add(
                new MainMenuButtonElement("Load Game", new RelativeSize(.25F), new RelativeSize(.45F), new RelativeSize(.5F), new RelativeSize(.1F))
                        .setAction(() -> Chess.getInstance().getFrame().setScreen(new LoadGameScreen()))
        );
        this.elements.add(
                new MainMenuButtonElement("Options", new RelativeSize(.25F), new RelativeSize(.65F), new RelativeSize(.5F), new RelativeSize(.1F))
                        .setAction(() -> Chess.getInstance().getFrame().setScreen(new OptionsScreen(this)))
        );
    }
}
