class ActionComparator implements java.util.Comparator<Action>{
    public int compare(Action a, Action b){
        if(!a.left.equals(b.left)) return a.left.compareTo(b.left);
        int c = (new StringArrayComparator()).compare(a.right, b.right);
        if(c == 0){
            if(a.pos - b.pos == 0) return a.follow().compareTo(b.follow());
            return a.pos - b.pos;
        }
        return c;
    }
    
    public boolean equals(Action a, Action b){
        if(a.pos != b.pos) return false;
        if(!a.left.equals(b.left)) return false;
        if(!a.follow().equals(b.follow())) return false;
        return (new StringArrayComparator()).equals(a.right, b.right);
    }
}

/*class ActionComparator implements java.util.Comparator<Action>{
    public int compare(Action a, Action b){
        if(!a.left.equals(b.left)) return a.left.compareTo(b.left);
        int c = (new StringArrayComparator()).compare(a.right, b.right);
        if(c == 0) return a.pos - b.pos;
        return c;
    }
    
    public boolean equals(Action a, Action b){
        if(a.pos != b.pos) return false;
        if(!a.left.equals(b.left)) return false;
        return (new StringArrayComparator()).equals(a.right, b.right);
    }
}*/