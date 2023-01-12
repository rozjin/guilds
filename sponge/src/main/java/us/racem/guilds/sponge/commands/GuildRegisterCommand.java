package us.racem.guilds.sponge.commands;

import com.flowpowered.math.vector.Vector2i;
import org.hibernate.Session;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;
import us.racem.guilds.sponge.objects.Guild;
import us.racem.guilds.sponge.objects.Plot;
import us.racem.guilds.sponge.objects.Region;
import us.racem.guilds.sponge.objects.User;
import us.racem.guilds.sponge.persist.Access;
import us.racem.guilds.sponge.persist.AccessTransaction;

import javax.annotation.Nonnull;
import java.util.UUID;

public class GuildRegisterCommand extends GuildCommand {
    @Override @Nonnull
    public CommandResult execute(@Nonnull CommandSource src,
                                 @Nonnull CommandContext args) {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of(TextStyles.BOLD, "Player-only command."));

            return CommandResult.empty();
        }

        Player player = (Player) src;
        String name = args.<String>getOne("name").get();
        if (isExistingGuild(name)) {
            src.sendMessage(Text.of("A guild with the name ",
                    TextStyles.UNDERLINE, name,
                    TextStyles.RESET, " already exists."));

            return CommandResult.empty();
        }

        Vector2i pos = getPos(player);
        if (isLandClaimed(pos)) {
            src.sendMessage(Text.of("This chunk has already been claimed."));

            return CommandResult.empty();
        }

        UUID userId = idFromArgOptional(src, args, "creator");
        if (isPlayerInGuild(userId)) {
            if (args.hasAny("creator")) src.sendMessage(Text.of(player.getName(), " is already part of a Guild."));
            else src.sendMessage(Text.of("You are already part of a guild!"));

            return CommandResult.empty();
        }

        User user = new User();
        Guild guild = new Guild();
        Plot plot = new Plot();
        Region region = new Region();
        user.setGuild(guild);
        user.getPlots().add(plot);
        user.setId(userId);

        region.setPos(pos);
        region.setName("default");
        region.setOwner(guild);
        region.getPlots().add(plot);

        plot.setOwner(guild);
        plot.setPos(pos);
        plot.setSection(region);
        plot.setPlayer(user);

        guild.setName(name);
        guild.getPlayers().add(user);
        guild.getPlots().add(plot);
        guild.getRegions().add(region);

        try (Session ctx = Access.INSTANCE.ctx();
             AccessTransaction tx = new AccessTransaction(ctx)) {
            ctx.save(guild);
            ctx.save(user);

            tx.commit();
        }

        src.sendMessage(Text.of("Created guild with name ", TextStyles.UNDERLINE, name));
        return CommandResult.success();
    }
}
