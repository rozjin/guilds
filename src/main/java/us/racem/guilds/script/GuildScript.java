package us.racem.guilds.script;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.whl.quickjs.wrapper.QuickJSContext;
import groovy.util.GroovyScriptEngine;
import lombok.var;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.spongepowered.api.config.ConfigDir;

import java.io.IOException;
import java.nio.file.Path;

public class GuildScript {
    private QuickJSContext ctx;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path scripts;

    public GuildScript() throws IOException {

    }
}
