package org.beroot.android.goengine;

public class SgfProperty
{
  public String prop;
  public String value;

  public SgfProperty(String prop, String value)
  {
    super();
    this.prop = prop;
    this.value = value;
  }

  public String toString()
  {
    return prop + ":" + value;
  }
}
