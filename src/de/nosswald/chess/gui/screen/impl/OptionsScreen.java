package de.nosswald.chess.gui.screen.impl;

import com.sun.istack.internal.NotNull;
import de.nosswald.chess.Chess;
import de.nosswald.chess.gui.RelativeSize;
import de.nosswald.chess.gui.element.impl.mainmenu.MainMenuButtonElement;
import de.nosswald.chess.gui.screen.Screen;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public final class OptionsScreen extends Screen
{
    /**
     * @param parent the parent screen to return to after closing this one
     */
    public OptionsScreen(@NotNull Screen parent)
    {
        this.elements.add(
                new MainMenuButtonElement("Save", new RelativeSize(.25F), new RelativeSize(0.75F), new RelativeSize(.5F), new RelativeSize(0.1F))
                        .setAction(() -> Chess.getInstance().getFrame().setScreen(parent))
        );
    }
}
