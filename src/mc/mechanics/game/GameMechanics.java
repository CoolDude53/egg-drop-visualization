package mc.mechanics.game;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import mc.Setup;
import mc.mechanics.Mechanic;
import mc.mechanics.game.commands.StartGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class GameMechanics extends Mechanic
{
    private HashMap<UUID, Game> gameMap = new HashMap<>();

    public GameMechanics(Setup plugin)
    {
        super(plugin);
    }

    @Override
    public void register()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        plugin.getCommand("startgame").setExecutor(new StartGame(plugin));

        //plugin.getCommand("toggle").setTabCompleter(new ToggleTabCompleter());
    }

    public void startGame(Player player)
    {
        Game game = new Game(plugin, player);
        gameMap.put(player.getUniqueId(), game);

        game.start();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();

        if (gameMap.containsKey(player.getUniqueId()))
        {
            Game game = gameMap.get(player.getUniqueId());

            if (game.getCurrentQuestion() == null)
                return;

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (Integer.parseInt(event.getMessage()) == game.getCurrentQuestion().getAnswer())
                    {
                        player.sendMessage(ChatColor.GREEN + "Correct!");
                        game.generateQuestion();
                    }
                    else
                        player.sendMessage(ChatColor.RED + "Incorrect, try again!");

                }
            }.runTask(plugin);
        }
    }

    public void buildGrid(int[][] grid, Location loc)
    {
        for (int x = 0; x < grid.length - 2; x++)
        {
            for (int z = 0; z < grid[x].length - 2; z++)
            {
                Location blockLoc;
                for (int y = loc.getBlockY(); y <= loc.getBlockY() + grid[x][z]; y++)
                {
                    blockLoc = new Location(loc.getWorld(), loc.getBlockX() + x + 1, y, loc.getBlockZ() + z);
                    blockLoc.getBlock().setType(Material.STONE);

                    if (x == 0)
                    {
                        blockLoc = new Location(loc.getWorld(), loc.getBlockX() + x + 1.5, y + 1.7, loc.getBlockZ() + z + 0.5);
                        Hologram hologram = HologramsAPI.createHologram(plugin, blockLoc);
                        hologram.appendTextLine(ChatColor.RED + "" + ChatColor.BOLD + z);
                    }

                    else if (z == 0)
                    {
                        blockLoc = new Location(loc.getWorld(), loc.getBlockX() + x + 1.5, loc.getBlockY() + grid[x][z + 1] + 1.7, loc.getBlockZ() + z + 0.5);
                        Hologram hologram = HologramsAPI.createHologram(plugin, blockLoc);
                        hologram.appendTextLine(ChatColor.RED + "" + ChatColor.BOLD + x);
                    }
                }
            }
        }
    }

    public HashMap<UUID, Game> getGameMap()
    {
        return gameMap;
    }
}
