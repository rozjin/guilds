package us.racem.guilds.objects;

import com.google.common.graph.Graph;
import com.sk89q.worldedit.BlockVector2D;
import us.racem.guilds.util.GuildOTM;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@GuildObject(id = 1479)
public class Guild {
    @SuppressWarnings("UnstableApiUsage")
    private Graph<Guild> subGuilds;
    private List<Consumer<Guild>> rules;
    private UUID guildID;

    private List<BlockVector2D> bounds;
    private List<UUID> players;
    private GuildOTM<UUID, BlockVector2D> landOwners;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UUID)) return false;
        return obj.equals(guildID);
    }
}
