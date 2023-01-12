package us.racem.guilds.sponge.commands;

import com.flowpowered.math.vector.Vector2i;
import org.hibernate.Session;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import us.racem.guilds.sponge.objects.Guild;
import us.racem.guilds.sponge.objects.User;
import us.racem.guilds.sponge.persist.Access;
import us.racem.guilds.sponge.persist.AccessTransaction;

import java.util.UUID;

public abstract class GuildCommand implements CommandExecutor {
    protected boolean isExistingGuild(String name) {
        try (Session ctx = Access.INSTANCE.ctx()) {
            return ctx.createQuery("SELECT 1 FROM Guild WHERE name = :name")
                    .setParameter("name", name)
                    .uniqueResult() != null;
        }
    }

    protected boolean isGuildEmpty(String name) {
        try (Session ctx = Access.INSTANCE.ctx()) {
            return ctx.createQuery("SELECT 1 FROM Guild g WHERE g.name = :name AND g.players IS EMPTY")
                    .setParameter("name", name)
                    .uniqueResult() != null;
        }
    }

    protected boolean isLandClaimed(Vector2i pos) {
        try (Session ctx = Access.INSTANCE.ctx()) {
            return ctx.createQuery("SELECT 1 FROM Plot WHERE pos = :pos")
                    .setParameter("pos", pos)
                    .uniqueResult() != null;
        }
    }

    protected boolean isOwnedBy(Vector2i pos, String name) {
        try (Session ctx = Access.INSTANCE.ctx()) {
            return ctx.createQuery("SELECT 1 FROM Plot WHERE pos = :pos AND owner.name = :name")
                    .setParameter("pos", pos)
                    .setParameter("name", name)
                    .uniqueResult() != null;
        }
    }

    protected boolean isPlayerInGuild(UUID id) {
        try (Session ctx = Access.INSTANCE.ctx()) {
            return ctx.createQuery("SELECT 1 FROM User WHERE id = :id")
                    .setParameter("id", id)
                    .uniqueResult() != null;
        }
    }

    protected boolean isPlayerInGuild(UUID id, String name) {
        try (Session ctx = Access.INSTANCE.ctx()) {
            return ctx.createQuery("SELECT 1 FROM User WHERE id = :id AND guild.name = :name")
                    .setParameter("id", id)
                    .setParameter("name", name)
                    .uniqueResult() != null;
        }
    }

    protected String guildNameOfUser(UUID id) {
        try (Session ctx = Access.INSTANCE.ctx()) {
            return (String) ctx.createQuery("SELECT guild.name FROM User WHERE id = :id")
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    protected Guild fetchGuild(String name, Session ctx) {
        return (Guild) ctx.createQuery("FROM Guild WHERE name = :name")
                .setParameter("name", name)
                .uniqueResult();
    }

    protected User fetchUser(UUID id, Session ctx) {
        return (User) ctx.createQuery("FROM User WHERE id = :id")
                .setParameter("id", id)
                .uniqueResult();
    }

    protected void rmPlayerFromGuild(UUID id) {
        try (Session ctx = Access.INSTANCE.ctx();
             AccessTransaction tx = new AccessTransaction(ctx)) {
            ctx.createQuery("UPDATE Plot SET player = NULL WHERE player.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            ctx.createQuery("DELETE FROM User WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
        }
    }

    protected void rmGuild(String name) {
        try (Session ctx = Access.INSTANCE.ctx();
             AccessTransaction tx = new AccessTransaction(ctx)) {

            ctx.remove(fetchGuild(name, ctx));
            tx.commit();
        }
    }

    protected UUID idFromArgOptional(CommandSource src, CommandContext args, String name) {
        if (args.hasAny(name)) return args.<org.spongepowered.api.entity.living.player.User>getOne("creator").get().getUniqueId();
        return ((Player) src).getUniqueId();
    }

    protected Vector2i getPos(Player player) {
        return player.getLocation().getChunkPosition().toVector2(true);
    }
}
