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
import org.beroot.android.util.Perf;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


/**
 * - prévoir 2 map : indéxé par nom de fichier et une indexée par somme MD5
 * - supprimer de la base les fichiers supprimés (pas les fichers dont la somme MD5 a changée) 
 * - si la somme MD5 a changé, update bdd
 * - si somme MD5 inchangée mais fichier renommé, update bdd
 * - si nouveau fichier insérer
 * - supprimer le truncate
 */

public class Sync extends AsyncTask<String, Void, Boolean>
{
  protected static final String GAMES = "1";

  protected static final String TSUMEGO = "2";

  private ProgressDialog _progressDialog;

  private Context _context;

  private DbGame loadedGame = null;

  public Sync(Context context)
  {
    _context = context;
  }

  @Override
  protected void onPreExecute()
  {
    _progressDialog = new ProgressDialog(_context);
    _progressDialog.setTitle("Please wait");
    _progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    _progressDialog.show();
  }

  @Override
  protected void onPostExecute(final Boolean success)
  {
    _progressDialog.dismiss();
  }

  @Override
  protected Boolean doInBackground(String... params)
  {
    List<DbGame> sgfs = new ArrayList<DbGame>();
    List<String> errors = new ArrayList<String>();
    File dir = new File("/sdcard/GoAndDroid/");
    String[] files = dir.list();
    DbGame game;
    MessageDigest digest = null;
    _progressDialog.setMax(files.length);
    Perf perf2 = new Perf();
    try
    {
      digest = MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException e)
    {
      e.printStackTrace();
      return false;
    }
    Sgf sgf = new Sgf();
    String md5 = "";
    for (String f : files)
    {
      try
      {
        game = new DbGame();
        sgf.setFileName("/sdcard/GoAndDroid/" + f); // TODO chemins à paramétrer
        GoGame gg = sgf.loadGame();
        
        // Calcul du MD5
        digest.update(sgf.getFileContent().getBytes());
        BigInteger bigInt = new BigInteger(1, digest.digest());
        md5 = bigInt.toString(16);

        if (fileNeedUpdate(f, md5))
        {
          game.setWhitePlayerName(gg.getWhitePlayerName());
          //game.setWhitePlayerName(f);
          game.setBlackPlayerName(gg.getBlackPlayerName());
          game.setBoardSize(gg.getBoardSize());
          game.setStones(gg.getStonesForBDD());
          game.setResult(gg.getResult());
          game.setFileName(f);
          game.setMd5(md5);
          sgfs.add(game);
        }
      }
      catch (Exception e)
      {
        Log.d("beroot", "SYNC: " + f, e);
        errors.add(f);
      }
      _progressDialog.incrementProgressBy(1);
    }
    Log.d("beroot", "TOTALSYNC1: " + perf2.getTime() + " s - ");

    DaoGame daoGame = new DaoGame(_context);
    daoGame.open();
    daoGame.truncate();
    daoGame.bulkInsert(sgfs);
    daoGame.close();
    Log.d("beroot", "TOTALSYNC2: " + perf2.getTime() + " s - ");
    Log.d("beroot", "Errors: " + errors);
    return true;
  }

  private boolean fileNeedUpdate(String filename, String md5)
  {
    return true;
  }
}
