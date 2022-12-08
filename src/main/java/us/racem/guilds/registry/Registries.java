package us.racem.guilds.registry;

import java.util.HashMap;
import java.util.Map;

public class Registries {
    public static final GlobalRegistry GLOBAL_REGISTRY = new GlobalRegistry();
    private static final Map<String, Object> registries = new HashMap<>();

    public Object registry(String name) {
        if (name == null) return null;
        if (!registries.containsKey(name)) return null;

        return registries.get(name);
    }

    public Object $(String name) {
        return registry(name);
    }
}
