package us.racem.guilds.sponge;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Config {
    @Setting(value = "general")
    public static Config.Guilds guilds = new Config.Guilds();

    @Setting(value = "storage")
    public static Config.Storage storage = new Config.Storage();

    @ConfigSerializable
    public static class Guilds {

    }

    @ConfigSerializable
    public static class Storage {
        @Setting public static String url = "DATABASE_URL";

        @Setting public static String user = "DATABASE_USER";
        @Setting public static String pass = "DATABASE_PASS";
        @Setting public static String name = "DATABASE_NAME";
    }
}
