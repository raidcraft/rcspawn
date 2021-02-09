package de.raidcraft.spawn.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import de.raidcraft.spawn.RCSpawn;
import de.raidcraft.spawn.entities.Spawn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static de.raidcraft.spawn.Constants.PERMISSION_PREFIX;

@CommandPermission(PERMISSION_PREFIX + "admin")
@CommandAlias("spawn:admin|rcspawn:admin")
public class AdminCommands extends BaseCommand {

    private final RCSpawn plugin;

    public AdminCommands(RCSpawn plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reload")
    @CommandPermission(PERMISSION_PREFIX + "admin.reload")
    public void reload() {

        final CommandIssuer commandIssuer = getCurrentCommandIssuer();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.reload();
            commandIssuer.sendMessage(ChatColor.GREEN + "RCSpawn wurde erfolgreich neu geladen.");
        });
    }

    @Subcommand("set")
    @CommandAlias("setspawn|spawnset")
    @CommandPermission(PERMISSION_PREFIX + "admin.setspawn")
    public void set(Player player) {

        Spawn.set(player.getLocation());

        getCurrentCommandIssuer().sendMessage(ChatColor.GREEN + "Der Spawn von " + ChatColor.AQUA
                + player.getLocation().getWorld().getName() + ChatColor.GREEN + " wurde gesetzt.");
    }
}
