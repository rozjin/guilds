package us.racem.guilds.sponge.persist;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.type.UUIDCharType;
import org.spongepowered.api.service.sql.SqlService;
import us.racem.guilds.Config;
import us.racem.guilds.objects.*;
import us.racem.guilds.sponge.objects.*;
import us.racem.guilds.sponge.persist.types.Vector2IType;

import java.sql.SQLException;
import java.util.UUID;

public class Access {
    public static Access INSTANCE;

    private SqlService service;

    private Configuration config;
    private StandardServiceRegistry registry;
    private Metadata meta;
    private SessionFactory sf;

    private String protocol;
    private String url;

    private String user;
    private String pass;
    private String name;

    private String jdbc;

    public Access() throws SQLException {
        this.protocol = "mariadb://";
        this.url = Config.Storage.url;
        if (url.equals("DATABASE_URL")) throw new SQLException("No provided Database");

        this.user = Config.Storage.user;
        this.pass = Config.Storage.pass;
        this.name = Config.Storage.name;

        initConfig();
        initSessionFactory();
    }

    private void initConfig() {
        this.jdbc = "jdbc" + ":" + protocol + url + "/" + name;
        this.config = new Configuration();

        config.setProperty("hibernate.connection.driver_class", "org.mariadb.jdbc.Driver");

        config.setProperty("hibernate.connection.url", jdbc);
        config.setProperty("hibernate.connection.username", user);
        config.setProperty("hibernate.connection.password", pass);

        config.setProperty("hibernate.dialect", "org.hibernate.dialect.MariaDB106Dialect");
        config.setProperty("hibernate.hbm2ddl.auto", "update");
    }

    private void initSessionFactory() {
        this.registry = new StandardServiceRegistryBuilder()
                .applySettings(config.getProperties())
                .build();

        this.meta = new MetadataSources(registry)
                .addAnnotatedClass(Guild.class)
                .addAnnotatedClass(Law.class)
                .addAnnotatedClass(Script.class)
                .addAnnotatedClass(Plot.class)
                .addAnnotatedClass(Region.class)
                .addAnnotatedClass(User.class)
                .getMetadataBuilder()
                .applyBasicType(Vector2IType.INSTANCE, "Vector2I")
                .applyBasicType(UUIDCharType.INSTANCE, UUID.class.getName())
                .build();
        this.sf = meta
                .buildSessionFactory();
    }

    public void persist() {
        sf.close();
    }

    public Session ctx() {
        return sf.openSession();
    }
}
