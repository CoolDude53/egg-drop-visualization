package mc.mechanics.game;

import algs.Drop;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import mc.GeneralUtils;
import mc.Setup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

public class Game
{
    private Setup plugin;
    private Player player;
    private int floors, eggs;
    private int[][] grid;
    private Question currentQuestion;
    private ArrayList<Hologram> holograms = new ArrayList<>();
    private Location loc1, loc2;
    private int score = 0;
    private Scoreboard scoreboard;

    public Game(Setup plugin, Player player)
    {
        this.plugin = plugin;
        this.player = player;
        this.floors = GeneralUtils.randInt(10, 20);
        this.eggs = GeneralUtils.randInt(2, 5);
    }

    public Game(Setup plugin, Player player, int floors, int eggs)
    {
        this.plugin = plugin;
        this.player = player;
        this.floors = floors;
        this.eggs = eggs;
    }

    public void start()
    {
        grid = Drop.gameDrop(floors + 1, eggs + 1);
        plugin.getMechanics().getGameMechanics().buildGrid(this, player.getLocation());
        generateQuestion();

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective partyObjective = scoreboard.registerNewObjective(player.getName(), "dummy");
        partyObjective.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Score");

        partyObjective.getScore("Correct").setScore(score);
        partyObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(scoreboard);
    }

    public void clean()
    {
        holograms.forEach(Hologram::delete);

        GeneralUtils.log(loc1.getBlockX() + " " + loc1.getBlockY() + " " + loc1.getBlockZ());
        GeneralUtils.log(loc2.getBlockX() + " " + loc2.getBlockY() + " " + loc2.getBlockZ());

        for (int x = loc1.getBlockX(); x < loc2.getBlockX(); x++)
        {
            for (int z = loc1.getBlockZ(); z < loc2.getBlockZ(); z++)
            {
                for (int y = loc1.getBlockY(); y < loc2.getBlockY(); y++)
                    new Location(loc1.getWorld(), x, y, z).getBlock().setType(Material.AIR);
            }
        }

        scoreboard.getObjective(DisplaySlot.SIDEBAR).unregister();
    }

    public void generateQuestion()
    {
        if (currentQuestion != null)
        {
            score++;
            Objective partyObjective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
            partyObjective.getScore("Correct").setScore(score);
        }

        int randFloor = GeneralUtils.randInt(0, floors);
        int randEgg = GeneralUtils.randInt(0, eggs);

        String question = "How many drops are required if you have " + randEgg + " egg(s) on the " + randFloor + " floor?";
        currentQuestion = new Question(question, grid[randFloor][randEgg]);

        player.sendMessage(ChatColor.YELLOW + question);
    }

    public int[][] getGrid()
    {
        return grid;
    }

    public Question getCurrentQuestion()
    {
        return currentQuestion;
    }

    public ArrayList<Hologram> getHolograms()
    {
        return holograms;
    }

    public Location getLoc1()
    {
        return loc1;
    }

    public Location getLoc2()
    {
        return loc2;
    }

    public void setLoc1(Location loc1)
    {
        this.loc1 = loc1;
    }

    public void setLoc2(Location loc2)
    {
        this.loc2 = loc2;
    }

    public int getFloors()
    {
        return floors;
    }

    public int getEggs()
    {
        return eggs;
    }
}
