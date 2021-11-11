package de.nosswald.chess.gui.element.impl;

import de.nosswald.chess.Chess;
import de.nosswald.chess.gui.CustomGraphics;
import de.nosswald.chess.gui.Frame;
import de.nosswald.chess.gui.SizeReference;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public class ResizeElement extends ButtonElement
{
    private int posX, posY;
    private boolean dragging;

    /**
     * @param x      the x position
     * @param y      the y position
     * @param width  the width
     * @param height the height
     */
    public ResizeElement(SizeReference x, SizeReference y, SizeReference width, SizeReference height)
    {
        super(x, y, width, height);
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

        graphics.drawRect(x, y, width, height, new Color(0, 0, 0, 50));
    }

    public void onMove()
    {
        dragging = false;
    }

    /**
     * Called when the mouse is dragging the frame
     *
     * @param event the event
     */
    public void onDrag(MouseEvent event)
    {
        final Frame frame = Chess.getInstance().getFrame();

        if(isHovered() || dragging)
        {
            dragging = true;
            frame.setSize(
                    event.getXOnScreen() - frame.getX(),
                    event.getYOnScreen() - frame.getY()
            );
        }
    }

    /**
     * Called when a mouse button is hold down
     *
     * @param event the event
     */
    public void onPress(MouseEvent event)
    {
        if (!isHovered()) return;
        posX = event.getX();
        posY = event.getY();
    }

    /**
     * Called if a mouse button was clicked
     *
     * @param event the {@link MouseEvent}
     */
    @Override
    public void onClick(MouseEvent event) { }

    /**
     * @return If the resize flag is currently set
     */
    public boolean isDragging()
    {
        return dragging;
    }
}
