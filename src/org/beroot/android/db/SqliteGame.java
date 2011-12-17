package org.beroot.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteGame extends SQLiteOpenHelper
{
  public static final String TABLE_NAME = "GAME";
  public static final String COL_ID = "ID";
  public static final String COL_WHITE_PLAYER_NAME = "WHITE_PLAYER_NAME";
  public static final String COL_BLACK_PLAYER_NAME = "BLACK_PLAYER_NAME";
  public static final String COL_BOARD_SIZE = "COL_BOARD_SIZE";
  public static final String COL_RESULT = "RESULT";
  public static final String COL_STONES = "STONES";
  public static final String COL_FILE_NAME = "FILE_NAME";
  public static final String COL_MD5 = "MD5";

  /**
   * SQL de création de table
   */
  /* @formatter:off */
  public static final String CREATE_TABLE = 
    "CREATE TABLE " + TABLE_NAME + 
      " (" + 
        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COL_WHITE_PLAYER_NAME + " TEXT, " + 
        COL_BLACK_PLAYER_NAME + " TEXT, " + 
        COL_BOARD_SIZE + " INTEGER, " + 
        COL_RESULT + " TEXT, " + 
        COL_STONES + " TEXT, " +
        COL_FILE_NAME + " TEXT, " +
        COL_MD5 + " TEXT" +
      ");";
  /* @formatter:on */

  public SqliteGame(Context context, String name, CursorFactory factory, int version)
  {
    super(context, name, factory, version);
  }

  @Override
  public void onCreate(SQLiteDatabase db)
  {
    db.execSQL(CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  {
  }
}
