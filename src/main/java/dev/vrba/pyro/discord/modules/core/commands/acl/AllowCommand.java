package dev.vrba.pyro.discord.modules.core.commands.acl;

import dev.vrba.pyro.discord.commands.Command;
import dev.vrba.pyro.discord.commands.CommandContext;
import dev.vrba.pyro.discord.commands.CommandListener;
import dev.vrba.pyro.discord.commands.CommandUtils;
import dev.vrba.pyro.discord.commands.acl.ACLEntriesRepository;
import dev.vrba.pyro.discord.commands.acl.ACLEntry;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AllowCommand implements Command {

    private final ACLEntriesRepository repository;

    @Autowired
    public AllowCommand(@NotNull final ACLEntriesRepository repository) {
        this.repository = repository;
    }

    @Override
    public @NotNull String getName() {
        return "acl:allow";
    }

    @Override
    public @NotNull String getDescription() {
        return "Allows execution of command (passed as the first argument) to a list of mentioned users / roles.";
    }

    @Override
    public @NotNull String getHelp() {
        return "Usage: " + CommandListener.COMMAND_PREFIX + getName() + " <command_name> @Role / @User...\n" +
               "If the list of roles / users is omitted, the permission is granted to everybody.";
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
                    "For example: `p:acl:allow ping @0x4a69726b61:6666`"
            );
            return;
        }

        long guildId = context.getEvent().getGuild().getIdLong();
        String name = context.getArguments().get(0);

        if (context.getMentionedUsers().isEmpty() && context.getMentionedRoles().isEmpty()) {
            ACLEntry entry = new ACLEntry(
                0,
                guildId,
                0,
                ACLEntry.EntryType.Allow,
                ACLEntry.TargetType.Everyone,
                name
            );


            repository.save(entry);

            CommandUtils.sendSuccess(
                context,
                "ACL entry added.",
                "Allowed execution of `" + CommandListener.COMMAND_PREFIX + name + "` for everyone."
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
                "Allowed execution of `" + CommandListener.COMMAND_PREFIX + name + "` for mentioned users and roles."
        );
    }

    private ACLEntry createUserEntry(final long guildId, final long userId, @NotNull final String command) {
        return new ACLEntry(
            0,
            guildId,
            userId,
            ACLEntry.EntryType.Allow,
            ACLEntry.TargetType.User,
            command
        );
    }

    private ACLEntry createRoleEntry(final long guildId, final long roleId, @NotNull final String command) {
        return new ACLEntry(
            0,
            guildId,
            roleId,
            ACLEntry.EntryType.Allow,
            ACLEntry.TargetType.Role,
            command
        );
    }
}
