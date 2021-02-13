package dev.vrba.bomberman.discord.modules.subjects.commands;

import dev.vrba.bomberman.discord.commands.Command;
import dev.vrba.bomberman.discord.commands.CommandContext;
import dev.vrba.bomberman.discord.commands.CommandListener;
import dev.vrba.bomberman.discord.commands.CommandUtils;
import dev.vrba.bomberman.discord.modules.subjects.entities.Subject;
import dev.vrba.bomberman.discord.modules.subjects.repositories.SubjectRepository;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ScaffoldSubjectCommand implements Command {

    private final SubjectRepository repository;

    public ScaffoldSubjectCommand(SubjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public @NotNull String getName() {
        return "subject:scaffold";
    }

    @Override
    public @NotNull String getDescription() {
        return "Scaffolds a channel category and basic text/voice channels for a given subject";
    }

    @Override
    public @NotNull String getHelp() {
        return "Use " + CommandListener.COMMAND_PREFIX + getName() + " to scaffold a category + channels.\n" +
                "This commands also sets the correct permissions overrides.";
    }

    @Override
    public @NotNull Command.ExecutionSecurityPolicy getExecutionPolicy() {
        return ExecutionSecurityPolicy.DeterminedByACL;
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        if (context.getArguments().size() != 1) {
            CommandUtils.sendError(
                    context,
                    "Subject not specified. Cannot continue."
            );
            return;
        }

        String code = context.getArguments().get(0);
        Optional<Subject> subject = repository.findByGuildIdAndCode(
                context.getEvent().getGuild().getIdLong(),
                code
        );

        if (subject.isEmpty()) {
            CommandUtils.sendError(
                    context,
                    "Subject with code " + code + " not found."
            );
            return;
        }

        scaffold(context, subject.get());
    }

    private void scaffold(@NotNull CommandContext context, @NotNull Subject subject) {
        Role role = context.getGuild().getRoleById(subject.getAssociatedRoleId());

        if (role == null) {
            CommandUtils.sendError(
                    context,
                    "Role with ID " + subject.getAssociatedRoleId() + " not found."
            );
            return;
        }

        // Create a subject category
        Category category = context.getGuild()
                .createCategory(subject.getCode() + " " + subject.getName())
                .addRolePermissionOverride(role.getIdLong(), List.of(
                        Permission.VIEW_CHANNEL,
                        Permission.MESSAGE_READ,
                        Permission.VOICE_CONNECT,
                        Permission.VOICE_SPEAK
                ), List.of()).complete();

        // Create text channel #[subject-code]-chat
        context.getGuild()
                .createTextChannel(subject.getCode().toLowerCase() + "-chat", category)
                .queue();

        // Create text channel #[subject-code]-studijni-materialy
        context.getGuild()
                .createTextChannel(subject.getCode().toLowerCase() + "-studijni-materialy", category)
                .queue();

        // Create voice channel #[subject-code]
        context.getGuild()
                .createVoiceChannel(subject.getCode().toLowerCase(), category)
                .queue();
    }
}
