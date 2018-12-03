package mc.mechanics.game.commands;

import mc.Setup;
import mc.mechanics.game.GameMechanics;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopGame implements CommandExecutor
{
    private final GameMechanics gameMechanics;

    public StopGame(Setup plugin)
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
                if (gameMechanics.getGameMap().containsKey(player.getUniqueId()))
                {
                    gameMechanics.getGameMap().get(player.getUniqueId()).clean();
                    gameMechanics.getGameMap().remove(player.getUniqueId());
                }

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