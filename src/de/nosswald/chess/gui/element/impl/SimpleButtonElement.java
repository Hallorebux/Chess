package de.nosswald.chess.gui.element.impl;

import de.nosswald.chess.gui.AbsoluteSize;
import de.nosswald.chess.gui.Anchor;
import de.nosswald.chess.gui.CustomGraphics;
import de.nosswald.chess.gui.SizeReference;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public final class SimpleButtonElement extends ButtonElement
{
    private final String title;

    /**
     * @param title  the title
     * @param x      the x position
     * @param y      the y position
     * @param width  the width
     * @param height the height
     */
    public SimpleButtonElement(String title, SizeReference x, SizeReference y, SizeReference width, SizeReference height)
    {
        super(x, y, width, height);

        this.title = title;
    }

    /**
     * Paints the element
     *
     * @param graphics the {@link CustomGraphics}
     */
    @Override
    public void onPaint(CustomGraphics graphics)
    {
        super.onPaint(graphics);

        graphics.drawRect(x, y, width, height, isHovered() ? Color.RED : Color.BLUE);
        getContextGraphics(graphics).drawString(title, Anchor.CENTER, Anchor.CENTER, Color.WHITE, new Font("Arial", Font.PLAIN, new AbsoluteSize(14).get(0)));
    }

    /**
     * Called if a mouse button was clicked
     *
     * @param event the {@link MouseEvent}
     */
    @Override
    public void onClick(MouseEvent event)
    {
        super.onClick(event);
    }

    /**
     * @param graphics the {@link CustomGraphics}
     * @return the {@link CustomGraphics} but clipped and translated on the {@link SimpleButtonElement}
     */
    private CustomGraphics getContextGraphics(CustomGraphics graphics)
    {
        return graphics.translate(this.x, this.y).clip(graphics, this.width, this.height);
    }
}
