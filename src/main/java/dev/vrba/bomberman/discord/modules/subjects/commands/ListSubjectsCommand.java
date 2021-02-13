package dev.vrba.bomberman.discord.modules.subjects.commands;

import dev.vrba.bomberman.discord.commands.Command;
import dev.vrba.bomberman.discord.commands.CommandContext;
import dev.vrba.bomberman.discord.commands.CommandListener;
import dev.vrba.bomberman.discord.commands.CommandUtils;
import dev.vrba.bomberman.discord.modules.subjects.entities.Subject;
import dev.vrba.bomberman.discord.modules.subjects.repositories.SubjectRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListSubjectsCommand implements Command {

    private final SubjectRepository repository;

    public ListSubjectsCommand(SubjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public @NotNull String getName() {
        return "subject:list";
    }

    @Override
    public @NotNull String getDescription() {
        return "Lists all subjects registered for the given guild.";
    }

    @Override
    public @NotNull String getHelp() {
        return CommandListener.COMMAND_PREFIX + getName();
    }

    @Override
    public @NotNull Command.ExecutionSecurityPolicy getExecutionPolicy() {
        return ExecutionSecurityPolicy.DeterminedByACL;
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        List<Subject> subjects = repository.findAllByGuildId(context.getGuild().getIdLong());

        String body = subjects.stream()
                .map(subject ->
                        "**" + subject.getCode() + "**\n" +
                        subject.getName() + "\n" + "<@&" + subject.getAssociatedRoleId() + ">\n")
                .collect(Collectors.joining("\n"));

        CommandUtils.sendSuccess(context, "Subjects", body);
    }
}
