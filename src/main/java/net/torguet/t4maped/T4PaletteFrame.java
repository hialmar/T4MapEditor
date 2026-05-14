package net.torguet.t4maped;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class T4PaletteFrame extends JFrame {


    private final T4PalettePanel panel;


    /**
     * Creates new form MainFrame
     */
    public T4PaletteFrame(T4DrawingPanel drawingPanel) {
        initComponents();

        this.setTitle("T4 Palette");

        panel = new T4PalettePanel(drawingPanel);

        //panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                panel.mousePressed(evt);
            }
            @Override
            public void mouseReleased(MouseEvent evt) {
                panel.mouseReleased();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.mouseEntered(e);
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent evt) {
                panel.mousePressed(evt);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                panel.mouseMoved(e);
            }
        });


        // add the component to the frame to see it!
        jScrollPane2.setViewportView(panel);

        pack();
    }


    private void initComponents() {

        jScrollPane2 = new JScrollPane();
        jScrollPane2.getVerticalScrollBar().setUnitIncrement(16);

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }

    private JScrollPane jScrollPane2;

    public void refresh() {
        panel.refresh();
    }


    public T4PalettePanel getPanel() {
        return panel;
    }
}
