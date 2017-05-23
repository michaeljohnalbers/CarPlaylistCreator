/**
 * @file RootDirectoryPanel.java
 * @brief Panel containing widgets to select the root directory.
 */

package com.NoSuchCompany.CarPlaylistCreator.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Panel containing widgets to select the root directory of music and playlists.
 * This assumes they cohabitat within a single directory.
 */
public class RootDirectoryPanel extends JPanel implements ActionListener {

  /**
   * Constructor.
   */
  public RootDirectoryPanel(DirectoryChangeListener... listeners) {
    super();

    listeners_ = Arrays.asList(listeners);

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    add(new JLabel("Root Directory:"));
    add(rootDirectory_);
    JButton browseButton = new JButton("Browse");
    add(browseButton);
    browseButton.addActionListener(this);

    updateDirectory(Paths.get(".").toAbsolutePath().normalize().toString());
  }

  /**
   * Returns the root directory selected by the user.
   * @return root directory
   */
  public String getRootDirectory() {
    return rootDirectory_.getText();
  }

  /**
   * Action callback.
   * @param e action event
   */
  public void actionPerformed(ActionEvent e) {
    JFileChooser chooser = new JFileChooser();
    String initialDirectory = getRootDirectory();
    if ("".equals(initialDirectory)) {
      initialDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
    }
    chooser.setCurrentDirectory(new File(initialDirectory));
    chooser.setDialogTitle("Select Root Directory");
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    // disable the "All files" option.
    chooser.setAcceptAllFileFilterUsed(false);
    int selection = chooser.showOpenDialog(this);
    if (JFileChooser.APPROVE_OPTION == selection) {
      updateDirectory(chooser.getSelectedFile().getPath());
    }
  }

  /**
   * Performs all actions for changing root directory. Updates the test widget
   * and notifies listeners.
   *
   * @param newDirectoryString new directory
   */
  private void updateDirectory(String newDirectoryString) {
    Path newDirectory = Paths.get(newDirectoryString);
    rootDirectory_.setText(newDirectoryString);
    listeners_.forEach((s)->{s.directoryChanged(newDirectory);});
  }

  /** Text field containing the root directory. */
  private JTextField rootDirectory_ = new JTextField(40);

  /** Objects to be notified about directory changes. */
  private List<DirectoryChangeListener> listeners_;
}
