package mc;

import org.bukkit.Bukkit;

import java.util.Random;

public class GeneralUtils
{
    private static final Random rand = new Random();


    // Chat Utils
    public static void log(String msg)
    {
        Bukkit.getConsoleSender().sendMessage("[Algs] " + msg);
    }

    public static int randInt(int min, int max)
    {
        return rand.nextInt(Math.abs(max - min) + 1) + min;
    }
}
