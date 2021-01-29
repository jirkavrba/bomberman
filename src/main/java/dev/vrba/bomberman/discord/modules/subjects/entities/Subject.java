package dev.vrba.bomberman.discord.modules.subjects.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        // Make each combination of guildId and code unique
        uniqueConstraints = @UniqueConstraint(columnNames = {"code", "guildId"})
)
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public long guildId;

    // The subject's code (eg. 4IZ110)
    @NotNull
    public String code;

    // The subject's full name (eg. Informační a komunikační technologie)
    @NotNull
    public String name;

    public long associatedRoleId;

    @NotNull
    @OneToMany
    public final List<AssignedChannel> assignedChannels = List.of();
}
