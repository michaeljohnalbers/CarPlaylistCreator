/**
 * @file CarPlaylistCreator.java
 * @brief Main class
 */

package com.NoSuchCompany.CarPlaylistCreator;

import com.NoSuchCompany.CarPlaylistCreator.ui.MainWindow;

/**
 *
 */
public class CarPlaylistCreator
{
  public static void main(String[] args)
  {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          new MainWindow();
        }
      });
  }
}
