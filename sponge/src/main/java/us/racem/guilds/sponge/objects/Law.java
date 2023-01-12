package us.racem.guilds.sponge.objects;

import jakarta.persistence.*;

@Entity
@Table(name = "laws")
public class Law {
    @Id
    public int id;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    public Guild juris;

    @OneToOne(mappedBy = "act",
              cascade = CascadeType.ALL)
    public Script exec;
}
