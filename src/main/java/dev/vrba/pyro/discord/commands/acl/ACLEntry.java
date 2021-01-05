package dev.vrba.pyro.discord.commands.acl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@ToString(includeFieldNames = true)
@AllArgsConstructor
public class ACLEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public long guildId;

    public long targetId;

    @NotNull
    public EntryType type;

    @NotNull
    public TargetType targetType;

    @NotNull
    public String command;

    public enum TargetType {
        Everyone,
        User,
        Role,
    }

    public enum EntryType {
        Allow,
        Deny
    }
}
