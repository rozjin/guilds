package us.racem.guilds.sponge.objects;

import jakarta.persistence.*;
import lombok.Data;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "guilds")
@Data
public class Guild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    private String name;
    private int overlord_id;

    @OneToMany(mappedBy = "juris",
               cascade = CascadeType.ALL)
    private List<Law> acts = new ArrayList<>();

    @OneToMany(mappedBy = "owner",
               cascade = CascadeType.ALL)
    private List<Plot> plots = new ArrayList<>();

    @OneToMany(mappedBy = "owner",
               cascade = CascadeType.ALL)
    private List<Region> regions = new ArrayList<>();

    @OneToMany(mappedBy = "guild",
               cascade = CascadeType.ALL)
    private List<User> players = new ArrayList<>();

    @Transient
    private UniqueAccount acc;
}
