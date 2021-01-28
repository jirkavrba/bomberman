package dev.vrba.bomberman.discord.modules.subjects.repositories;

import dev.vrba.bomberman.discord.modules.subjects.entities.Subject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {
    List<Subject> findAllByGuildId(long guildId);
    Optional<Subject> findByGuildIdAndCode(long guildId, String code);
}
