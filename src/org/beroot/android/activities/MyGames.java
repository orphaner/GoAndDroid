package org.beroot.android.activities;

import org.beroot.android.R;
import org.beroot.android.db.DaoGame;
import org.beroot.android.db.DbGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MyGames extends Activity
{
  private ListView lvListe;

  private DaoGame _daoGame;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mygames);
    _daoGame = new DaoGame(getBaseContext());
    _daoGame.open();

    MyGamesAdapter adapter = new MyGamesAdapter(this, _daoGame);
    lvListe = (ListView) findViewById(R.id.lvListe);
    lvListe.setAdapter(adapter);

    lvListe.setOnItemClickListener(new OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> a, View v, int position, long id)
      {
        Intent loadGameIntent = new Intent(getBaseContext(), LoadGame.class);
        DbGame game = (DbGame) lvListe.getItemAtPosition(position);
        loadGameIntent.putExtra("SGF", game.getFileName());
        startActivity(loadGameIntent);
      }
    });
  }

  @Override
  protected void onDestroy()
  {
    super.onDestroy();
    _daoGame.close();
  }
}
