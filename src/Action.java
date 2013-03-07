class Action{
    public String left;
    public String[] right;
    public int pos;
    public boolean html;
    public String empty;
    
    public Action(String left, String[] right, int pos, boolean html, String empty){
        this.left = left;
        this.right = right;
        this.pos = pos;
        this.html = html;
        this.empty = empty;
    }
    
    public Action(String left, String right, int pos, boolean html, String empty){
        this.left = left;
        this.right = new String[1];
        this.right[0] = right;
        this.pos = pos;
        this.html = html;
        this.empty = empty;
    }
    
    public Action(String left, String[] right, int pos, String follow, boolean html, String empty){
        this.left = left;
        this.right = right;
        this.pos = pos;
        this.html = html;
        this.empty = empty;
    }
    
    public Action(String left, String right, int pos, String follow, boolean html, String empty){
        this.left = left;
        this.right = new String[1];
        this.right[0] = right;
        this.pos = pos;
        this.html = html;
        this.empty = empty;
    }
    
    public String follow(){
        return "";
    }
    
    public String[] beta(String b){
        String[] a = new String[right.length - pos];
        int i = 0;
        int j = pos + 1;
        while(j < right.length) a[i++] = right[j++];
        a[i] = b;
        return a;
    }
    
    public String next(){
        if(pos < right.length)
            return right[pos];
        return "";
    }
    
    public Action next(String[] right, String follow){
        return new Action(next(), right, 0, follow, html, empty);
    }
    
    public Action advance(){
        return new Action(left, right, pos+1, html, empty);
    }
    
    public Action advance(String X){
        if(X.equals(next())) return advance();
        return null;
    }
    
    public boolean completed(){
        if(right.length == 1 && right[0].equals(empty)) return true;
        return pos == right.length;
    }
    
    public String toString(){
        if(html) return toHtml();
        return toTikz(true);
    }
    
    public String toHtml(){
        String ret = "";
        ret += PTG.escapeHtml(left, empty) + " &rarr;";
        for(int i = 0; i < right.length; i++)
            ret += i == pos ? " &middot; " + PTG.escapeHtml(right[i],empty) : " " + PTG.escapeHtml(right[i],empty);
        if(pos == right.length) ret += " &middot;";
        return ret;
    }
    
    public String toTikz(boolean mathmode){
        String ret = "";
        ret += PTG.escapeLatex(left, empty, mathmode) + " \\rightarrow ";
        for(int i = 0; i < right.length; i++)
            ret += i == pos ? " \\cdot " + PTG.escapeLatex(right[i], empty, mathmode) : " " + PTG.escapeLatex(right[i], empty, mathmode);
        if(pos == right.length) ret += " \\cdot ";
        return ret;
    }
}