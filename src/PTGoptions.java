import java.util.*;

public class PTGoptions{
    public boolean latex;
    public boolean html;
    public boolean LR;
    public int stateSize;
    public String outname;
    public String title;
    public String label;
    public int vbreak;
    public ArrayList<String[]> rows;
    public ArrayList<String[]> columns;
    
    public PTGoptions(String title, String[] command, String label, int vbreak){
        this.outname = command[0];
        this.latex = command[1].equals("HTML") || command[1].equals("GZ") ? false : true;
        this.html = command[1].equals("LATEX") || command[1].equals("TIKZ") ? false : true;
        this.title = title;
        this.label = label;
        this.vbreak = vbreak;
        rows = new ArrayList<String[]>();
        columns = new ArrayList<String[]>();
        stateSize = 0;
    }
    
    public void addRow(String s, String h, String l){
        String[] a = new String[3];
        a[0] = s; a[1] = h; a[2] = l;
        if(!rows.contains(a)) rows.add(a);
    }
    
    public void addRows(String[] s, String[] h, String[] l){
        for(int i = 0; i < s.length; i++)
            addRow(s[i], h[i], l[i]);
    }
    
    public void makeLastRow(String s){
        String[] a;
        for(int i = 0; i < rows.size(); i++)
            if((rows.get(i))[0].equals(s)){
                a = rows.get(i);
                rows.remove(i);
                rows.add(a);
                break;
            }
    }
    
    public void topDown(){
        LR = false;
    }
    
    public void leftRight(){
        LR = true;
    }
    
    public void stateSizeTiny(){
        stateSize = 0;
    }
    
    public void stateSizeSmall(){
        stateSize = 1;
    }
    
    public void stateSizeLarge(){
        stateSize = 2;
    }
    
    public void addColumn(String s, String h, String l){
        String[] a = new String[3];
        a[0] = s; a[1] = h; a[2] = l;
        if(!columns.contains(a)) columns.add(a);
    }
    
    public void addColumns(String[] s, String[] h, String[] l){
        for(int i = 0; i < s.length; i++)
            addColumn(s[i], h[i], l[i]);
    }
    
    public void makeLastColumn(String s){
        String[] a;
        for(int i = 0; i < columns.size(); i++)
            if((columns.get(i))[0].equals(s)){
                a = columns.get(i);
                columns.remove(i);
                columns.add(a);
                break;
            }
    }
    
    public void addStates(int I){
        for(int i = 0; i < I; i++)
            addRow("" + i, "I<sub>" + i + "</sub>", "I$_{" + i + "}$");
    }
}