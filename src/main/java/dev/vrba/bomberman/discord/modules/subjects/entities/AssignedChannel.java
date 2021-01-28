package dev.vrba.bomberman.discord.modules.subjects.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
public class AssignedChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public final long id = 0;

    public final long subjectId = 0;

    public final AssignedChannelType type = AssignedChannelType.Text;

    enum AssignedChannelType {
        Text,
        Voice
    }
}

