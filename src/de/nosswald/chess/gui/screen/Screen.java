package de.nosswald.chess.gui.screen;

import de.nosswald.chess.Chess;
import de.nosswald.chess.gui.AbsoluteSize;
import de.nosswald.chess.gui.CustomGraphics;
import de.nosswald.chess.gui.RelativeSize;
import de.nosswald.chess.gui.element.Element;
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
    private final TitleBarComponent titleBarComponent = new TitleBarComponent(new RelativeSize(0), new RelativeSize(0), new RelativeSize(1), new AbsoluteSize(30));
    protected final List<Element> elements = new ArrayList<>();

    public Screen()
    {
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent event)
            {
                titleBarComponent.onClick(event);
                elements.forEach(element -> element.onClick(event));
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent event)
            {
                titleBarComponent.setMousePos(event.getX(), event.getY());
                elements.forEach(element -> element.setMousePos(event.getX(), event.getY()));
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                titleBarComponent.onDrag(event);
            }
        });
    }

    @Override
    public void paint(Graphics graphics)
    {
        super.paint(graphics);

        final CustomGraphics customGraphics = new CustomGraphics(graphics);

        // set background
        this.setBackground(new Color(229, 229, 229));

        // paint title bar
        titleBarComponent.onPaint(customGraphics);

        // paint elements
        elements.forEach(element -> element.onPaint(customGraphics.translate(new RelativeSize(0), new AbsoluteSize(30))));

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
