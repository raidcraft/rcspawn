package de.raidcraft.spawn;

import lombok.NonNull;
import org.bukkit.World;
import org.bukkit.entity.Player;

public final class Constants {

    public static final String TABLE_PREFIX = "rcspawn_";
    public static final String PERMISSION_PREFIX = "rcspawn.";
    public static final String SPAWN_PREFIX = PERMISSION_PREFIX + "spawn";
    public static final String SPAWN_OTHER = SPAWN_PREFIX + ".other";
    public static final String SPAWN_WORLD_PREFIX = SPAWN_PREFIX + ".world.";

    public static String worldPermission(@NonNull World world) {

        return SPAWN_WORLD_PREFIX + world.getName().toLowerCase();
    }

    public static boolean hasWorldPermission(@NonNull Player player, @NonNull World world) {

        return player.hasPermission(SPAWN_WORLD_PREFIX + world.getName().toLowerCase());
    }

    public static boolean hasOtherWorldPermission(@NonNull Player player, @NonNull World world) {

        return player.hasPermission(SPAWN_OTHER) || player.hasPermission(worldPermission(world) + ".other");
    }

    private Constants() {}
}
