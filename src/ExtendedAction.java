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

public class ExtendedAction extends Action{
    public String follow;
    
    public ExtendedAction(String left, String[] right, int pos, String follow, boolean html, String empty){
        super(left, right, pos, html, empty);
        this.follow = follow;
    }
    
    public ExtendedAction(String left, String right, int pos, String follow, boolean html, String empty){
        super(left, right, pos, html, empty);
        this.follow = follow;
    }
    
    public ExtendedAction(Action a, String follow){
        super(a.left, a.right, a.pos, a.html, a.empty);
        this.follow = follow;
    }
    
    public Action action(){
        return new Action(left, right, pos, html, empty);
    }
    
    public ExtendedAction next(String[] right, String follow){
        return new ExtendedAction(next(), right, 0, follow, html, empty);
    }
    
    public String lookahead(){
        return follow;
    }
    
    public ExtendedAction advance(){
        return new ExtendedAction(left, right, pos+1, follow, html, empty);
    }
    
    public String toHtml(){
        return super.toHtml() + ", " + PTG.escapeHtml(follow, empty);
    }
    
    public String toTikz(boolean mathmode){
        return super.toTikz(mathmode) + ", " + PTG.escapeLatex(follow, empty, mathmode);
    }
}