package us.racem.guilds;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "guilds",
        name = "Guilds",
        description = "Guilds: THE AUSTRALIAN TAXATION OFFICE",
        authors = {
                "Racemus"
        }
)

public class Guilds {
    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

    }
}