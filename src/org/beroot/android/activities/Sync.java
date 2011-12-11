package org.beroot.android.activities;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.beroot.android.db.DaoGame;
import org.beroot.android.db.DbGame;
import org.beroot.android.goengine.GoGame;
import org.beroot.android.goengine.Sgf;

import android.content.Context;

// TODO : code dégueulasse !!
public class Sync
{

  private List<DbGame> sgfs = new ArrayList<DbGame>();

  private void fillList(Context context)
  {
    File dir = new File("/sdcard/GoAndDroid/");
    String[] files = dir.list();
    DbGame game;
    MessageDigest digest = null;
    for (String f : files)
    {
      try
      {
        digest = MessageDigest.getInstance("MD5");
      }
      catch (NoSuchAlgorithmException e)
      {
        e.printStackTrace();
      }

      Sgf sgf = new Sgf("/sdcard/GoAndDroid/" + f);
      GoGame gg = sgf.load(true);

      game = new DbGame();
      game.setWhitePlayerName(gg.getWhitePlayerName());
      game.setBlackPlayerName(gg.getBlackPlayerName());
      game.setStones(gg.getStonesForBDD());
      game.setResult(gg.getResult());
      game.setFileName(f);
      if (digest != null)
      {
        digest.update(sgf.getFileContent().getBytes());
        byte[] md5sum = digest.digest();
        BigInteger bigInt = new BigInteger(1, md5sum);
        String output = bigInt.toString(16);
        game.setMd5(output);
      }
      sgfs.add(game);
    }
    
    DaoGame daoGame = new DaoGame(context);
    daoGame.open();
    daoGame.truncate();
    for (DbGame g:sgfs)
    {
      daoGame.insert(g);
    }
    daoGame.close();
  }

  public void process(Context context)
  {
    fillList(context);
  }
}
