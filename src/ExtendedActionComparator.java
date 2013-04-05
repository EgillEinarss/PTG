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

class ExtendedActionComparator implements java.util.Comparator<ExtendedAction>{
    public int compare(ExtendedAction a, ExtendedAction b){
        if(!a.left.equals(b.left)) return a.left.compareTo(b.left);
        int c = (new StringArrayComparator()).compare(a.right, b.right);
        if(c == 0){
            if(a.pos != b.pos) return a.lookahead().compareTo(b.lookahead());
            return a.pos - b.pos;
        }
        return c;
    }
    
    public boolean equals(ExtendedAction a, ExtendedAction b){
        if(a.pos != b.pos) return false;
        if(!a.left.equals(b.left)) return false;
        if(!a.lookahead().equals(b.lookahead())) return false;
        return (new StringArrayComparator()).equals(a.right, b.right);
    }
}