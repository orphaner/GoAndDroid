package org.beroot.android.goengine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.beroot.android.util.Perf;

import android.util.Log;

public class Sgf
{
  private String _fileName;
  private GoGame _game;
  private String _content;
  private int _cursor = 0;
  private boolean _keepFileInMemory = false;

  public Sgf(String fileName)
  {
    _fileName = fileName;
  }

  public GoGame load(boolean keepFileInMemory)
  {
    _keepFileInMemory = keepFileInMemory;
    return load();
  }

  public String getFileContent()
  {
    return _content;
  }

  public GoGame load()
  {
    Perf perf = new Perf();
    try
    {
      readFile();
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
      return null;
    }
    _game = new GoGame();

    while (!eof())
    {
      findOpenParentesis();
      buildTree(null);
    }

    if (!_keepFileInMemory)
    {
      _content = null;
    }

    Log.d("beroot", "Load SGF: " + perf.getTime() + "s");
    return _game;
  }

  private void buildTree(GoNode node)
  {
    boolean first = true;
    while (!eof())
    {
      _cursor++;
      switch (_content.charAt(_cursor))
      {
        case ';':
          _cursor++;
          node = createNewGoNode(node, first);
          first = false;
          break;

        case '(':
          buildTree(node);
          break;

        case ')':
          return;
      }
    }
  }

  private GoNode createNewGoNode(GoNode parent, boolean variation)
  {
    GoNode newNode = new GoNode();
    newNode = fillProperties(newNode);
    if (parent == null)
    {
      _game.setFirstNode(newNode);
    }
    else
    {
      if (variation)
      {
        if (parent.getNextNode() == null)
        {
          parent.setNextNode(newNode);
        }
        else
        {
          parent.addVariation(newNode);
        }
      }
      else
      {
        parent.setNextNode(newNode);
      }
    }
    return newNode;
  }

  private GoNode fillProperties(GoNode node)
  {
    char c, lc = '-';
    String prop = "";
    String value = "";
    boolean startValue = false;
    while (!eof())
    {
      c = _content.charAt(_cursor);
      switch (c)
      {
        // End Node
        case '(':
        case ')':
        case ';':
          _cursor--;
          return node;

        case ']':
          _cursor++;
          c = _content.charAt(_cursor);
          if (c != '[')
          {
            node.addProp(prop, value);
            prop = "";
            value = "";
            startValue = false;
            _cursor--;
          }
          else
          {
            value += "|";
          }
          break;

        case '[':
          startValue = true;
          break;

        default:
          if (startValue)
          {
            while (!eof() && (c != ']' || (c == ']' && lc == '\\')))
            {
              value += c;
              _cursor++;
              lc = c;
              c = _content.charAt(_cursor);
            }
            _cursor--;
          }
          else if (Character.isUpperCase(c))
          {
            prop += c;
          }
          break;
      }
      _cursor++;
    }
    return node;
  }

  private void findOpenParentesis()
  {
    while (!eof() && _content.charAt(_cursor) != '(')
    {
      _cursor++;
    }
  }

  private boolean eof()
  {
    return _cursor >= _content.length();
  }

  private void readFile() throws IOException
  {
    // 1 - Création de la ressource
    FileReader reader = new FileReader(new File(_fileName));
    try
    {
      // 2 - Utilisation de la ressource
      StringBuffer buffer = new StringBuffer();
      char[] cbuf = new char[2048];
      int len;
      while ((len = reader.read(cbuf)) > 0)
      {
        buffer.append(cbuf, 0, len);
      }
      _content = buffer.toString();
    }
    finally
    {
      // 3 - Libération de la ressource
      reader.close();
    }
  }

  public static void main(String args[])
  {
    //Sgf sgf = new Sgf("/home/nicolas/android/go/Lee-Changho-vs-Xie-He-20111123.sgf");
    Sgf sgf = new Sgf("/home/nicolas/Documents/go/Kogo's Joseki Dictionary.sgf");
    sgf.load();
  }
}
