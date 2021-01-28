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

@Component
public class ResetCommand implements Command {

    private final ACLEntriesRepository repository;

    @Autowired
    public ResetCommand(@NotNull final ACLEntriesRepository repository) {
        this.repository = repository;
    }

    @Override
    public @NotNull String getName() {
        return "acl:reset";
    }

    @Override
    public @NotNull String getDescription() {
        return "Reset all ACL permissions for the given command";
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
                            "For example: `p:acl:reste ping`"
            );
            return;
        }

        long guildId = context.getEvent().getGuild().getIdLong();
        final String name = context.getArguments().get(0);

        final List<ACLEntry> deleted = repository.deleteAllByGuildIdAndCommand(guildId, name);

        CommandUtils.sendSuccess(
                context,
                "ACL entries for `" + CommandListener.COMMAND_PREFIX + name + "` deleted.",
                "Removed " + deleted.size() + " ACL entries."

        );
    }
}
