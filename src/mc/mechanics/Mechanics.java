package mc.mechanics;

import mc.Setup;
import mc.mechanics.game.GameMechanics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

public class Mechanics
{
    private final Setup plugin;
    private final ArrayList<Mechanic> mechanics = new ArrayList<>();
    private GameMechanics gameMechanics;

    public Mechanics(Setup plugin)
    {
        this.plugin = plugin;
        instantiate();
    }

    private void instantiate()
    {
        plugin.setSpawn(new Location(Bukkit.getServer().getWorld("world"), 1340.5, 166, 65.5, 90, 0));

        Bukkit.getServer().getWorld(plugin.getDefaultWorld()).setSpawnLocation(plugin.getSpawn().getBlockX(), plugin.getSpawn().getBlockY(), plugin.getSpawn().getBlockZ());

        setGameMechanics(new GameMechanics(plugin));
    }

    public void register()
    {
        for (Mechanic mechanic : mechanics)
            mechanic.register();
    }

    public void cleanUp()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.closeInventory();
            for (PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());
        }

        // clean up
        gameMechanics.clean();

        // unregister all listeners just for good measure
        HandlerList.unregisterAll(plugin);
    }

    public GameMechanics getGameMechanics()
    {
        return gameMechanics;
    }

    private void setGameMechanics(GameMechanics gameMechanics)
    {
        this.gameMechanics = gameMechanics;
        mechanics.add(gameMechanics);
    }
}
