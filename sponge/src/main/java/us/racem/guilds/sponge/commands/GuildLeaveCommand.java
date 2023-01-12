package us.racem.guilds.sponge.commands;

import com.flowpowered.math.vector.Vector2i;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;

import javax.annotation.Nonnull;
import java.util.UUID;

public class GuildLeaveCommand extends GuildCommand {
    @Override @Nonnull
    public CommandResult execute(@Nonnull CommandSource src,
                                 @Nonnull CommandContext args) {
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
        rmPlayerFromGuild(userId);
        src.sendMessage(Text.of("You left ", name));

        if (isGuildEmpty(name)) {
            src.sendMessage(Text.of(name, " no longer has any players, deleting."));
            rmGuild(name);
        }

        return CommandResult.success();
    }
}
