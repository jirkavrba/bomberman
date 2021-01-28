package dev.vrba.bomberman.discord.modules.subjects.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public final long id = 0;

    public final long guildId = 0;

    // The subject's code (eg. 4IZ110)
    @NotNull
    public final String code = "";

    // The subject's full name (eg. Informační a komunikační technologie)
    @NotNull
    public final String name = "";

    public final long associatedRoleId = 0;

    public final long assignedChannelCategoryId = 0;

    @NotNull
    @OneToMany
    public final List<AssignedChannel> assignedChannels = List.of();
}
