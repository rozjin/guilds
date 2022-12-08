package us.racem.guilds.objects;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.BlockVector2D;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@GuildObject(id = 1788)
public class Region {
    public final BlockVector2D p1;
    public final BlockVector2D p2;
    public final BlockVector2D capital;

    public final String name;
    public final String friendlyName;

    public List<Guild> guilds;
    public Region(BlockVector2D p1, BlockVector2D p2, BlockVector2D capital,
                  String friendlyName, String name,
                  Guild... guilds) {
        this.p1 = p1;
        this.p2 = p2;
        this.name = name;
        this.capital = capital == null ? p1.add(p2).divide(2).toBlockVector2D() : capital;
        this.guilds = Lists.newArrayList(guilds);

        if (friendlyName == null) {
            this.friendlyName = StringUtils.capitalize(name.toLowerCase().replace("_", " "));
        } else {
            this.friendlyName = friendlyName;
        }
    }
}
