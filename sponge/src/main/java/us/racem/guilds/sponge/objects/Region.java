package us.racem.guilds.sponge.objects;

import com.flowpowered.math.vector.Vector2i;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "regions")
@Data
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Columns(columns = {
            @Column(name = "x_pos"),
            @Column(name = "z_pos")
    })
    @Type(type = "Vector2I")
    private Vector2i pos;

    @OneToMany(mappedBy = "section")
    private List<Plot> plots = new ArrayList<>();

    private String name;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    private Guild owner;
}
