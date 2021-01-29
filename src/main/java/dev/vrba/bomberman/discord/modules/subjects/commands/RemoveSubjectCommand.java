package dev.vrba.bomberman.discord.modules.subjects.commands;

import dev.vrba.bomberman.discord.commands.*;
import dev.vrba.bomberman.discord.modules.subjects.entities.Subject;
import dev.vrba.bomberman.discord.modules.subjects.repositories.SubjectRepository;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ExcludeFromHelpListing
public class RemoveSubjectCommand implements Command {

    @NotNull
    private final SubjectRepository repository;

    public RemoveSubjectCommand(@NotNull final SubjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public @NotNull String getName() {
        return "subject:remove";
    }

    @Override
    public @NotNull String getDescription() {
        return "Removes a specific subject and all associations.";
    }

    @Override
    public @NotNull String getHelp() {
        return CommandListener.COMMAND_PREFIX + getName() + " <subject_code>";
    }

    @Override
    public @NotNull Command.ExecutionSecurityPolicy getExecutionPolicy() {
        return ExecutionSecurityPolicy.AdminsOnly;
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        if (context.getArguments().size() != 1) {
            CommandUtils.sendError(
                    context,
                    "Missing subject code!"
            );
            return;
        }

        final Guild guild = context.getEvent().getGuild();
        final String code = context.getArguments().get(0);
        final Optional<Subject> match = repository.findByGuildIdAndCode(guild.getIdLong(), code);

        if (match.isEmpty()) {
            CommandUtils.sendError(
                    context,
                    "No matches for subject with code " + code + " for this guild!"
            );
            return;
        }

        Subject subject = match.get();

        repository.deleteById(subject.id);
        CommandUtils.sendSuccess(
                context,
                "Deleted associations for subject " + code
        );
    }
}
