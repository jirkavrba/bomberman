package dev.vrba.bomberman.discord.modules.subjects.commands;

import dev.vrba.bomberman.discord.commands.Command;
import dev.vrba.bomberman.discord.commands.CommandContext;
import dev.vrba.bomberman.discord.commands.CommandListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModifySubjectRolesCommand implements Command {



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
                "For example: `" + CommandListener.COMMAND_PREFIX + getName() + " +4mm106 +4iz110 -4it101";
    }

    @Override
    public @NotNull Command.ExecutionSecurityPolicy getExecutionPolicy() {
        return ExecutionSecurityPolicy.DeterminedByACL;
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        final List<String> subjects = context.getArguments();
    }
}
