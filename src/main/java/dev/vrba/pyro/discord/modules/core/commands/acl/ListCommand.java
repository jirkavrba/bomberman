package dev.vrba.pyro.discord.modules.core.commands.acl;

import dev.vrba.pyro.discord.commands.*;
import dev.vrba.pyro.discord.commands.acl.ACLEntriesRepository;
import dev.vrba.pyro.discord.commands.acl.ACLEntry;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@AutowiredCommand
public class ListCommand implements Command {

    private final ACLEntriesRepository repository;

    @Autowired
    public ListCommand(@NotNull final ACLEntriesRepository repository) {
        this.repository = repository;
    }

    @Override
    public @NotNull String getName() {
        return "acl:list";
    }

    @Override
    public @NotNull String getDescription() {
        return "Lists all entries of a specified command";
    }

    @Override
    public @NotNull String getHelp() {
        return "Usage: " + CommandListener.COMMAND_PREFIX + getName() + " <command_name>";
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
                            "For example: `p:acl:list ping`"
            );
            return;
        }

        long guildId = context.getEvent().getGuild().getIdLong();
        String name = context.getArguments().get(0);

        final List<ACLEntry> allows = repository.findAllByGuildIdAndCommandAndType(guildId, name, ACLEntry.EntryType.Allow);
        final List<ACLEntry> denials = repository.findAllByGuildIdAndCommandAndType(guildId, name, ACLEntry.EntryType.Deny);

        CommandUtils.sendSuccess(
                context,
                "ACL configuration for `" + CommandListener.COMMAND_PREFIX + name + "`",
                "**Allowed** for:\n" + permissionsMentions(allows) + "\n" +
                     "**Denied** for:\n" + permissionsMentions(denials)

        );
    }

    private @NotNull String permissionsMentions(List<ACLEntry> entries) {
        final Stream<ACLEntry> roles = entries.stream().filter(entry -> entry.targetType == ACLEntry.TargetType.Role);
        final Stream<ACLEntry> users = entries.stream().filter(entry -> entry.targetType == ACLEntry.TargetType.User);

        final StringBuilder builder = new StringBuilder();
        final boolean containsEveryone =  entries.stream().anyMatch(entry -> entry.targetType == ACLEntry.TargetType.Everyone);

        if (containsEveryone) {
            builder.append("@everyone\n");
        }

        roles.map(role -> "<@&" + role.targetId + ">").forEach(mention -> builder.append(mention).append("\n"));
        users.map(user -> "<@" + user.targetId + ">").forEach(mention -> builder.append(mention).append("\n"));

        return builder.toString();
    }
}
