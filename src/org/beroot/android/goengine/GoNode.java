package org.beroot.android.goengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoNode
{
  GoNode _next;
  List<GoNode> _variations = new ArrayList<GoNode>();
  Map<String, String> _props = new HashMap<String, String>();
}
