package us.racem.guilds.sponge.events;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.impl.AbstractEvent;

public class GuildClaimEvent extends AbstractEvent implements Cancellable {
    private final Cause cause;
    private boolean cancelled = false;

    public GuildClaimEvent(int x, int z, Player player, boolean cancelled) {
        EventContext ctx = EventContext.builder()
                .add(EventContextKeys.OWNER, player)
                .build();

        this.cause = Cause.builder()
                .append(player)
                .append(x)
                .append(z)
                .build(ctx);
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }
}
