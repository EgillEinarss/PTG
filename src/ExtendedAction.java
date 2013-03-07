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
    
    public String follow(){
        return follow;
    }
    
    public ExtendedAction advance(){
        return new ExtendedAction(left, right, pos+1, follow, html, empty);
    }
    
    public String toHtml(){
        String ret = "";
        ret += PTG.escapeHtml(left, empty) + " &rarr;";
        for(int i = 0; i < right.length; i++)
            ret += i == pos ? " &middot; " + PTG.escapeHtml(right[i], empty) : " " + PTG.escapeHtml(right[i], empty);
        if(pos == right.length) ret += " &middot;";
        ret += ", " + PTG.escapeHtml(follow, empty);
        return ret;
    }
    
    public String toTikz(boolean mathmode){
        String ret = "";
        ret += PTG.escapeLatex(left, empty, mathmode) + " \\rightarrow ";
        for(int i = 0; i < right.length; i++)
            ret += i == pos ? " \\cdot " + PTG.escapeLatex(right[i], empty, mathmode) : " " + PTG.escapeLatex(right[i], empty, mathmode);
        if(pos == right.length) ret += " \\cdot ";
        ret += ", " + PTG.escapeLatex(follow, empty, mathmode);
        return ret;
    }
}