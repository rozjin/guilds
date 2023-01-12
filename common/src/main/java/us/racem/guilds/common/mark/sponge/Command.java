package us.racem.guilds.common.mark.sponge;

import org.spongepowered.api.command.spec.CommandExecutor;
import us.racem.guilds.common.mark.meta.Requires;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Requires(depends = Spec.class)

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    Class<? extends CommandExecutor> inherits();
}
