package net.torguet.t4maped;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class T4TileAssemblerFrame  extends JFrame {


    private final T4TileAssemblerPanel panel;


    /**
     * Creates new form MainFrame
     */
    public T4TileAssemblerFrame(T4DrawingPanel drawingPanel) {
        initComponents();

        this.setTitle("T4 Tile Editor");

        panel = new T4TileAssemblerPanel(drawingPanel);

        //panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                panel.mousePressed(evt);
            }
            @Override
            public void mouseReleased(MouseEvent evt) {
                panel.mouseReleased(evt);
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
        jScrollPane1.setViewportView(panel);

        pack();
    }

    private void initComponents() {
        jScrollPane2 = new JScrollPane();
        jScrollPane2.getVerticalScrollBar().setUnitIncrement(16);

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        previousTileButton = new javax.swing.JButton();
        nextTileButton = new javax.swing.JButton();
        previousSubTile = new javax.swing.JButton();
        nextSubTile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        previousTileButton.setText("Previous Tile");
        previousTileButton.setToolTipText("");
        previousTileButton.addActionListener(this::previousTileButtonActionPerformed);

        nextTileButton.setText("Next Tile");
        nextTileButton.addActionListener(this::nextTileButtonActionPerformed);

        previousSubTile.setText("Previous Sub Tile");
        previousSubTile.addActionListener(this::previousSubTileActionPerformed);

        nextSubTile.setText("Next Sub Tile");
        nextSubTile.addActionListener(this::nextSubTileActionPerformed);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("1- Choose a Tile.\n2- Click on the Sub Tile\n3- Choose a Sub Tile\n4- You can also edit the Sub Tile\n in the bottom part of the window");
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(previousSubTile)
                                        .addComponent(previousTileButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(nextTileButton)
                                        .addComponent(nextSubTile))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(16, 16, 16)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(previousTileButton)
                                                        .addComponent(nextTileButton))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(previousSubTile)
                                                        .addComponent(nextSubTile))
                                                .addGap(26, 26, 26))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }

    private JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton nextSubTile;
    private javax.swing.JButton nextTileButton;
    private javax.swing.JButton previousSubTile;
    private javax.swing.JButton previousTileButton;


    public void refresh() {
        panel.refresh();
    }


    private void previousTileButtonActionPerformed(java.awt.event.ActionEvent evt) {
        panel.previousTile();
    }

    private void previousSubTileActionPerformed(java.awt.event.ActionEvent evt) {
        panel.previousSubTile();
    }

    private void nextTileButtonActionPerformed(java.awt.event.ActionEvent evt) {
        panel.nextTile();
    }

    private void nextSubTileActionPerformed(java.awt.event.ActionEvent evt) {
        panel.nextSubTile();
    }


}

