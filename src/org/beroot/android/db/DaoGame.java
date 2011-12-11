package org.beroot.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DaoGame
{
  private SQLiteDatabase bdd;

  private SqliteGame sqliteGame;

  public DaoGame(Context context)
  {
    sqliteGame = new SqliteGame(context, "GoAndDroid.db", null, 1);
  }

  public void open()
  {
    bdd = sqliteGame.getWritableDatabase();
  }

  public void close()
  {
    bdd.close();
  }

  public SQLiteDatabase getBDD()
  {
    return bdd;
  }

  public void truncate()
  {
    bdd.execSQL("DROP TABLE " + SqliteGame.TABLE_NAME);
    bdd.execSQL(SqliteGame.CREATE_TABLE);
  }

  public int getCount()
  {
    String query = new String("select count(*) from " + SqliteGame.TABLE_NAME);
    Cursor result = bdd.rawQuery(query, null);
    result.moveToFirst();
    return result.getInt(0);
  }

  public long insert(DbGame game)
  {
    ContentValues values = new ContentValues();
    values.put(SqliteGame.COL_WHITE_PLAYER_NAME, game.getWhitePlayerName());
    values.put(SqliteGame.COL_BLACK_PLAYER_NAME, game.getBlackPlayerName());
    values.put(SqliteGame.COL_RESULT, game.getResult());
    values.put(SqliteGame.COL_STONES, game.getStones());
    values.put(SqliteGame.COL_FILE_NAME, game.getFileName());
    values.put(SqliteGame.COL_MD5, game.getMd5());
    return bdd.insert(SqliteGame.TABLE_NAME, null, values);
  }

  public DbGame getGameFromId(int id)
  {
    /* @formatter:off */
    Cursor c = bdd.query(SqliteGame.TABLE_NAME,
        new String[] {
          SqliteGame.COL_ID,
          SqliteGame.COL_WHITE_PLAYER_NAME,
          SqliteGame.COL_BLACK_PLAYER_NAME,
          SqliteGame.COL_RESULT,
          SqliteGame.COL_STONES,
          SqliteGame.COL_FILE_NAME,
          SqliteGame.COL_MD5},
        SqliteGame.COL_ID + " = " + id + "", null, null, null, null);
    /* @formatter:on */
    return cursorToLivre(c);
  }

  private DbGame cursorToLivre(Cursor c)
  {
    if (c.getCount() == 0)
    {
      return null;
    }
    c.moveToFirst();
    DbGame game = new DbGame();
    game.setId(c.getInt(0));
    game.setWhitePlayerName(c.getString(1));
    game.setBlackPlayerName(c.getString(2));
    game.setResult(c.getString(3));
    game.setStones(c.getString(4));
    game.setFileName(c.getString(5));
    game.setMd5(c.getString(6));
    c.close();
    return game;
  }
}
