/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.torguet.t4maped;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author torguet
 */
public class T4MainFrame extends JFrame {

    private final T4DrawingPanel panel;

    private final T4PaletteFrame paletteFrame;

    private final T4SubtilePaletteFrame subtilePaletteFrame;

    private final T4TileAssemblerFrame tileAssemblerFrame;

    private File savedDirectory = null;

    /**
     * Creates new form MainFrame
     */
    public T4MainFrame() {
        initComponents();

        this.setTitle("T4 Map/City Editor");
        
        panel = new T4DrawingPanel();

        paletteFrame = new T4PaletteFrame(panel);
        paletteFrame.setVisible(true);

        subtilePaletteFrame = new T4SubtilePaletteFrame(panel);
        subtilePaletteFrame.setVisible(true);

        tileAssemblerFrame = new T4TileAssemblerFrame(panel, paletteFrame.getPanel(), subtilePaletteFrame.getPanel(), this);
        tileAssemblerFrame.setVisible(true);

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
                panel.mouseDragged(evt);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                panel.mouseMoved(e);
            }
        });
        

        // add the component to the frame to see it!
        jScrollPane2.setViewportView(panel);
        
        pack();

        resetViews();
    }

    private void resetViews() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        int paletteWidth = dimension.width / 3;

        int paletteHeight = dimension.height / 3;

        paletteFrame.setSize(paletteWidth, paletteHeight);

        subtilePaletteFrame.setSize(paletteWidth, paletteHeight);

        dimension.width -= paletteWidth;

        if (dimension.width < this.getWidth())
            this.setSize(dimension);

        this.setLocation(paletteWidth,0);
        paletteFrame.setLocation(0,0);
        subtilePaletteFrame.setLocation(0, paletteFrame.getHeight()+30);
        tileAssemblerFrame.setLocation(0, paletteFrame.getHeight()*2+30);
    }

    private void initComponents() {

        jScrollPane2 = new JScrollPane();
        jScrollPane2.getVerticalScrollBar().setUnitIncrement(16);
        JMenuBar jMenuBar1 = new JMenuBar();
        // Variables declaration - do not modify//GEN-BEGIN:variables
        JMenu jMenu1 = new JMenu();
        JMenuItem jMenuItemNew = new JMenuItem();
        JMenuItem jMenuItemEvenColor = new JMenuItem();
        JMenuItem jMenuItemOddColor = new JMenuItem();
        JMenuItem jMenuItemLoad = new JMenuItem();
        JMenuItem jMenuItemSave = new JMenuItem();
        JMenuItem jMenuItemQuit = new JMenuItem();
        JMenu jMenu2 = new JMenu();
        JMenuItem jMenuItemSelect = new JMenuItem();
        JMenuItem jMenuItemCopy = new JMenuItem();
        JMenuItem jMenuItemPaste = new JMenuItem();
        JMenuItem jMenuItemEmpty = new JMenuItem();
        JMenuItem jMenuItemUndo = new JMenuItem();
        JMenuItem jMenuItemRedo = new JMenuItem();
        jMenuItemOther = new JCheckBoxMenuItem();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jMenu1.setText("File");

        jMenuItemNew.setText("New");
        jMenuItemNew.addActionListener(this::jMenuItemNewActionPerformed);
        jMenu1.add(jMenuItemNew);

        jMenuItemLoad.setText("Open");
        jMenuItemLoad.addActionListener(evt -> jMenuItemLoadActionPerformed(evt, false));
        jMenu1.add(jMenuItemLoad);

        KeyStroke keyStrokeToOpen
                = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
        jMenuItemLoad.setAccelerator(keyStrokeToOpen);

        jMenuItemSave.setText("Save");
        jMenuItemSave.addActionListener(evt -> jMenuItemSaveActionPerformed(evt, false));
        jMenu1.add(jMenuItemSave);

        KeyStroke keyStrokeToSave
                = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        jMenuItemSave.setAccelerator(keyStrokeToSave);

        jMenuItemEvenColor.setText("Define Even Color");
        jMenuItemEvenColor.addActionListener(this::jMenuItemEvenColorActionPerformed);
        jMenu1.add(jMenuItemEvenColor);

        jMenuItemOddColor.setText("Define Odd Color");
        jMenuItemOddColor.addActionListener(this::jMenuItemOddColorActionPerformed);
        jMenu1.add(jMenuItemOddColor);

        jMenuItemQuit.setText("Quit");
        jMenuItemQuit.addActionListener(this::jMenuItemQuitActionPerformed);
        jMenu1.add(jMenuItemQuit);

        KeyStroke keyStrokeToQuit
                = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK);
        jMenuItemQuit.setAccelerator(keyStrokeToQuit);

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

        jMenuItemEmpty.setText("Empty the map/city");
        jMenuItemEmpty.addActionListener(this::jMenuItemEmptyActionPerformed);
        jMenu2.add(jMenuItemEmpty);

        jMenuItemOther.setText("Other...");
        jMenuItemOther.addActionListener(this::jMenuItemOtherActionPerformed);
        jMenu2.add(jMenuItemOther);

        jMenuBar1.add(jMenu2);

        JMenu jMenu3 = new JMenu();

        jMenu3.setText("Windows");

        JMenuItem jMenuItemResetViews = new JMenuItem();
        jMenuItemResetViews.setText("Reset Views");
        jMenuItemResetViews.addActionListener(e -> resetViews());
        jMenu3.add(jMenuItemResetViews);


        JMenuItem jMenuItemMainView = new JMenuItem();
        jMenuItemMainView.setText("Main View Zoom x 1");
        jMenuItemMainView.addActionListener(e -> {
            panel.setZoom(1);
            jScrollPane2.getVerticalScrollBar().setUnitIncrement(16);
        });
        jMenu3.add(jMenuItemMainView);

        JMenuItem jMenuItemMainView4 = new JMenuItem();
        jMenuItemMainView4.setText("Main View Zoom x 2");
        jMenuItemMainView4.addActionListener(e -> {
            panel.setZoom(2);
            jScrollPane2.getVerticalScrollBar().setUnitIncrement(16*2);
        });
        jMenu3.add(jMenuItemMainView4);

        JMenuItem jMenuItemMainView8 = new JMenuItem();
        jMenuItemMainView8.setText("Main View Zoom x 4");
        jMenuItemMainView8.addActionListener(e -> {
            panel.setZoom(4);
            jScrollPane2.getVerticalScrollBar().setUnitIncrement(16*4);
        });
        jMenu3.add(jMenuItemMainView8);

        JMenuItem jMenuItemPaletteView = new JMenuItem();
        jMenuItemPaletteView.setText("Palette View");
        jMenuItemPaletteView.addActionListener(e -> paletteFrame.setVisible(true));
        jMenu3.add(jMenuItemPaletteView);

        JMenuItem jMenuItemSubtilePaletteView = new JMenuItem();
        jMenuItemSubtilePaletteView.setText("SubTile Palette View");
        jMenuItemSubtilePaletteView.addActionListener(e -> subtilePaletteFrame.setVisible(true));
        jMenu3.add(jMenuItemSubtilePaletteView);

        JMenuItem jMenuItemTileEditorView = new JMenuItem();
        jMenuItemTileEditorView.setText("Tile Editor View");
        jMenuItemTileEditorView.addActionListener(e -> tileAssemblerFrame.setVisible(true));
        jMenu3.add(jMenuItemTileEditorView);


        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

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

    private void jMenuItemOddColorActionPerformed(ActionEvent actionEvent) {
        panel.chooseOddColor();
    }

    private void jMenuItemEvenColorActionPerformed(ActionEvent actionEvent) {
        panel.chooseEvenColor();
    }

    private void jMenuItemPasteActionPerformed(ActionEvent evt) {
        panel.paste();
    }

    private void jMenuItemCopyActionPerformed(ActionEvent evt) {
        panel.copy();
    }

    private void jMenuItemSelectActionPerformed(ActionEvent evt) {
        panel.selectMode();
    }

    public void jMenuItemLoadActionPerformed(ActionEvent ignoredEvt, boolean onlyTiles) {//GEN-FIRST:event_jMenuItemLoadActionPerformed
        if(!onlyTiles && panel.isModified()) {
            Object[] options = {"Yes","No"};
            int n = JOptionPane.showOptionDialog(this,
                    "The labyrinth was modified.\n"+
                    "Are you sure you want to loose your modifications ?",
                    "Quit without saving",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
             if(n != 0)
                return;
        }
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Labyrinth files", "s");
        chooser.setFileFilter(filter);
        if(savedDirectory != null) chooser.setCurrentDirectory(savedDirectory);
        int returnVal = chooser.showOpenDialog(this);
        savedDirectory = chooser.getCurrentDirectory();
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            // make sure the file name ends with .s
            String fileName = chooser.getSelectedFile().getName();
            String filePath = chooser.getSelectedFile().getAbsolutePath();
            if (!fileName.toLowerCase().endsWith(".s")) {
                fileName += ".s";
                filePath += ".s";
            }
            try {
                panel.loadLaby(filePath, onlyTiles);
                paletteFrame.refresh();
                subtilePaletteFrame.refresh();
                tileAssemblerFrame.refresh();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error while loading file "+fileName,
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(T4MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItemLoadActionPerformed

    public void jMenuItemSaveActionPerformed(ActionEvent ignoredEvt, boolean onlyTiles) {//GEN-FIRST:event_jMenuItemSaveActionPerformed
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Labyrinth files", "s");
        chooser.setFileFilter(filter);
        if(savedDirectory != null) chooser.setCurrentDirectory(savedDirectory);
        int returnVal = chooser.showSaveDialog(this);
        savedDirectory = chooser.getCurrentDirectory();
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            // make sure the file name ends with .s
            String fileName = chooser.getSelectedFile().getName();
            String filePath = chooser.getSelectedFile().getAbsolutePath();
            if (!fileName.toLowerCase().endsWith(".s")) {
                fileName += ".s";
                filePath += ".s";
            }
            // tests file existence
            File file = new File(filePath);
            if (file.exists()) {
                Object[] options = {"Yes","No"};
                int n = JOptionPane.showOptionDialog(this,
                    "The file "+fileName+" exists.\n"+
                    "Are you sure you want to overwrite it?",
                    "File exists",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
                if(n != 0)
                    return;
            }
            try {
                panel.saveLaby(filePath, onlyTiles);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error while saving file "+fileName,
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(T4MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItemSaveActionPerformed

    private void jMenuItemQuitActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemQuitActionPerformed
        // TODO add your handling code here:
        if(panel.isModified()) {
            Object[] options = {"Yes","No"};
            int n = JOptionPane.showOptionDialog(this,
                    "The labyrinth was modified.\n"+
                    "Are you sure you want to loose your modifications ?",
                    "Quit without saving",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
             if(n != 0)
                return;
        }
        this.setVisible(false);
        System.exit(0);
    }//GEN-LAST:event_jMenuItemQuitActionPerformed

    private void jMenuItemEmptyActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemEmptyActionPerformed
        panel.emptyLaby();

    }//GEN-LAST:event_jMenuItemEmptyActionPerformed

    private void jMenuItemUndoActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemWallActionPerformed
        panel.undo();
    }//GEN-LAST:event_jMenuItemWallActionPerformed

    private void jMenuItemOtherActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemOtherActionPerformed
        String s = (String)JOptionPane.showInputDialog(
                            this,
                            "Please type the number for filling the block",
                            "Block type selection",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            "");

        //If a string was returned, say so.
        if ((s != null) && (!s.isEmpty())) {
            System.out.println("You typed: " +s);
            try {
                int i = Integer.parseInt(s);
                if(i<0 || i > 255) {
                    JOptionPane.showMessageDialog(this,
                    "Please type in an integer between 0 and 99",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                    jMenuItemOther.setSelected(false); 
                } else {
                    panel.setCurrentValue(i);
                    jMenuItemOther.setSelected(true); 
                    jMenuItemOther.setText("Other ("+i+") ...");
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this,
                    "Please type in an integer between 0 and 99",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                jMenuItemOther.setSelected(false);
            }
        } else {
            jMenuItemOther.setSelected(false); 
        }
    }//GEN-LAST:event_jMenuItemOtherActionPerformed

    private void formWindowClosing(WindowEvent ignoredEvt) {//GEN-FIRST:event_formWindowClosing
        jMenuItemQuitActionPerformed(null);
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItemNewActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewActionPerformed
        panel.clear(false);
        paletteFrame.refresh();
        subtilePaletteFrame.refresh();
        tileAssemblerFrame.refresh();
    }//GEN-LAST:event_jMenuItemNewActionPerformed

    private void jMenuItemRedoActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemDoorActionPerformed
        panel.redo();
    }//GEN-LAST:event_jMenuItemDoorActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ex) {
            Logger.getLogger(T4MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new T4MainFrame().setVisible(true));
    }

    private JCheckBoxMenuItem jMenuItemOther;
    private JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
