package org.beroot.android.goengine;

public class SgfMarker
{
  public int x;
  public int y;
  public String text;

  public SgfMarker(int x, int y, String text)
  {
    super();
    this.x = x;
    this.y = y;
    this.text = text;
  }

  public String toString()
  {
    return x + ":" + y + " -> " + text;
  }
}
