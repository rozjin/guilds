package us.racem.guilds.util;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;

public class GuildList<E> extends ArrayList<E> {
    public final Class<E> kind;
    public GuildList() {
        this.kind = GuildUtils.to();
    }
}
