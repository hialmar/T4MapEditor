package net.torguet.t4maped;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;

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
        jScrollPane1.setViewportView(panel);

        pack();
    }

    private void initComponents() {
        JScrollPane jScrollPane2 = new JScrollPane();
        jScrollPane2.getVerticalScrollBar().setUnitIncrement(16);

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        
        JMenuBar jMenuBar1 = new JMenuBar();
        // Variables declaration - do not modify//GEN-BEGIN:variables
        JMenu jMenu1 = new JMenu();
        JMenuItem jMenuItemNew = new JMenuItem();
        JMenuItem jMenuItemLoad = new JMenuItem();
        JMenuItem jMenuItemSave = new JMenuItem();
        JMenu jMenu2 = new JMenu();
        JMenuItem jMenuItemSelect = new JMenuItem();
        JMenuItem jMenuItemCopy = new JMenuItem();
        JMenuItem jMenuItemPaste = new JMenuItem();
        JMenuItem jMenuItemUndo = new JMenuItem();
        JMenuItem jMenuItemRedo = new JMenuItem();

        jMenu1.setText("File");

        jMenuItemNew.setText("New");
        jMenuItemNew.addActionListener(this::jMenuItemNewActionPerformed);
        jMenu1.add(jMenuItemNew);

        jMenuItemLoad.setText("Open");
        jMenuItemLoad.addActionListener(this::jMenuItemLoadActionPerformed);
        jMenu1.add(jMenuItemLoad);

        KeyStroke keyStrokeToOpen
                = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
        jMenuItemLoad.setAccelerator(keyStrokeToOpen);

        jMenuItemSave.setText("Save");
        jMenuItemSave.addActionListener(this::jMenuItemSaveActionPerformed);
        jMenu1.add(jMenuItemSave);

        KeyStroke keyStrokeToSave
                = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        jMenuItemSave.setAccelerator(keyStrokeToSave);
        
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Tiles");

        jMenuItemSelect.setText("Select");
        jMenuItemSelect.addActionListener(this::jMenuItemSelectActionPerformed);
        jMenu2.add(jMenuItemSelect);

        jMenuItemCopy.setText("Copy");
        jMenuItemCopy.addActionListener(this::jMenuItemCopyActionPerformed);
        jMenu2.add(jMenuItemCopy);

        KeyStroke keyStrokeToCopy
                = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);
        jMenuItemCopy.setAccelerator(keyStrokeToCopy);

        jMenuItemPaste.setText("Paste");
        jMenuItemPaste.addActionListener(this::jMenuItemPasteActionPerformed);
        jMenu2.add(jMenuItemPaste);

        KeyStroke keyStrokeToPaste
                = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK);
        jMenuItemPaste.setAccelerator(keyStrokeToPaste);

        jMenuItemUndo.setText("Undo");
        jMenuItemUndo.addActionListener(this::jMenuItemUndoActionPerformed);
        jMenu2.add(jMenuItemUndo);

        KeyStroke keyStrokeToUndo
                = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
        jMenuItemUndo.setAccelerator(keyStrokeToUndo);


        jMenuItemRedo.setText("Redo");
        jMenuItemRedo.addActionListener(this::jMenuItemRedoActionPerformed);
        jMenu2.add(jMenuItemRedo);

        KeyStroke keyStrokeToRedo
                = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK|KeyEvent.SHIFT_DOWN_MASK);
        jMenuItemRedo.setAccelerator(keyStrokeToRedo);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);


        JButton previousTileButton = new JButton();
        JButton nextTileButton = new JButton();
        JButton previousSubTile = new JButton();
        JButton nextSubTile = new JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTextArea jTextArea1 = new JTextArea();

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
        jTextArea1.setText("1- Choose a Tile.\n2- Click on the Sub Tile\n3- Choose a Sub Tile\n4- You can also edit the Sub Tile\n in the right part of the window");
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

    private void jMenuItemLoadActionPerformed(ActionEvent actionEvent) {
    }

    private void jMenuItemSaveActionPerformed(ActionEvent actionEvent) {
    }

    private void jMenuItemSelectActionPerformed(ActionEvent actionEvent) {
    }

    private void jMenuItemCopyActionPerformed(ActionEvent actionEvent) {
    }

    private void jMenuItemPasteActionPerformed(ActionEvent actionEvent) {
    }

    private void jMenuItemUndoActionPerformed(ActionEvent actionEvent) {
    }

    private void jMenuItemRedoActionPerformed(ActionEvent actionEvent) {
    }

    private void jMenuItemNewActionPerformed(ActionEvent actionEvent) {
    }

    private javax.swing.JScrollPane jScrollPane1;


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

