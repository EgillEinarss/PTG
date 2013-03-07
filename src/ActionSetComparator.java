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

import java.util.TreeSet;

public class ActionSetComparator implements java.util.Comparator<TreeSet<Action>>{
    public int compare(TreeSet<Action> A, TreeSet<Action> B){
        if(equals(A, B)) return 0; 
        if(A.size() != B.size()) return A.size() - B.size();
        return A.hashCode() - B.hashCode();
    }
    
    public boolean equals(TreeSet<Action> A, TreeSet<Action> B){
        if(A.size() != B.size()) return false;
        ActionComparator c = new ActionComparator();
        for(Action a : A){
            boolean r = false;
            for(Action b : B){
                r = r || c.equals(a,b);
            }
            if(!r) return false;
        }
        return true;
    }
}