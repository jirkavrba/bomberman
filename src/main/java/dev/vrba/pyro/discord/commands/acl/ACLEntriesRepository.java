package dev.vrba.pyro.discord.commands.acl;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ACLEntriesRepository extends CrudRepository<ACLEntry, Long> {
    List<ACLEntry> findAllByGuildIdAndCommand(long guildId, String command);
    List<ACLEntry> findAllByGuildIdAndCommandAndType(long guildId, String command, ACLEntry.EntryType type);
}
