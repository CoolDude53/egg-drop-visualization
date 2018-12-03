package algs;

public class Drop
{
    private int floors;
    private int eggs;

    private int tempEggs;
    private int tempFloors;

    private DropData[][] dropDataArray;

    private Grid grid;
    private Controller controller;

    public static void main(String[] args)
    {
        new Drop(14, 2);
    }

    public Drop(int floors, int eggs)
    {
        this.floors = floors;
        this.eggs = eggs;
        this.tempFloors = 0;
        this.tempEggs = 1;

        initialize();
        drop();

        this.grid = new Grid(this);
        this.controller = new Controller(this);
    }

    // theoretical worst case
    public void drop()
    {
        for (int floor = 0; floor <= floors; floor++)
        {
            for (int egg = 0; egg <= eggs; egg++)
            {
                System.out.println("Floor: " + floor + " Eggs Left: " + egg);
                DropData data = dropDataArray[floor][egg];


                if (floor == 0 || egg == 0)
                {
                    data.setDrop(0);
                    data.getCases().add(new CaseData(0, "Drops Required: " + 0 + "<br>Edge case (zero floors/eggs)"));
                }

                else if (floor == 1)
                {
                    data.setDrop(1);
                    data.getCases().add(new CaseData(1, "Drops Required: " + 1 + "<br>Edge case (one floor)"));
                }

                else if (egg == 1)
                {
                    data.setDrop(floor);
                    data.getCases().add(new CaseData(floor, "Drops Required: " + floor + "<br>Edge case (one egg)"));
                }

                else
                {
                    data.setDrop(Integer.MAX_VALUE);

                    for (int x = 1; x <= floor; x++)
                    {
                        int egg_break = dropDataArray[x - 1][egg - 1].getDrop(); // if dropable breaks, we have one less and just try lower floors
                        int egg_not_break = dropDataArray[floor - x][egg].getDrop(); // if dropable doesn't break, we just try higher floors

                        int drops = Math.max(egg_break, egg_not_break) + 1; // worst case between two cases above + 1 for current drop
                        data.getCases().add(new CaseData(drops, x - 1, egg - 1, floor - x, egg, "Drops Required: " + drops + "<br>1 + max{dropDataArray[" + (x - 1) + "][" + (egg - 1) + "], dropDataArray[" + (floor - x) + "][" + egg + "]}<br><br>1 + max{" + egg_break + "," + egg_not_break + "}"));


                        // if drops is less than current, update
                        if (drops < data.getDrop())
                            data.setDrop(drops);
                    }
                }
            }
        }
    }

    public static int[][] gameDrop(int floors, int eggs)
    {
        int[][] grid = new int[floors][eggs];

        for (int floor = 0; floor < floors; floor++)
        {
            for (int egg = 0; egg < eggs; egg++)
            {
                if (floor == 0 || egg == 0)
                    grid[floor][egg] = 0;

                else if (floor == 1)
                    grid[floor][egg] = 1;

                else if (egg == 1)
                    grid[floor][egg] = floor;

                else
                {
                    grid[floor][egg] = Integer.MAX_VALUE;

                    for (int x = 1; x <= floor; x++)
                    {
                        int egg_break = grid[x - 1][egg - 1]; // if dropable breaks, we have one less and just try lower floors
                        int egg_not_break = grid[floor - x][egg]; // if dropable doesn't break, we just try higher floors

                        int drops = Math.max(egg_break, egg_not_break) + 1; // worst case between two cases above + 1 for current drop

                        // if drops is less than current, update
                        if (drops <  grid[floor][egg])
                            grid[floor][egg] = drops;
                    }
                }
            }
        }

        return grid;
    }

    private void initialize()
    {
        this.dropDataArray = new DropData[floors + 1][eggs + 1];

        for (int floor = 0; floor <= floors; floor++)
            for (int egg = 0; egg <= eggs; egg++)
                dropDataArray[floor][egg] = new DropData();
    }

    public Grid getGrid()
    {
        return grid;
    }

    public Controller getController()
    {
        return controller;
    }

    public int getFloors()
    {
        return floors;
    }

    public int getEggs()
    {
        return eggs;
    }

    public DropData[][] getDropDataArray()
    {
        return dropDataArray;
    }

    public int getTempEggs()
    {
        return tempEggs;
    }

    public int getTempFloors()
    {
        return tempFloors;
    }

    public void incrementTempEggs()
    {
        tempEggs++;
        if (tempEggs > eggs)
        {
            tempFloors++;
            tempEggs = 1;
        }
    }
}
