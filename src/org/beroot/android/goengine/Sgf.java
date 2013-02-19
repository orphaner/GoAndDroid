package org.beroot.android.goengine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Sgf
{
  private String _fileName;
  private GoGame _game;
  private String _content;
  private int _cursor;

  public Sgf()
  {
  }

  public Sgf(String fileName)
  {
    _fileName = fileName;
  }

  public void setFileName(String fileName)
  {
    _fileName = fileName;
  }

  public GoGame loadTsumego()
  {
    return load(true, true);
  }

  public GoGame loadGame()
  {
    return load(true, false);
  }

  public String getFileContent()
  {
    return _content;
  }

  private GoGame load(boolean keepFileInMemory, boolean readOnlyHeader)
  {
    try
    {
      readFile();
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
      return null;
    }
    _cursor = 0;
    _game = new GoGame();

    if (readOnlyHeader && !eof())
    {
      findOpenParentesis();
      _cursor++;
      _cursor++;
      GoNode node = createNewGoNode(null, true);
      if (!_game.hasMovesAndStones())
      {
        while (!eof() && _content.charAt(_cursor) != ';')
        {
          _cursor++;
        }
        _cursor++;
        createNewGoNode(node, false);
      }
    }
    else
    {
      while (!eof())
      {
        findOpenParentesis();
        buildTree(null);
      }
    }

    if (!keepFileInMemory)
    {
      _content = null;
    }
    return _game;
  }

  private void buildTree(GoNode node)
  {
    boolean first = true;
    while (!eof())
    {
      _cursor++;
      if (!eof())
      {
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
    StringBuffer prop = new StringBuffer();
    StringBuffer value = new StringBuffer();
    while (!eof())
    {
      c = _content.charAt(_cursor);
      switch (c)
      {
        // End Node
        case '(':
        case ';':
          _cursor--;
          return node;

        case ')':
          _cursor--;
          return node;

        case ']':
          _cursor++;
          c = _content.charAt(_cursor);
          if (c != '[')
          {
            node.addProp(prop.toString(), value.toString());
            prop = new StringBuffer();
            value = new StringBuffer();
            _cursor--;
          }
          else
          {
            value.append("|");
            _cursor--;
          }
          break;

        case '[':
          _cursor++;
          c = _content.charAt(_cursor);
          while (!eof() && (c != ']' || (c == ']' && lc == '\\')))
          {
            value.append(c);
            _cursor++;
            lc = c;
            c = _content.charAt(_cursor);
          }
          _cursor--;
          break;

        default:
          if (Character.isUpperCase(c))
          {
            prop.append(c);
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
    FileReader reader = new FileReader(new File(_fileName));
    try
    {
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
      reader.close();
    }
  }

  public static void main(String args[])
  {
    Sgf sgf = new Sgf("/home/nicolas/Documents/go/goproblemsSGF/696.sgf");
    sgf.setFileName("/home/nicolas/android/go/marks.sgf");
    GoGame go = sgf.loadGame();
    go.getStonesForBDD();
  }
}
