package org.beroot.android.goengine;

import java.util.ArrayList;
import java.util.List;

import org.beroot.android.util.Point;

public class GoConstants
{
  private static final List<Point> hoshis19x19;
  private static final List<Point> hoshis13x13;
  private static final List<Point> hoshis9x9;

  static
  {
    // hoshis 19
    hoshis19x19 = new ArrayList<Point>();
    hoshis19x19.add(new Point(4, 4));
    hoshis19x19.add(new Point(16, 4));
    hoshis19x19.add(new Point(4, 16));
    hoshis19x19.add(new Point(16, 16));
    hoshis19x19.add(new Point(10, 10));
    hoshis19x19.add(new Point(4, 10));
    hoshis19x19.add(new Point(10, 4));
    hoshis19x19.add(new Point(10, 16));
    hoshis19x19.add(new Point(16, 10));

    // hoshis 13
    hoshis13x13 = new ArrayList<Point>();
    hoshis13x13.add(new Point(4, 4));
    hoshis13x13.add(new Point(10, 10));
    hoshis13x13.add(new Point(4, 10));
    hoshis13x13.add(new Point(10, 4));
    hoshis13x13.add(new Point(7, 7));

    // hoshis 9
    hoshis9x9 = new ArrayList<Point>();
    hoshis9x9.add(new Point(3, 3));
    hoshis9x9.add(new Point(3, 7));
    hoshis9x9.add(new Point(7, 3));
    hoshis9x9.add(new Point(7, 7));
  }

  public static List<Point> getHoshis(GobanSize gobanSize)
  {
    switch (gobanSize.getSize())
    {
      case 9:
        return hoshis9x9;
      case 13:
        return hoshis13x13;
      case 19:
      default:
        return hoshis19x19;
    }
  }
}
