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