package mc.mechanics;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import mc.GeneralUtils;
import mc.Setup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class Mechanic implements Listener
{
    protected final Setup plugin;

    protected Mechanic(Setup plugin)
    {
        this.plugin = plugin;
    }

    public abstract void register();

    public static WorldGuardPlugin getWorldGuard()
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin))
        {
            GeneralUtils.log(ChatColor.RED + "Could not load WorldGuard!");
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

    public static WorldEditPlugin getWorldEdit()
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin == null || !(plugin instanceof WorldEditPlugin))
        {
            GeneralUtils.log(ChatColor.RED + "Could not load WorldEdit!");
            return null;
        }
        return (WorldEditPlugin) plugin;
    }
}
