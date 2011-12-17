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
    values.put(SqliteGame.COL_BOARD_SIZE, game.getBoardSize());
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
          SqliteGame.COL_BOARD_SIZE,
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
    int i = 0;
    DbGame game = new DbGame();
    game.setId(c.getInt(i++));
    game.setWhitePlayerName(c.getString(i++));
    game.setBlackPlayerName(c.getString(i++));
    game.setBoardSize(c.getInt(i++));
    game.setResult(c.getString(i++));
    game.setStones(c.getString(i++));
    game.setFileName(c.getString(i++));
    game.setMd5(c.getString(i++));
    c.close();
    return game;
  }
}
