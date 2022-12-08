package us.racem.guilds.registry;

import com.google.inject.Inject;
import com.sk89q.worldedit.BlockVector2D;
import org.spongepowered.api.config.ConfigDir;
import us.racem.guilds.objects.Region;
import us.racem.guilds.objects.Guild;
import us.racem.guilds.util.GuildList;
import us.racem.guilds.util.GuildUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GlobalRegistry implements ObjectRegistry {
    private final GuildList<UUID> players;
    private final GuildList<BlockVector2D> chunks;
    private final GuildList<BlockVector2D> landProperties;
    private final GuildList<Guild> guilds;
    private final GuildList<Object> objects;
    private final GuildList<Region> regions;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    public GlobalRegistry() {
        this.players = new GuildList<>();
        this.chunks = new GuildList<>();
        this.landProperties = new GuildList<>();
        this.guilds = new GuildList<>();
        this.objects = new GuildList<>();
        this.regions = new GuildList<>();
    }

    private GuildList<?> ofStore(String name) {
        switch (name) {
            case "landProperties":
            case "properties":
                return landProperties;
            case "players":
                return players;
            case "chunks":
            case "lands":
                return chunks;
            case "guilds":
            case "polities":
                return guilds;
            case "objects":
            case "entities":
                return objects;
            case "regions":
                return regions;
            default:
                return null;
        }
    }

    @Override
    public List<?> all(String name) {
        return ofStore(name);
    }

    @Override
    public <K, T> T of(String name, K key) {
        List<?> store = ofStore(name);
        if (store == null) return null;

        int valIdx = store.indexOf(key);
        if (valIdx < 0) return null;

        return (T) store.get(valIdx);
    }

    @Override
    public <T> void register(String name, T... obj) {
        GuildList<?> store = ofStore(name);
        if (store == null) return;
        if (GuildUtils.to() != store.kind) return;


    }

    @Override
    public void inject(Path registryPath) {

    }
}
