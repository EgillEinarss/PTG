public class PTG{
    public static void main(String[] args){
        PTGobject po = new PTGobject(args);
        po.parse();
    }
    
    public static String escapeHtml(String s, String e){
        if(s.equals(e)) return "&#1013";
        return s.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
    
    public static String[] escapeHtml(String S[], String e){
        String[] s = new String[S.length];
        for(int i = 0; i < S.length; i++)
            s[i] = escapeHtml(S[i],e);
        return s;
    }
    
    public static java.util.TreeSet<String> escapeHtml(java.util.TreeSet<String> T, String e){
        java.util.TreeSet<String> t = new java.util.TreeSet<String>();
        for(String s : T)
            t.add(escapeHtml(s,e));
        return t;
    }
    
    public static String escapeLatex(String s, String e, boolean mathmode){
        if(s.equals(e)) return "$\\epsilon$";
        int i = 0;
        String sub = "";
        while(i < s.length()){
            char c = s.charAt(i);
            if(c == '\\') sub = mathmode ? "\\backslash " : "$\\backslash$";
            else if(c == '$') sub = "\\$ ";
            else if(c == '#') sub = "\\# ";
            else if(c == '_') sub = "\\_ ";
            else if(c == '{') sub = "\\{ ";
            else if(c == '}') sub = "\\} ";
            else if(c == '%') sub = "\\% ";
            else if(c == '&') sub = "\\& ";
            else if(c == '~') sub = "\\verb#~#";
            else if(c == '^') sub = mathmode ? "\\wedge " : "$\\wedge$";
            else sub = "" + c;
            String right = i+1 < s.length() ? s.substring(i+1, s.length()) : "";
            s = s.substring(0, i) + sub + right;
            i += sub.length();
        }
        return s;
    }
    
    public static String[] escapeLatex(String S[], String e, boolean mathmode){
        String[] s = new String[S.length];
        for(int i = 0; i < S.length; i++)
            s[i] = escapeLatex(S[i],e, mathmode);
        return s;
    }
    
    public static java.util.TreeSet<String> escapeLatex(java.util.TreeSet<String> T, String e, boolean mathmode){
        java.util.TreeSet<String> t = new java.util.TreeSet<String>();
        for(String s : T)
            t.add(escapeLatex(s,e,mathmode));
        return t;
    }
    
    public static void help(){
        System.out.println("Error: fileName not specified.\n");
        help(1);
    }
    
    public static void help(String e){
        System.out.println("Error: Could not interpret " + e + ", fileName already defined.\n");
        help(2);
    }
    
    public static void help(int x){
        //TODO: Needs updating
        System.out.println("Print a helpful message. If this was not requested then the input was incorrect.");
        System.out.println("Usage: java AutoParser fileName[ empty[ end[ start]]]");
        System.out.println("fileName: An input file containing a viable grammar.");
        System.out.println("empty: The empty string token to be used.");
        System.out.println("\tDefault is <e>.");
        System.out.println("end: The prefered end of file token.");
        System.out.println("\tDefault is $.");
        System.out.println("start: The start variable of the grammar in fileName.");
        System.out.println("\tDefault is the first variable in fileName.");
        System.exit(x);
    }
}

class PTGobject{
    String[] args;
    int ind;
    java.util.ArrayList<PTGoptions> opts;
    Grammar g;
    String start, empty, end;
    /*
        Integer key:
        0 - First
        1 - Follow
        2 - LL(1)
        3 - SLR(1)
        4 - LALR(1)
        
        10 - LR(0) state machine
        11 - LR(1) state machine
    */
    
    public PTGobject(String[] args){
        this.args = args;
        ind = args[0].indexOf('.');
        opts = new java.util.ArrayList<PTGoptions>();
        g = null;
    }
    
    public void parse(){
        if(args.length < 2)
                PTG.help(0);
        start = ""; empty = "<e>"; end = "$";
        for(int i = 1; i < args.length; i++){
            if(args[i].startsWith("-")){
                switch(Options.valueOf(args[i].substring(1).toUpperCase())){
                    case START: start = args[++i]; break;
                    case END:   end = args[++i]; break;
                    case EMPTY: empty = args[++i]; break;
                    default: break;
                }
            }
        }
        g = new Grammar(args[0], "obs", start, empty, end);
        for(int i = 1; i < args.length; i++){
            if(args[i].startsWith("-")){
                switch(Options.valueOf(args[i].substring(1).toUpperCase())){
                    case FIRST:
                        opts.add(ff(i, "FIRST"));
                        break;
                    case FOLLOW:
                        opts.add(ff(i, "FOLLOW"));
                        break;
                    case LL1:
                        opts.add(ll(i));
                        break;
                    case LR0: //state machine
                        opts.add(lrM(i, "LR0M"));
                        break;
                    case SLR1:
                        opts.add(lr(i, "SLR1"));
                        break;
                    case LR1M: //state machine
                        opts.add(lrM(i, "LR1M"));
                        break;
                    case LR1:
                        opts.add(lr(i, "LR1"));
                        break;
                    case LALR1M: //state machine.þæ
                        opts.add(lrM(i, "LALR1M"));
                        break;
                    case LALR1:
                        opts.add(lr(i, "LALR1"));
                        break;
                    case ALL:
                        opts.add(ff(i, "FIRST"));
                        opts.add(ff(i, "FOLLOW"));
                        opts.add(ll(i));
                        opts.add(lrM(i, "LR0M"));
                        opts.add(lr(i, "SLR1"));
                        opts.add(lrM(i, "LR1M"));
                        opts.add(lr(i, "LR1"));
                        opts.add(lrM(i, "LALR1M"));
                        opts.add(lr(i, "LALR1"));
                        break;
                    default: break;
                }
            }
        }
        for(PTGoptions p : opts)
            g.find(p);
    }
    
    private enum Options{
        START,
        END,
        EMPTY,
        FIRST,
        FOLLOW,
        LL1,
        LR0,
        SLR1,
        LR1M,
        LALR1M,
        LR1,
        LALR1,
        ALL
    }
    
    private String[] params(int i, String suffix){
        String[] a = new String[2];
        a[0] = "";
        a[1] = "";
        if(i+1 < args.length && !args[i+1].startsWith("-")){
            if(args[i+1].toUpperCase().equals("HTML") || args[i+1].toUpperCase().equals("LATEX"))
                a[1] = args[i+1].toUpperCase();
            else a[0] = args[i+1];
            if(i+2 < args.length && !args[i+2].startsWith("-")){
                if(args[i+2].toUpperCase().equals("HTML") || args[i+2].toUpperCase().equals("LATEX"))
                    a[1] = args[i+2].toUpperCase();
                else a[0] = args[i+2];
            }
        }
        if(a[0].equals("")) a[0] = ind == -1 ? args[0] : args[0].substring(0, ind);
        if(a[1].equals("")) a[1] = "BOTH";
        return a;
    }
    
    private String[] paramsM(int i, String suffix){
        String[] a = new String[3];
        a[0] = "";
        a[1] = "";
        a[2] = "";
        while(++i < args.length && !args[i].startsWith("-")){
            if(args[i].toUpperCase().equals("GZ") || args[i].toUpperCase().equals("TIKZ"))
                a[1] = args[i].toUpperCase();
            else if(args[i].toUpperCase().equals("TINY") || args[i].toUpperCase().equals("SMALL") || args[i].toUpperCase().equals("LARGE"))
                a[2] = args[i].toUpperCase();
            else a[0] = args[i].toUpperCase();
        }
        /*if(i+1 < args.length && !args[i+1].startsWith("-")){
            if(args[i+1].toUpperCase().equals("GZ") || args[i+1].toUpperCase().equals("TIKZ"))
                a[1] = args[i+1].toUpperCase();
            else a[0] = args[i+1];
            if(i+2 < args.length && !args[i+2].startsWith("-")){
                if(args[i+2].toUpperCase().equals("GZ") || args[i+2].toUpperCase().equals("TIKZ"))
                    a[1] = args[i+2].toUpperCase();
                else a[0] = args[i+2];
            }
        }*/
        if(a[0].equals("")) a[0] = ind == -1 ? args[0] : args[0].substring(0, ind);
        if(a[1].equals("")) a[1] = "BOTH";
        return a;
    }
    
    private PTGoptions ff(int i, String f){
        String[] command = params(i, f);
        PTGoptions p = new PTGoptions(f, command, "X", -1);
        p.addColumn(f + "(X)", f + "(X)", f + "(X)");
        String[] rows = g.V.toArray(new String[1]);
        p.addRows(rows, PTG.escapeHtml(rows, empty), PTG.escapeLatex(rows, empty, false));
        return p;
    }
    
    private PTGoptions ll(int i){
        String[] command = params(i, "LL1");
        PTGoptions p = new PTGoptions("LL1", command, "", -1);
        String[] columns = g.T.toArray(new String[1]);
        p.addColumns(columns, PTG.escapeHtml(columns, empty), PTG.escapeLatex(columns, empty, false));
        String[] rows = g.V.toArray(new String[1]);
        p.addRows(rows, PTG.escapeHtml(rows, empty), PTG.escapeLatex(rows, empty, false));
        p.makeLastColumn(g.end);
        return p;
    }
    
    private PTGoptions lr(int i, String f){
        String[] command = params(i, f);
        PTGoptions p = new PTGoptions(f, command, "State", g.T.size());
        String[] columns = g.T.toArray(new String[1]);
        p.addColumns(columns, PTG.escapeHtml(columns, empty), PTG.escapeLatex(columns, empty, false));
        p.makeLastColumn(g.end);
        columns = g.V.toArray(new String[1]);
        p.addColumns(columns, PTG.escapeHtml(columns, empty), PTG.escapeLatex(columns, empty, false));
        return p;
    }
    
    private PTGoptions lrM(int i, String f){
        String[] command = paramsM(i, f);
        String size = command[2];
        String[] com = new String[2];
        com[0] = command[0];
        com[1] = command[1];
        PTGoptions p = new PTGoptions(f, com, "", g.T.size());
        if(size.equals("SMALL")) p.stateSizeSmall();
        else if(size.equals("LARGE")) p.stateSizeLarge();
        String[] columns = g.T.toArray(new String[1]);
        p.addColumns(columns, PTG.escapeHtml(columns, empty), PTG.escapeLatex(columns, empty, false));
        p.makeLastColumn(g.end);
        columns = g.V.toArray(new String[1]);
        p.addColumns(columns, PTG.escapeHtml(columns, empty), PTG.escapeLatex(columns, empty, false));
        return p;
    }
}


/*public class PTG{

    /*
        Integer key:
        0 - First
        1 - Follow
        2 - LL(1)
        3 - SLR(1)
        4 - LALR(1)
        
        10 - LR(0) state machine
        11 - LR(1) state machine
    */
    
  /*  public static void main(String[] args){
        if(args.length == 0 || 
            args[0].toUpperCase().equals("HELP") ||
            args[0].toUpperCase().equals("-HELP") ||
            args[0].toUpperCase().equals("--HELP") )
                help(0);
        String file = "", start = "", empty = "<e>", end = "$";//, out = "", dot = "", type = "";
        /*boolean first = false, firstH = false, follow = false, followH = false,
            LL1 = false, LL1H = false, LR0 = false, SLR1 = false, SLR1H = false,
            LR1 = false, LALR1 = false, LALR1H = false;*/
    /*    for(int i = 0; i < args.length; i++){
            if(args[i].startsWith("-")){
                switch(Options.valueOf(args[i].substring(1).toUpperCase())){
                    case START: start = args[++i]; break;
                    case END: end = args[++i]; break;
                    case EMPTY: empty = args[++i]; break;
                    case O: out = args[++i]; break;
                    case FIRST:
                        first = true;
                        if(args[i+1].toUpperCase().equals("HTML")){
                            firstH = true;
                            i++;
                        }
                        break;
                    case FOLLOW:
                        follow = true;
                        if(args[i+1].toUpperCase().equals("HTML")){
                            followH = true;
                            i++;
                        }
                        break;
                    case LL1:
                        LL1 = true;
                        if(args[i+1].toUpperCase().equals("HTML")){
                            LL1H = true;
                            i++;
                        }
                        break;
                    case LR0: LR0 = true;
                    case SLR1:
                        SLR1 = true;
                        if(args[i+1].toUpperCase().equals("HTML")){
                            SLR1H = true;
                            i++;
                        }
                        break;
                    case LR1: LR1 = true;
                    case LALR1:
                        LALR1 = true;
                        if(args[i+1].toUpperCase().equals("HTML")){
                            LALR1H = true;
                            i++;
                        }
                        break;
                    case ALL:
                        first = follow = LL1 = LR0 = SLR1 = LR1 = LALR1 = true; break;
                    case HTML:
                        firstH = followH = LL1H = SLR1H = LALR1H = true; break;
                    case DOT: dot = args[++i]; break;
                    case FILETYPE: type = args[++i]; break;
                    default:
                        System.out.println("Error: " + args[i] +" is an unknown commmand.\n");
                        help(3);
                }
            } else {
                if(file.equals("")) file = args[i];
                else help(args[i]);
            }
        }
        if(file.equals("")) help();
        if(out.equals("")) out = file;
        Grammar g = new Grammar(args[0], out, start, empty, end);
        if(first) g.findFirst(firstH);
        if(follow) g.findFollow(followH);
        if(LL1) g.findLL1(LL1H);
        if(LR0) g.findLR0();
        if(SLR1) g.findSLR1(SLR1H);
        if(LR1) g.findLR1();
        if(LALR1) g.findLALR1(LALR1H);
        if(!dot.equals("")){
            /*if(type.equals("")) type = "png";
            java.lang.Runtime rt = java.lang.Runtime.getRuntime();
            java.lang.Process p1, p2;
            if(LR0) p1 = rt.exec(dot + " -T" + type + " " + out + "LR0.gz -o " + out + "LR0." + type);
            if(LR1) p2 = rt.exec(dot + " -T" + type + " " + out + "LR1.gz -o " + out + "LR1." + type);*/
      /*  }
    }
    
    private enum Options{
        START,
        END,
        EMPTY,
        O,
        FIRST,
        FOLLOW,
        LL1,
        LR0,
        SLR1,
        LR1,
        LALR1,
        ALL,
        HTML,
        DOT,
        FILETYPE
    }
    
    private static void help(){
        System.out.println("Error: fileName not specified.\n");
        help(1);
    }
    
    private static void help(String e){
        System.out.println("Error: Could not interpret " + e + ", fileName already defined.\n");
        help(2);
    }
    
    private static void help(int x){
        //TODO: Needs updating
        System.out.println("Print a helpful message. If this was not requested then the input was incorrect.");
        System.out.println("Usage: java AutoParser fileName[ empty[ end[ start]]]");
        System.out.println("fileName: An input file containing a viable grammar.");
        System.out.println("empty: The empty string token to be used.");
        System.out.println("\tDefault is <e>.");
        System.out.println("end: The prefered end of file token.");
        System.out.println("\tDefault is $.");
        System.out.println("start: The start variable of the grammar in fileName.");
        System.out.println("\tDefault is the first variable in fileName.");
        System.exit(x);
    }
    
}*/
