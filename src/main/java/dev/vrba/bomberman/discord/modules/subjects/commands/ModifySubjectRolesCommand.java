package dev.vrba.bomberman.discord.modules.subjects.commands;

import dev.vrba.bomberman.discord.commands.Command;
import dev.vrba.bomberman.discord.commands.CommandContext;
import dev.vrba.bomberman.discord.commands.CommandListener;
import dev.vrba.bomberman.discord.commands.CommandUtils;
import dev.vrba.bomberman.discord.modules.subjects.entities.Subject;
import dev.vrba.bomberman.discord.modules.subjects.repositories.SubjectRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ModifySubjectRolesCommand implements Command {

    private final SubjectRepository repository;

    @Autowired
    public ModifySubjectRolesCommand(@NotNull final SubjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public @NotNull String getName() {
        return "subject";
    }

    @Override
    public @NotNull String getDescription() {
        return "Modifies users' subject roles, practically allowing all users to self-assign roles in a pretty comfortable way.";
    }

    @Override
    public @NotNull String getHelp() {
        return "Use `" + CommandListener.COMMAND_PREFIX + getName() + " +<subject_code> or -<subject_code>` to gain roles.\n" +
                "For example: `" + CommandListener.COMMAND_PREFIX + getName() + " +4mm106 +4iz110 -4it101`";
    }

    @Override
    public @NotNull Command.ExecutionSecurityPolicy getExecutionPolicy() {
        return ExecutionSecurityPolicy.DeterminedByACL;
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        final Guild guild = context.getEvent().getGuild();

        final long successfulAssignments = context.getArguments()
                .stream()
                .filter(argument -> argument.matches("^[-+][a-zA-Z0-9]{6}$"))
                .map(argument -> new ModificationRequest(
                        argument.charAt(0),
                        repository.findByGuildIdAndCode(guild.getIdLong(), argument.substring(1))
                ))
                .map(request -> request.subject
                        .map(subject -> handleModificationRequest(request.operator, subject, context))
                        .orElse(false)
                )
                .filter(result -> result)
                .count();

        if (successfulAssignments < context.getArguments().size()) {
            CommandUtils.sendError(
                    context,
                    "Finished only " + successfulAssignments + " role modifications."
            );
            return;
        }

        CommandUtils.sendSuccess(
                context,
                "Finished all " + successfulAssignments + " role modifications"
        );
    }

    @Data
    @AllArgsConstructor
    private static class ModificationRequest {
        final char operator;

        @NotNull
        final Optional<Subject> subject;
    }

    private boolean handleModificationRequest(
            final char operator,
            @NotNull final Subject subject,
            @NotNull final CommandContext context
    ) {
        final Guild guild = context.getEvent().getGuild();
        final Role role = guild.getRoleById(subject.associatedRoleId);

        if (role == null) {
            return false;
        }


        if (operator == '+') {
            guild.addRoleToMember(context.getAuthor().getId(), role).queue();
        }
        else {
            guild.removeRoleFromMember(context.getAuthor().getId(), role).queue();
        }

        return true;
    }
}
