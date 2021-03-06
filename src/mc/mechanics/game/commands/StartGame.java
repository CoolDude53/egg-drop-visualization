package mc.mechanics.game.commands;

import mc.Setup;
import mc.mechanics.game.GameMechanics;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartGame implements CommandExecutor
{
    private final GameMechanics gameMechanics;

    public StartGame(Setup plugin)
    {
        this.gameMechanics = plugin.getMechanics().getGameMechanics();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;

            if (args.length == 0)
            {
                gameMechanics.startGame(player);
                return true;
            }
            else if (args.length == 2)
            {
                int floors, eggs;
                try
                {
                    floors = Integer.parseInt(args[0]) + 1;
                    eggs = Integer.parseInt(args[1]) + 1;
                } catch (NumberFormatException ex)
                {
                    player.sendMessage(ChatColor.RED + "Invalid Number!");
                    return false;
                }

                gameMechanics.startGame(player, floors, eggs);
                return true;
            }
        }

        else
        {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        return false;
    }
}
