package de.raidcraft.spawn.entities;

import de.raidcraft.spawn.Constants;
import io.ebean.Finder;
import io.ebean.annotation.Index;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.silthus.ebean.BaseEntity;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Optional;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PACKAGE)
@Accessors(fluent = true)
@Table(name = Constants.TABLE_PREFIX + "spawns")
public class Spawn extends BaseEntity {

    static final Finder<UUID, Spawn> find = new Finder<>(Spawn.class);

    /**
     * Creates a new named spawn at the given location for the world.
     *
     * @param location the location to create a spawn at
     * @return the created or existing spawn
     */
    public static Spawn set(Location location) {

        World world = location.getWorld();
        if (world == null) {
            throw new NullArgumentException("world cannot be null!");
        }

        return byWorldName(world.getName())
                .map(spawn -> {
                    if (!spawn.location().equals(location)) {
                        spawn.location(location);
                        spawn.save();
                    }
                    return spawn;
                })
                .orElseGet(() -> {
            Spawn spawn = new Spawn(location);
            spawn.insert();
            return spawn;
        });
    }

    /**
     * Tries to find a spawn for the given world name.
     *
     * @param name the name of the world
     * @return the spawn of the world if it exists
     */
    public static Optional<Spawn> byWorldName(String name) {

        return find.query().where().ieq("name", name).findOneOrEmpty();
    }

    /**
     * Tries to find a spawn for the given world.
     * <p>The default spawn location of the world is retrieved if none is found.
     *
     * @param world the world to get the spawn for. must not be null.
     * @return the spawn of the world
     */
    public static Spawn of(@NonNull World world) {

        return find.query().where().eq("world", world.getUID())
                .findOneOrEmpty()
                .orElseGet(() -> new Spawn(world.getSpawnLocation()));
    }

    @Index
    private String name;
    @Index
    private UUID world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    @Transient
    private transient Location location;

    Spawn(@NonNull Location location) {

        this.location(location);
    }

    /**
     * @return the bukkit location of this spawn
     */
    public Location location() {

        if (location == null) {
            location = new Location(Bukkit.getWorld(world()), x(), y(), z(), yaw(), pitch());
        }

        return location;
    }

    /**
     * Sets a new spawn location for the world of the given location.
     * <p>A different spawn record maybe created or modified if the worlds of the given
     * location and this spawn record do not match.
     *
     * @param location the location to set the spawn to.
     * @return the created spawn for the world of the given location
     */
    public Spawn location(@NonNull Location location) {

        World world = location.getWorld();
        if (world == null) {
            throw new NullArgumentException("world cannot be null!");
        }

        if (!location.getWorld().getUID().equals(world())) {
            return set(location);
        }

        this.name(world.getName());
        this.world(world.getUID());
        this.x(location.getX());
        this.y(location.getY());
        this.z(location.getZ());
        this.pitch(location.getPitch());
        this.yaw(location.getYaw());

        this.location = location;

        return this;
    }
}
