package view;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;

public class OutlineLabel extends JLabel {
    OutlineLabel(String text) {
        super(text);
        setForeground(Color.WHITE);
        setFont(new Font("Red Hat Text", Font.BOLD, 16));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String txt = getText();
        Font font = getFont();
        g2.setFont(font);

        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(frc, txt);

        Shape textShape = gv.getOutline();

        int x = 0;
        int y = g2.getFontMetrics().getAscent();
        AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
        Shape shifted = transform.createTransformedShape(textShape);

        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(Color.BLACK);
        g2.draw(shifted);
        g2.setColor(getForeground());
        g2.fill(shifted);

        g2.dispose();
    }


    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        return new Dimension(fm.stringWidth(getText()), fm.getHeight());
    }
}

