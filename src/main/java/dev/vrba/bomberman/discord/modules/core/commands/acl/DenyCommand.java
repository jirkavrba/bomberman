package dev.vrba.bomberman.discord.modules.core.commands.acl;

import dev.vrba.bomberman.discord.commands.*;
import dev.vrba.bomberman.discord.commands.acl.ACLEntriesRepository;
import dev.vrba.bomberman.discord.commands.acl.ACLEntry;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DenyCommand extends ACLCommand implements Command {

    @Autowired
    public DenyCommand(@NotNull final ACLEntriesRepository repository, @NotNull final ApplicationContext context) {
        super(repository, context);
    }

    @Override
    public @NotNull String getName() {
        return "acl:deny";
    }

    @Override
    public @NotNull String getDescription() {
        return "Denies execution of command (passed as the first argument) to a list of mentioned users / roles.";
    }

    @Override
    public @NotNull String getHelp() {
        return "Usage: " + CommandListener.COMMAND_PREFIX + getName() + " <command_name> @Role / @User...\n" +
                "If the list of roles / users is omitted, the permission is removed from everybody.";
    }

    @Override
    public @NotNull Command.ExecutionSecurityPolicy getExecutionPolicy() {
        return ExecutionSecurityPolicy.AdminsOnly;
    }

    @Override
    public void execute(@NotNull final CommandContext context) {
        if (context.getArguments().isEmpty()) {
            CommandUtils.sendError(
                    context,
                    "Missing command name.",
                    "Please, pass the command name (something like `ping`) as the first argument.\n" +
                            "For example: `p:acl:deny ping @0x4a69726b61:6666`"
            );
            return;
        }

        long guildId = context.getEvent().getGuild().getIdLong();
        String name = context.getArguments().get(0);

        if (!isTargetCommandValid(name)) {
            CommandUtils.sendError(
                    context,
                    "Unknown or invalid command `" + name + "`",
                    "Make sure the target command exists and that its execution is ACL-dependent."
            );
            return;
        }

        if (context.getMentionedUsers().isEmpty() && context.getMentionedRoles().isEmpty()) {
            ACLEntry entry = new ACLEntry(
                    0,
                    guildId,
                    0,
                    ACLEntry.EntryType.Deny,
                    ACLEntry.TargetType.Everyone,
                    name
            );


            repository.save(entry);

            CommandUtils.sendSuccess(
                    context,
                    "ACL entry added.",
                    "Denied execution of `" + CommandListener.COMMAND_PREFIX + name + "` for everyone."
            );
            return;
        }

        final List<ACLEntry> entries = context.getMentionedRoles()
                .stream()
                .map(role -> createRoleEntry(guildId, role.getIdLong(), name))
                .collect(Collectors.toList());

        entries.addAll(
                context.getMentionedUsers()
                        .stream()
                        .map(user -> createUserEntry(guildId, user.getIdLong(), name))
                        .collect(Collectors.toList())
        );

        repository.saveAll(entries);

        CommandUtils.sendSuccess(
                context,
                entries.size() > 1 ? "ACL entries added." : "ACL entry added",
                "Denied execution of `" + CommandListener.COMMAND_PREFIX + name + "` for mentioned users and roles."
        );
    }

    private ACLEntry createUserEntry(final long guildId, final long userId, @NotNull final String command) {
        return new ACLEntry(
                0,
                guildId,
                userId,
                ACLEntry.EntryType.Deny,
                ACLEntry.TargetType.User,
                command
        );
    }

    private ACLEntry createRoleEntry(final long guildId, final long roleId, @NotNull final String command) {
        return new ACLEntry(
                0,
                guildId,
                roleId,
                ACLEntry.EntryType.Deny,
                ACLEntry.TargetType.Role,
                command
        );
    }
}
