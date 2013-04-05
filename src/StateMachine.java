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

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class StateMachine<S extends java.util.Collection, T>{
    List<S> Q; //states
    List<HashMap<T, Integer>> d; //transistions
    java.util.Comparator<S> comparator;
    int stateSize;
    
    public StateMachine(S I, java.util.Comparator<S> comparator, int stateSize){
        Q = new ArrayList<S>();
        d = new ArrayList<HashMap<T, Integer>>();
        add(I);
        this.comparator = comparator;
        this.stateSize = stateSize;
    }
    
    private void add(S I){
        Q.add(I);
        d.add(new HashMap<T, Integer>());
    }
    
    public void add(int I, T X, S g){
        int IX = repeatOf(g);
        if(IX == -1){
            IX = Q.size();
            add(g);
        }
        d.get(I).put(X, IX);
		if(IX != -1)
			Q.get(IX).addAll(g);
    }
    
    private int repeatOf(S g){
        for(int i = 0; i < Q.size(); i++)
            if(comparator.compare(get(i), g) == 0) return i;
        return -1;
    }
	
	public int repeatOf(int i, java.util.Comparator<S> comp){
		S g = get(i);
		for(int j = i+1; j < size(); j++)
		    if(comp.compare(get(j), g) == 0) return j;
        return -1;
	}
    
    public S get(int i){
        return Q.get(i);
    }
    
    public int get(int s, T a){
        if(s >= Q.size() || !d.get(s).containsKey(a)) return -1;
        return d.get(s).get(a);
    }
    
    public int size(){
        return Q.size();
    }
    
    public void print(String label){
        System.out.println(label + " statemachine:");
        for(int i = 0; i < Q.size(); i++){
            System.out.println("State " + i + " contains elements:");
            for(Object a : (java.util.Set)(Q.get(i)))
                System.out.print("  " + a + ";");
            System.out.println();
            for(Object key : d.get(i).keySet())
                System.out.println("State " + i + " transitions to state " + d.get(i).get(key) + " on reading " + key);
            System.out.println("\n");
        }
    }
	
	public void reduce(java.util.Comparator<S> comp){
		int IX;
		for(int i = 0; i < size(); i++)
			while((IX = repeatOf(i, comp)) != -1)
				reduce(i,IX);
	}
	
	public void reduce(int i, int j){
		get(i).addAll(get(j));
		d.get(j).putAll(d.get(i));
		d.get(i).putAll(d.get(j));
		for(HashMap<T, Integer> map: d){
			for(T key : map.keySet()){
				int k = map.get(key).intValue();
				if(k == j)
					map.put(key, new Integer(i));
				else if(k > j)
					map.put(key, new Integer(k-1));
			}
		}
		Q.remove(j);
		d.remove(j);
	}
    
    public void makeGz(String filename, java.util.TreeSet<String> V, String empty, boolean LR){
        try{
            java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(filename));
            bw.write("digraph Automaton {");
            bw.newLine();
            if(LR) bw.write("  rankdir=LR;"); else bw.write("  rankdir=TD;");
            bw.newLine();
            bw.write("  start [shape=plaintext, width=0, label=\"\"];");
            bw.newLine();
            bw.write("  start -> 0;");
            bw.newLine();
            for(int i = 0; i < size(); i++){
                String s = "";
                for(Object o: (java.util.Set)(Q.get(i))){
                    if(!s.equals("")) s += "<br/>";
                    s += "[" + o.toString().replace(PTG.escapeHtml(empty,empty), "") + "]";
                }
                if(stateSize > 0)
                    bw.write(i + " [label=<I<sub>" + i + "</sub>:<br/>" + s + ">]");
                else
                    bw.write(i + " [label=<I<sub>" + i + "</sub>>]");
                bw.newLine();
            }
            for(int i = 0; i < size(); i++){
                for(Object key : d.get(i).keySet()){
                    bw.write("  " + i + " -> " + d.get(i).get(key) + " [style=");
                    if(V.contains(key)) bw.write("dashed");
                    else bw.write("solid");
                    bw.write(" label=\"" + PTG.escapeHtml(key.toString(),empty) + "\"]");
                    bw.newLine();
                }
            }
            bw.write("}");
            bw.newLine();
            bw.close();
        } catch(java.io.IOException e){
            System.out.println(filename + " appears to be locked.\n");
            System.exit(9);
        }
    }
    
    public void makeTikz(String filename, java.util.TreeSet<String> V, String empty, boolean LR){
        try{
            int[] parent = new int[size()];
            int[] lSibling = new int[size()];
            int[] height = new int[size()];
            int[] count = new int[size()];
            int[] x = new int[size()];
            int[] y = new int[size()];
            for(int i = 0; i < size(); i++){
                parent[i] = -1;
                lSibling[i] = -1;
                count[i] = 0;
            }
            for(int i = 0; i < size(); i++){
                for(Integer J : d.get(i).values()){
                    int j = J.intValue();
                    if(i > parent[j] && i < j) parent[j] = i;
                }
            }
            height[0] = 0;
            for(int i = 1; i < size(); i++){
                height[i] = height[parent[i]] + 1;
                count[height[i]]++;
                for(int j = i-1; j > 0; j--)
                    if(height[j] == height[i]){
                        lSibling[i] = j;
                        break;
                    }
            }
            java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(filename));
            //bw.write("\\begin{tikzpicture}[shorten >=1pt,node distance=4cm,on grid,initial above,initial text=,initial distance=1cm]");
            bw.write("\\begin{tikzpicture}[>=stealth',shorten >=1pt,auto,node distance=2cm,every text node part/.style={align=center}]");
            bw.newLine();
            bw.write("  \\node[initial,state]  (i0)  {$I_0$");
            if(stateSize > 0)
                for(Object o: (java.util.Set)(Q.get(0))){
                    bw.write(" \\\\ $" + o.toString() + "$");
                }
            bw.write("};");
            bw.newLine();
            for(int i = 1; i < size(); i++){
                String s = "";
                if(stateSize > 0)
                    for(Object o: (java.util.Set)(Q.get(i))){
                        s += " \\\\ $" + o.toString().replace(PTG.escapeLatex(empty,empty,true), "") +"$";
                    }
                //s = "";
                bw.write("  \\node[state]  (i" + i + ")  ");
                //if(parent[i] != -1 && lSibling[i] != -1) bw.write("[right of=i" + parent[i] + ",below of=i" + lSibling[i] + "]  ");
                //if(parent[i] != -1 && lSibling[i] != -1) bw.write("[below of=i" + lSibling[i] + "]  ");
                if(parent[i] != -1 && lSibling[i] != -1) bw.write("[right of=i" + lSibling[i] + "]  ");
                else if(parent[i] != -1) bw.write("[below of=i" + parent[i] + "]  ");
                else if(lSibling[i] != -1) bw.write("[right of=i" + lSibling[i] + "]  ");
                bw.write("{$I_{" + i + "}$" + s + "};");
                bw.newLine();
            }
            for(int i = 0; i < size(); i++){
                for(Object key : d.get(i).keySet()){
                    int j = d.get(i).get(key);
                    bw.write("  \\path[->");
                    if(V.contains(key)) bw.write(",dashed");
                    bw.write("] (i" + i + ") edge ");
                    if(i == j) bw.write("[loop right] ");
                    else if(i < j) bw.write("[right] ");
                    else if(i > j) bw.write("[bend left] ");
                    /*if(i == j) bw.write("[loop right] ");
                    else if(i < j) bw.write("[bend below] ");
                    else if(i > j) bw.write("[bend left] ");*/
                    bw.write("node {" + PTG.escapeLatex(key.toString(),empty,false) + "} (i" + j + ");");
                    bw.newLine();
                }
            }
            bw.write("\\end{tikzpicture}");
            bw.newLine();
            bw.close();
        } catch(java.io.IOException e){
            System.out.println(filename + " appears to be locked.\n");
            System.exit(9);
        }
    }
}