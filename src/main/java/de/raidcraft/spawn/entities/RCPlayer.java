package de.raidcraft.spawn.entities;

import de.raidcraft.spawn.Constants;
import io.ebean.Finder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.silthus.ebean.BaseEntity;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Optional;
import java.util.UUID;

/**
 * The player is the database link between the bukkit player
 * and the achievement plugin.
 * <p>It's id and name always match the bukkit representation of the player.
 */
@Entity
@Getter
@Setter
@Accessors(fluent = true)
@Table(name = Constants.TABLE_PREFIX + "players")
public class RCPlayer extends BaseEntity {


    public static final Finder<UUID, RCPlayer> find = new Finder<>(RCPlayer.class);

    /**
     * Gets or creates a new achievement player from the given offline player.
     * <p>The id will be the same as the players id.
     *
     * @param player the player to create or get the achievement player for
     * @return the created or existing achievement player
     */
    public static RCPlayer of(OfflinePlayer player) {

        return Optional.ofNullable(find.byId(player.getUniqueId()))
                .orElseGet(() -> {
                    RCPlayer achievementPlayer = new RCPlayer(player);
                    achievementPlayer.insert();
                    return achievementPlayer;
                });
    }

    /**
     * Tries to find an achievement player with the given id.
     * <p>The id is the same as the Minecraft's player id.
     * <p>Returns an empty optional if no player by the id is found.
     *
     * @param uuid the unique id of the player
     * @return the player or an empty optional
     */
    public static Optional<RCPlayer> byId(UUID uuid) {

        if (uuid == null) return Optional.empty();

        return Optional.ofNullable(find.byId(uuid));
    }

    /**
     * The name of the player.
     */
    @Setter(AccessLevel.PRIVATE)
    private String name;

    RCPlayer(OfflinePlayer player) {

        this.id(player.getUniqueId());
        this.name(player.getName());
    }

    /**
     * @return the offline player of this achievement player
     */
    public OfflinePlayer offlinePlayer() {

        return Bukkit.getOfflinePlayer(id());
    }

    /**
     * @return the online player of this achievement player if it is online
     */
    public Optional<Player> bukkitPlayer() {

        return Optional.ofNullable(Bukkit.getPlayer(id()));
    }

}
