package de.raidcraft.spawn;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import com.google.common.base.Strings;
import de.raidcraft.spawn.commands.AdminCommands;
import de.raidcraft.spawn.commands.PlayerCommands;
import de.raidcraft.spawn.entities.RCPlayer;
import de.raidcraft.spawn.entities.Spawn;
import de.raidcraft.spawn.entities.SpawnHistory;
import io.ebean.Database;
import kr.entree.spigradle.annotations.PluginMain;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.silthus.ebean.Config;
import net.silthus.ebean.EbeanWrapper;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.stream.Collectors;

@PluginMain
public class RCSpawn extends JavaPlugin {

    @Getter
    @Accessors(fluent = true)
    private static RCSpawn instance;

    private Database database;
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private PluginConfig pluginConfig;

    private PaperCommandManager commandManager;

    @Getter
    private static boolean testing = false;

    public RCSpawn() {
        instance = this;
    }

    public RCSpawn(
            JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        instance = this;
        testing = true;
    }

    @Override
    public void onEnable() {

        loadConfig();
        setupDatabase();
        if (!testing) {
            setupListener();
            setupCommands();
        }
    }

    public void reload() {

        loadConfig();
    }

    private void loadConfig() {

        getDataFolder().mkdirs();
        pluginConfig = new PluginConfig(new File(getDataFolder(), "config.yml").toPath());
        pluginConfig.loadAndSave();
    }

    private void setupListener() {


    }

    private void setupCommands() {

        this.commandManager = new PaperCommandManager(this);

        commandManager.getCommandCompletions().registerAsyncCompletion("worlds", context ->
                Bukkit.getServer().getWorlds().stream()
                .map(World::getName).collect(Collectors.toSet()));
        commandManager.getCommandContexts().registerIssuerAwareContext(World.class, context -> {

            String worldName = context.popFirstArg();
            if (Strings.isNullOrEmpty(worldName)) {
                return context.getPlayer().getWorld();
            }

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                throw new InvalidCommandArgument("Es wurde keine Welt mit dem Namen " + worldName + " gefunden!");
            }

            return world;
        });
        commandManager.getCommandConditions().addCondition(World.class, "spawn", (context, execContext, world) -> {
            Player player = execContext.getPlayer();

            if (!world.equals(player.getWorld())) {
                if (!Constants.hasOtherWorldPermission(player, world)) {
                    throw new ConditionFailedException("Du hast nicht genügend Rechte dich in andere Welten zu teleportieren.");
                }
            }

            if (!Constants.hasWorldPermission(player, world)) {
                throw new ConditionFailedException("Du hast nicht genügend Rechte /spawn in dieser Welt zu benutzen.");
            }
        });

        commandManager.registerCommand(new AdminCommands(this));
        commandManager.registerCommand(new PlayerCommands(this));
    }

    private void setupDatabase() {

        this.database = new EbeanWrapper(Config.builder(this)
                .entities(
                        RCPlayer.class,
                        Spawn.class,
                        SpawnHistory.class
                )
                .build()).connect();
    }
}
