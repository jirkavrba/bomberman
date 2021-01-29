package dev.vrba.bomberman.discord.modules.subjects.commands;

import dev.vrba.bomberman.discord.commands.*;
import dev.vrba.bomberman.discord.modules.subjects.entities.Subject;
import dev.vrba.bomberman.discord.modules.subjects.repositories.SubjectRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@ExcludeFromHelpListing
public class AddSubjectCommand implements Command {

    @NotNull
    private final SubjectRepository repository;

    @Autowired
    public AddSubjectCommand(@NotNull SubjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public @NotNull String getName() {
        return "subject:add";
    }

    @Override
    public @NotNull String getDescription() {
        return "Adds a new subject to the database of a given guild";
    }

    @Override
    public @NotNull String getHelp() {
        return CommandListener.COMMAND_PREFIX + getName() + " <role_id> <subject_code> <subject_name>";
    }

    @Override
    public @NotNull Command.ExecutionSecurityPolicy getExecutionPolicy() {
        return ExecutionSecurityPolicy.AdminsOnly;
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        final Guild guild = context.getEvent().getGuild();
        final List<String> arguments = context.getArguments();

        if (arguments.size() < 3) {
            CommandUtils.sendError(
                    context,
                    "Invalid number of arguments.",
                    "Expected arguments as following:\n" +
                            "1. ID of the role for given subject\n" +
                            "2. Subject code (eg. 4IZ110)\n" +
                            "3. Subject name (eg. Informační a komunikační technologie)"
            );
            return;
        }

        final Role role = guild.getRoleById(arguments.get(0));

        if (role == null) {
            CommandUtils.sendError(
                    context,
                    "Cannot find role with ID " + arguments.get(0)
            );
            return;
        }

        try {
            final Subject subject = repository.save(new Subject(
                    0,
                    guild.getIdLong(),
                    arguments.get(1),
                    arguments.stream().skip(2).collect(Collectors.joining(" ")),
                    role.getIdLong()
            ));

            CommandUtils.sendSuccess(
                    context,
                    "Added `" + subject.name + "` (" + subject.code + ")",
                    "Subject is bound to role: " + "<@&" + subject.associatedRoleId + ">"
            );
        }
        catch (DataIntegrityViolationException exception) {
            CommandUtils.sendError(
                    context,
                    "There is already a subject with code " + arguments.get(1) + " registered in this guild."
            );
        }
    }
}
