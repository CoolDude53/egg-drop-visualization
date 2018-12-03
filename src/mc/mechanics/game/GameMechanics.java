package mc.mechanics.game;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import mc.Setup;
import mc.mechanics.Cleanable;
import mc.mechanics.Mechanic;
import mc.mechanics.game.commands.StartGame;
import mc.mechanics.game.commands.StopGame;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class GameMechanics extends Mechanic implements Cleanable
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
        plugin.getCommand("stopgame").setExecutor(new StopGame(plugin));

        //plugin.getCommand("toggle").setTabCompleter(new ToggleTabCompleter());
    }

    public void startGame(Player player)
    {
        Game game = new Game(plugin, player);
        gameMap.put(player.getUniqueId(), game);

        game.start();
    }

    public void startGame(Player player, int floors, int eggs)
    {
        Game game = new Game(plugin, player, floors, eggs);
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

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (gameMap.containsKey(event.getPlayer().getUniqueId()))
        {
            Game game = gameMap.get(event.getPlayer().getUniqueId());

            if (event.getTo().getBlockX() > game.getLoc1().getBlockX() && event.getTo().getBlockX() <= game.getLoc2().getBlockX() && event.getTo().getBlockZ() >= game.getLoc1().getBlockZ() && event.getTo().getBlockZ() < game.getLoc2().getBlockZ() && event.getTo().getBlockY() > game.getLoc1().getBlockY() && event.getTo().getBlockY() <= game.getLoc2().getBlockY())
            {
                int floor = event.getTo().getBlockX() - game.getLoc1().getBlockX() - 1;
                int egg = event.getTo().getBlockZ() - game.getLoc1().getBlockZ();
                int val = event.getTo().getBlockY() - game.getLoc1().getBlockY() - 1;
                sendActionbar(event.getPlayer(), ChatColor.GOLD + "Floor: " + floor + " Eggs: " + egg + " Value: " + val);
            }
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event)
    {
        event.getPlayer().setGameMode(GameMode.CREATIVE);
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event)
    {
        if (gameMap.containsKey(event.getPlayer().getUniqueId()))
        {
            gameMap.get(event.getPlayer().getUniqueId()).clean();
            gameMap.remove(event.getPlayer().getUniqueId());
        }
    }

    public void buildGrid(Game game, Location loc)
    {
        int[][] grid = game.getGrid();

        for (int x = 0; x < grid.length - 1; x++)
        {
            for (int z = 0; z < grid[x].length - 1; z++)
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
                        game.getHolograms().add(hologram);
                    }

                    else if (z == 0)
                    {
                        blockLoc = new Location(loc.getWorld(), loc.getBlockX() + x + 1.5, loc.getBlockY() + grid[x][z + 1] + 1.7, loc.getBlockZ() + z + 0.5);
                        Hologram hologram = HologramsAPI.createHologram(plugin, blockLoc);
                        hologram.appendTextLine(ChatColor.RED + "" + ChatColor.BOLD + x);
                        game.getHolograms().add(hologram);
                    }
                }
            }
        }

        game.setLoc1(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        game.setLoc2(new Location(loc.getWorld(), loc.getBlockX() + game.getFloors() + 1, loc.getBlockY() + game.getFloors(), loc.getBlockZ() + game.getEggs()));
    }

    public static void sendActionbar(Player player, String message)
    {
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(icbc, ChatMessageType.GAME_INFO);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public HashMap<UUID, Game> getGameMap()
    {
        return gameMap;
    }

    @Override
    public void clean()
    {
        for (Game game : gameMap.values())
            game.clean();
    }
}
