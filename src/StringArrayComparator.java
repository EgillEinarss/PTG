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

public class StringArrayComparator implements java.util.Comparator<String[]>{
    public int compare(String[] a, String[] b){
        int m = a.length < b.length ? a.length : b.length;
        for(int i = 0; i < m; i++)
            if(!a[i].equals(b[i]))
                return a[i].compareTo(b[i]);
        if(a.length == b.length) return 0;
        if(a.length < b.length) return -1;
        return 1;
    }
    
    public boolean equals(String[] a, String[] b){
        if(a.length != b.length) return false;
        for(int i = 0; i < a.length; i++)
            if(!a[i].equals(b[i]))
                return false;
        return true;
    }
}
