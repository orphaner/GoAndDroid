package org.beroot.android.activities;

import org.beroot.android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Home extends Activity
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home);

    OnClickListener newGameListener = new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent intent = new Intent(getBaseContext(), NewGame.class);
        startActivity(intent);
      }
    };

    Button newGameButton = (Button) findViewById(R.id.newGameButton);
    newGameButton.setOnClickListener(newGameListener);

    OnClickListener myGamesListener = new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent intent = new Intent(getBaseContext(), MyGames.class);
        startActivity(intent);
      }
    };

    Button myGamesButton = (Button) findViewById(R.id.myGamesButton);
    myGamesButton.setOnClickListener(myGamesListener);

    OnClickListener syncListener = new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Sync sync = new Sync(Home.this);
        //sync.execute(Sync.TSUMEGO);
        sync.execute(Sync.GAMES);
      }
    };

    Button syncButton = (Button) findViewById(R.id.syncButton);
    syncButton.setOnClickListener(syncListener);
  }
}
