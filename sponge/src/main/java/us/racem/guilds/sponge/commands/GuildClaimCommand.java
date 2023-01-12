package us.racem.guilds.sponge.commands;

import com.flowpowered.math.vector.Vector2i;
import org.hibernate.Session;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;
import us.racem.guilds.sponge.objects.Guild;
import us.racem.guilds.sponge.objects.Plot;
import us.racem.guilds.sponge.objects.User;
import us.racem.guilds.sponge.persist.Access;
import us.racem.guilds.sponge.persist.AccessTransaction;

import java.util.UUID;

public class GuildClaimCommand extends GuildCommand {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of(TextStyles.BOLD, "Player-only command."));

            return CommandResult.empty();
        }

        Player player = (Player) src;
        UUID userId = player.getUniqueId();
        if (!isPlayerInGuild(userId)) {
            src.sendMessage(Text.of("You are not part of a guild."));

            return CommandResult.empty();
        }

        String name = guildNameOfUser(userId);
        Vector2i pos = getPos(player);
        if (isLandClaimed(pos)) {
            if (isOwnedBy(pos, name)) src.sendMessage(Text.of("This chunk has already been claimed."));
            else src.sendMessage(Text.of("Claimed by another guild."));

            return CommandResult.empty();
        }

        try (Session ctx = Access.INSTANCE.ctx();
             AccessTransaction tx = new AccessTransaction(ctx)) {
            User user = fetchUser(userId, ctx);
            Guild guild = fetchGuild(name, ctx);

            Plot plot = new Plot();
            plot.setSection(null);
            plot.setPlayer(user);
            plot.setOwner(guild);
            plot.setPos(pos);

            user.getPlots().add(plot);
            guild.getPlots().add(plot);

            ctx.save(guild);
            ctx.save(user);
            ctx.save(plot);

            tx.commit();
        }

        src.sendMessage(Text.of("Claimed ", pos.getX(), ", ", pos.getY(), " for ", name));
        return CommandResult.success();
    }
}
