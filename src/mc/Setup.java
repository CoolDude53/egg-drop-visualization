package mc;

import mc.mechanics.Mechanics;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class Setup extends JavaPlugin
{
    public static final String defaultWorld = "world";
    private Location spawn;
    private Mechanics mechanics;

    @Override
    public void onEnable()
    {
        setMechanics(new Mechanics(this));
        getMechanics().register();
        GeneralUtils.log(ChatColor.GREEN + "Algs has been enabled!");
    }

    @Override
    public void onDisable()
    {
        mechanics.cleanUp();
        GeneralUtils.log(ChatColor.GREEN + "Algs has been disabled!");
    }

    public Location getSpawn()
    {
        return spawn;
    }

    public void setSpawn(Location spawn)
    {
        this.spawn = spawn;
    }

    public Mechanics getMechanics()
    {
        return mechanics;
    }

    private void setMechanics(Mechanics mechanics)
    {
        this.mechanics = mechanics;
    }

    public String getDefaultWorld()
    {
        return defaultWorld;
    }
}
