package us.racem.guilds.sponge.objects;

import com.flowpowered.math.vector.Vector2i;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

@Entity @Table(name = "plots")
@Data
public class Plot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    private Guild owner;

    @Columns(columns = {
        @Column(name = "x_pos"),
        @Column(name = "z_pos")
    })
    @Type(type = "Vector2I")
    private Vector2i pos;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region section;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User player;
}
