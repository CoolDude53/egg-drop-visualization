package mc.mechanics;

import mc.Setup;
import org.bukkit.event.Listener;

public abstract class Mechanic implements Listener
{
    protected final Setup plugin;

    protected Mechanic(Setup plugin)
    {
        this.plugin = plugin;
    }

    public abstract void register();
}
