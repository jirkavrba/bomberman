package dev.vrba.bomberman.discord.modules.subjects.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AssignedChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public final long id = 0;

    public long subjectId = 0;

    @NotNull
    public AssignedChannelType type = AssignedChannelType.Text;

    enum AssignedChannelType {
        Text,
        Voice
    }
}

