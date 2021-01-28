package dev.vrba.bomberman.discord.commands;

import org.jetbrains.annotations.NotNull;

public interface Command {
    @NotNull String getName();

    default @NotNull String getDescription() {
        return "No description provided";
    }

    default @NotNull String getHelp() {
        return "No help provided";
    }

    default @NotNull Command.ExecutionSecurityPolicy getExecutionPolicy() {
        return ExecutionSecurityPolicy.AdminsOnly;
    }

    void execute(@NotNull CommandContext context);

    enum ExecutionSecurityPolicy {
        AdminsOnly,
        DeterminedByACL
    }
}
