package de.nosswald.chess.gui.screen;

import de.nosswald.chess.Chess;
import de.nosswald.chess.gui.AbsoluteSize;
import de.nosswald.chess.gui.CustomGraphics;
import de.nosswald.chess.gui.RelativeSize;
import de.nosswald.chess.gui.element.Element;
import de.nosswald.chess.gui.element.impl.ResizeElement;
import de.nosswald.chess.gui.element.impl.titlebar.TitleBarComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nils Osswald
 * @author Noah Gerber
 */
public abstract class Screen extends JPanel
{
    /**
     * Contains the {@link TitleBarComponent}
     */
    private final TitleBarComponent titleBarComponent = new TitleBarComponent(
            new RelativeSize(0), new RelativeSize(0), new RelativeSize(1), new AbsoluteSize(30));

    /**
     * Contains the {@link ResizeElement}
     */
    private final ResizeElement resizeElement =
            new ResizeElement(new RelativeSize(.98F), new RelativeSize(.98F), new RelativeSize(.02F), new RelativeSize(.02F));

    /**
     * Contains all {@link Element}'s on the {@link Screen}
     */
    protected final List<Element> elements = new ArrayList<>();

    public Screen()
    {
        // add mouse listener
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent event)
            {
                titleBarComponent.onClick(event);
                resizeElement.onClick(event);

                elements.forEach(e -> e.onClick(event));
            }

            @Override
            public void mousePressed(MouseEvent event)
            {
                titleBarComponent.onPress(event);
                resizeElement.onPress(event);
            }
        });

        // add mouse motion listener
        this.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent event)
            {
                titleBarComponent.setMousePos(event.getX(), event.getY());
                resizeElement.setMousePos(event.getX(), event.getY());
                resizeElement.onMove();
                elements.forEach(e -> e.setMousePos(event.getX(), event.getY()));
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                resizeElement.onDrag(event);
                titleBarComponent.onDrag(event);
            }
        });
    }

    /**
     * Paints the screen
     *
     * @param graphics the {@link Graphics}
     */
    @Override
    public void paint(Graphics graphics)
    {
        super.paint(graphics);

        final CustomGraphics customGraphics = new CustomGraphics(graphics);

        // set background
        this.setBackground(new Color(229, 229, 229));

        // paint default elements
        titleBarComponent.onPaint(customGraphics);
        resizeElement.onPaint(customGraphics);

        // set cursor
        if (resizeElement.isHovered() || resizeElement.isDragging())
            setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        else
            setCursor(Cursor.getDefaultCursor());

        // paint elements
        elements.forEach(e -> e.onPaint(customGraphics.translate(new RelativeSize(0), new AbsoluteSize(30))));

        // draw centered debug lines
        if (Chess.DEBUG_MODE)
        {
            graphics.setColor(Color.BLUE);
            graphics.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
            graphics.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
        }
        repaint();
    }
}
