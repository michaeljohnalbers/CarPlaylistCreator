/**
 * @file MainWindow.java
 * @brief Main container window for UI.
 */

package com.NoSuchCompany.CarPlaylistCreator.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * Class to create the main window for the GUI. This houses all of the component
 * widgets as well.
 */
public class MainWindow extends JFrame {

  /**
   * Constructor.
   */
  public MainWindow() {
    super("Car Playlist Creator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    buildMenu();

    JPanel panel = new JPanel();
    add(panel);
    panel.setLayout(new GridBagLayout());

    GridBagConstraints constraints = null;

    MusicDirectoryTree mdt = new MusicDirectoryTree();
    constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.BOTH;
    constraints.gridwidth = 1;
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.weightx = 0.5;
    constraints.weighty = 1.0;
    panel.add(mdt, constraints);

    Playlists playlists = new Playlists();
    constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.BOTH;
    constraints.gridx = 1;
    constraints.gridy = 1;
    constraints.weightx = 0.5;
    constraints.weighty = 1.0;
    panel.add(playlists, constraints);

    constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridwidth = 2;
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weighty = 0.0;
    panel.add(new RootDirectoryPanel(mdt, playlists), constraints);

    pack();
    setVisible(true);
  }

  /**
   * Builds the menu for the window.
   */
  private void buildMenu() {
    setJMenuBar(menuBar_);

    JMenu fileMenu = new JMenu("File");
    menuBar_.add(fileMenu);
    JMenuItem quitItem = new JMenuItem("Quit");
    fileMenu.add(quitItem);
    quitItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          System.exit(0);
        }
      });
  }

  /** Menu bar */
  private JMenuBar menuBar_ = new JMenuBar();
}
