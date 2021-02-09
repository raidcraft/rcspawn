package de.raidcraft.spawn.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Subcommand;
import de.raidcraft.spawn.Constants;
import de.raidcraft.spawn.RCSpawn;
import de.raidcraft.spawn.entities.Spawn;
import org.bukkit.World;
import org.bukkit.entity.Player;

@CommandAlias("spawn|rcspawn")
public class PlayerCommands extends BaseCommand {

    private final RCSpawn plugin;

    public PlayerCommands(RCSpawn plugin) {
        this.plugin = plugin;
    }

    @Subcommand("spawn")
    @CommandCompletion("@worlds")
    @CommandPermission(Constants.PERMISSION_PREFIX + "player.spawn")
    public void spawn(Player player, @Conditions("spawn") World world) {

        player.teleport(Spawn.of(world).location());
    }
}
