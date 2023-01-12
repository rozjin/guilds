package us.racem.guilds.sponge.registry;

import java.sql.SQLException;
import java.util.HashMap;

public abstract class Registry<T> {
    protected HashMap<Integer, T> registry;

    public abstract T find(int id) throws SQLException;
    public abstract void commit(T object) throws SQLException;
}
