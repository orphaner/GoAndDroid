package org.beroot.android.goengine;

public enum GobanSize
{
  G9(9), G13(13), G19(19);

  private int size;

  GobanSize(int size)
  {
    this.size = size;
  }

  public int getSize()
  {
    return size;
  }
}
