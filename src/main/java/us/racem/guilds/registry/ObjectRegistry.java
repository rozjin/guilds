package us.racem.guilds.registry;

import com.google.common.collect.ImmutableList;

import java.nio.file.Path;
import java.util.List;

public interface ObjectRegistry {
    List<?> all(String name);
    <K, T> T of(String name, K key);

    <T> void register(String name, T... obj);

    void inject(Path registryPath);
}
