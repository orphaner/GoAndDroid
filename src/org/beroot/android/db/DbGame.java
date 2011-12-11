package org.beroot.android.db;

public class DbGame
{
  private int id;
  private String blackPlayerName;
  private String whitePlayerName;
  private String result;
  private String stones;
  private String fileName;
  private String md5;

  public DbGame()
  {
  }

  public DbGame(String blackPlayerName, String whitePlayerName, String result, String stones, String fileName, String md5)
  {
    super();
    this.blackPlayerName = blackPlayerName;
    this.whitePlayerName = whitePlayerName;
    this.result = result;
    this.stones = stones;
    this.fileName = fileName;
    this.md5 = md5;
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getBlackPlayerName()
  {
    return blackPlayerName;
  }

  public void setBlackPlayerName(String blackPlayerName)
  {
    this.blackPlayerName = blackPlayerName;
  }

  public String getWhitePlayerName()
  {
    return whitePlayerName;
  }

  public void setWhitePlayerName(String whitePlayerName)
  {
    this.whitePlayerName = whitePlayerName;
  }

  public String getResult()
  {
    return result;
  }

  public void setResult(String result)
  {
    this.result = result;
  }

  public String getStones()
  {
    return stones;
  }

  public void setStones(String stones)
  {
    this.stones = stones;
  }

  public String getMd5()
  {
    return md5;
  }

  public void setMd5(String md5)
  {
    this.md5 = md5;
  }

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
}
