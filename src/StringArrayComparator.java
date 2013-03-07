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
