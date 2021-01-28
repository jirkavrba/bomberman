package dev.vrba.bomberman.discord.commands.acl;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ACLEntriesRepository extends CrudRepository<ACLEntry, Long> {
    List<ACLEntry> findAllByGuildIdAndCommandAndType(long guildId, @NotNull String command, @NotNull ACLEntry.EntryType type);
    @Transactional
    List<ACLEntry> deleteAllByGuildIdAndCommand(long guildId, @NotNull String command);
}
