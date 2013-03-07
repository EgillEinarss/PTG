import java.util.TreeSet;

public class ExtendedActionSetComparator implements java.util.Comparator<TreeSet<ExtendedAction>>{
    
    public int compare(TreeSet<ExtendedAction> A, TreeSet<ExtendedAction> B){
        if(equals(A, B)) return 0; 
        if(A.size() != B.size()) return A.size() - B.size();
        return A.hashCode() - B.hashCode();
    }
    
    public boolean equals(TreeSet<ExtendedAction> A, TreeSet<ExtendedAction> B){
        if(A.size() != B.size()) return false;
        ExtendedActionComparator c = new ExtendedActionComparator();
        for(ExtendedAction a : A){
            boolean r = false;
            for(ExtendedAction b : B){
                r = r || c.equals(a,b);
            }
            if(!r) return false;
        }
        return true;
    }
}