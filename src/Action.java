/*
	PTG is a parsing table generator for common grammars used in academics.
    Copyright (C) 2013  Egill Búi Einarsson, ebe10@hi.is

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/	

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
		String dot = mathmode ? " \\cdot " : " $\\cdot$ ";
		String arr = mathmode ? " \\rightarrow " : " $\\rightarrow$ ";
        ret += PTG.escapeLatex(left, empty, mathmode) + arr;
        for(int i = 0; i < right.length; i++)
            ret += i == pos ? dot + PTG.escapeLatex(right[i], empty, mathmode) : " " + PTG.escapeLatex(right[i], empty, mathmode);
        if(pos == right.length) ret += dot;
        return ret;
    }
}