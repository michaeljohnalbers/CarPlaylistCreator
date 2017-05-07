/**
 * @file Playlists.java
 * @brief Lists playlists and provides editing abilities.
 */

package com.NoSuchCompany.CarPlaylistCreator.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;

public class Playlists extends JPanel {
  /**
   * Constructor.
   */
  public Playlists() {
    super();
    setLayout(new GridBagLayout());

    JComboBox<String> playlistSelector = new JComboBox<>();

    // TODO: remove
    playlistSelector.addItem("HeavyMetal");
    playlistSelector.addItem("WorkCD1");
    playlistSelector.addItem("WorkCD2");

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 1.0;
    constraints.weighty = 0.0;
    add(playlistSelector, constraints);

    DefaultListModel<String> listModel = new DefaultListModel<>();

    JList<String> playlistEntries = new JList<>(listModel);
    JScrollPane playlistScroller = new JScrollPane(playlistEntries);
    constraints.fill = GridBagConstraints.BOTH;
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.weightx = 1.0;
    constraints.weighty = 1.0;
    add(playlistScroller, constraints);

    // TODO: remove
    listModel.addElement("Lost In The Static");
    listModel.addElement("CAFO");
    listModel.addElement("Trespasses");
  }
}
