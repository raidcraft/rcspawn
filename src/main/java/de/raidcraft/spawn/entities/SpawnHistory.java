package de.raidcraft.spawn.entities;

import de.raidcraft.spawn.Constants;
import io.ebean.Finder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.silthus.ebean.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PACKAGE)
@Accessors(fluent = true)
@Table(name = Constants.TABLE_PREFIX + "history")
public class SpawnHistory extends BaseEntity {

    static final Finder<UUID, SpawnHistory> find = new Finder<>(SpawnHistory.class);

    public static SpawnHistory of(@NonNull RCPlayer player, @NonNull Spawn spawn) {

        return find.query().where()
                .eq("player_id", player.id())
                .and().eq("spawn_id", spawn.id())
                .findOneOrEmpty()
                .orElseGet(() -> new SpawnHistory(player, spawn));
    }

    @ManyToOne(optional = false)
    private RCPlayer player;
    @ManyToOne(optional = false)
    private Spawn spawn;
    private Instant lastExecution;

    public SpawnHistory(RCPlayer player, Spawn spawn) {

        this.player = player;
        this.spawn = spawn;
    }
}
