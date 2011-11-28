package org.beroot.android.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.beroot.android.R;

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

  private List<String> sgfs = new ArrayList<String>();

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mygames);
    fillList();

    MyGamesAdapter adapter = new MyGamesAdapter(this, sgfs);
    lvListe = (ListView) findViewById(R.id.lvListe);
    lvListe.setAdapter(adapter);

    lvListe.setOnItemClickListener(new OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> a, View v, int position, long id)
      {
        Intent loadGameIntent = new Intent(getBaseContext(), LoadGame.class);
        String sgfFile = (String) lvListe.getItemAtPosition(position);
        loadGameIntent.putExtra("SGF", sgfFile);
        startActivity(loadGameIntent);
      }
    });
  }

  private void fillList()
  {
    File dir = new File("/sdcard/GoAndDroid/");
    String[] files = dir.list();
    for (String f : files)
    {
      sgfs.add(f);
    }
  }
}
