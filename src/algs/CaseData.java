package algs;

public class CaseData
{
    private int drops;
    private int max10 = -1;
    private int max11 = -1;
    private int max20 = -1;
    private int max21 = -1;

    private String text;

    public CaseData(int drops, String text)
    {
        this.drops = drops;
        this.text = text;
    }

    public CaseData(int drops, int max10, int max11, int max20, int max21, String text)
    {
        this.drops = drops;
        this.max10 = max10;
        this.max11 = max11;
        this.max20 = max20;
        this.max21 = max21;
        this.text = text;
    }

    public int getDrops()
    {
        return drops;
    }

    public String getText()
    {
        return text;
    }

    public int getMax10()
    {
        return max10;
    }

    public int getMax11()
    {
        return max11;
    }

    public int getMax20()
    {
        return max20;
    }

    public int getMax21()
    {
        return max21;
    }
}
