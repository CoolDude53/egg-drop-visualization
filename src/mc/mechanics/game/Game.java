package mc.mechanics.game;

import algs.Drop;
import mc.GeneralUtils;
import mc.Setup;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Game
{
    private Setup plugin;
    private Player player;
    private int floors, eggs;
    private int[][] grid;
    private Question currentQuestion;

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
        grid = Drop.gameDrop(floors + 2, eggs + 2);
        plugin.getMechanics().getGameMechanics().buildGrid(grid, player.getLocation());
        generateQuestion();
    }

    public void generateQuestion()
    {
        int randFloor = GeneralUtils.randInt(0, floors);
        int randEgg = GeneralUtils.randInt(0, eggs);

        String question = "How many drops are required if you have " + randEgg + " egg(s) on the " + randFloor + " floor?";
        currentQuestion = new Question(question, grid[randFloor][randEgg]);

        player.sendMessage(ChatColor.YELLOW + question);
    }

    public Question getCurrentQuestion()
    {
        return currentQuestion;
    }
}
