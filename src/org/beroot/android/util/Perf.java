package org.beroot.android.util;

public class Perf
{
  private long begin;

  public Perf()
  {
    init();
  }

  private void init()
  {
    begin = System.currentTimeMillis();
  }

  public float getTime()
  {
    long end = System.currentTimeMillis();
    return ((float) (end - begin)) / 1000f;
  }
}
