package algs;

import java.util.ArrayList;

public class DropData
{
    private int drop;
    private int currentCase = 0;
    private ArrayList<CaseData> cases = new ArrayList<>();

    public int getDrop()
    {
        return drop;
    }

    public void setDrop(int drop)
    {
        this.drop = drop;
    }

    public ArrayList<CaseData> getCases()
    {
        return cases;
    }

    public CaseData getCurrentCase()
    {
        if (currentCase >= cases.size())
            return null;
        else
            return cases.get(currentCase++);
    }
}
