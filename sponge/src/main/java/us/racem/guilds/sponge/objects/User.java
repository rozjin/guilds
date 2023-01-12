package us.racem.guilds.sponge.objects;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    public UUID id;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    public Guild guild;

    @OneToMany(mappedBy = "player")
    public List<Plot> plots = new ArrayList<>();
}
