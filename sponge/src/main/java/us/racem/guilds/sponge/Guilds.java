package us.racem.guilds.sponge;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.plugin.builtin.jvm.Plugin;
import us.racem.guilds.sponge.commands.GuildClaimCommand;
import us.racem.guilds.sponge.commands.GuildLeaveCommand;
import us.racem.guilds.sponge.commands.GuildRegisterCommand;
import us.racem.guilds.sponge.persist.Access;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

@Plugin("guilds")
public class Guilds {
    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path guildsDir;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    private void loadDirs() throws IOException {
        Files.createDirectories(guildsDir);
    }

    private void loadConfig() throws IOException, ObjectMappingException {
        CommentedConfigurationNode node = loader.load(ConfigurationOptions
                        .defaults()
                        .withShouldCopyDefaults(true));
        node.getValue(TypeToken.of(Config.class),
                new Config());
        loader.save(node);
    }

    private void loadDB() throws SQLException, IOException {
        Access.INSTANCE = new Access();
    }

    private void saveDB() {
        Access.INSTANCE.persist();
    }

    private void registerCommands() {
        CommandSpec guildRegisterCommandSpec = CommandSpec.builder()
                .description(Text.of("Create a new guild"))
                .arguments(
                    GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))),
                    GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.string(Text.of("creator"))))
                )
                .executor(new GuildRegisterCommand())
                .build();
        CommandSpec guildLeaveCommandSpec = CommandSpec.builder()
                .description(Text.of("Leave your current guild"))
                .executor(new GuildLeaveCommand())
                .build();

        CommandSpec guildClaimCommandSpec = CommandSpec.builder()
                .description(Text.of("Claim land for your guild"))
                .executor(new GuildClaimCommand())
                .build();

        CommandSpec guildCommandSpec = CommandSpec.builder()
                .description(Text.of("Create and manage guilds"))
                .child(guildRegisterCommandSpec, "register", "r")
                .child(guildLeaveCommandSpec, "leave", "l")

                .child(guildClaimCommandSpec, "claim", "c")

                .build();

        Sponge
                .getCommandManager()
                .register(this, guildCommandSpec, "guild", "g");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) throws IOException, ObjectMappingException, SQLException {
        loadDirs();
        loadConfig();
        loadDB();

        registerCommands();
    }

    @Listener
    public void onServerStopping(GameStoppingServerEvent event) throws IOException, ObjectMappingException {
        saveDB();
    }
}