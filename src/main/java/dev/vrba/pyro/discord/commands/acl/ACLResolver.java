package dev.vrba.pyro.discord.commands.acl;

import dev.vrba.pyro.discord.commands.Command;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ACLResolver {
    @Autowired
    private ACLEntriesRepository repository;

    public boolean isAuthorized(@NotNull Member member, @NotNull Command command) {
        long guildId = member.getGuild().getIdLong();
        String name = command.getName();

        List<ACLEntry> denials = repository.findAllByGuildIdAndCommandAndType(guildId, name, ACLEntry.EntryType.Deny);

        // DENY has a priority over ALLOW in ACL context so it is resolved first
        if (denials.stream().anyMatch(entry -> entryMatches(entry, member))) {
            return false;
        }

        List<ACLEntry> allows = repository.findAllByGuildIdAndCommandAndType(guildId, name, ACLEntry.EntryType.Allow);

        return allows
                .stream()
                .anyMatch(entry -> entryMatches(entry, member));
    }

    private boolean entryMatches(@NotNull ACLEntry entry, @NotNull Member member) {
        switch (entry.targetType) {
            case Everyone:
                return true;

            case User:
                return member.getIdLong() == entry.targetId;

            case Role:
                return member.getRoles()
                        .stream()
                        .anyMatch(role -> role.getIdLong() == entry.targetId);

            // This is just to make the Java compiler happy, shouldn't be matched at all
            default:
                return false;
        }
    }
}
