package com.NoSuchCompany.app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for CarPlaylistCreator.
 */
public class CarPlaylistCreatorTest
    extends TestCase
{
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public CarPlaylistCreatorTest( String testName )
  {
    super( testName );
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite()
  {
    return new TestSuite( CarPlaylistCreatorTest.class );
  }

  /**
   * Rigourous Test :-)
   */
  public void testCarPlaylistCreator()
  {
    assertTrue( true );
  }
}
