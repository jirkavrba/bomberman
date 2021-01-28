package dev.vrba.pyro.discord.modules.core.commands.acl;

import dev.vrba.pyro.discord.commands.Command;
import dev.vrba.pyro.discord.commands.acl.ACLEntriesRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ACLCommand implements Command {
    protected final ACLEntriesRepository repository;

    protected final ApplicationContext context;

    @Nullable
    protected List<String> validCommands = null;

    protected ACLCommand(@NotNull final ACLEntriesRepository repository, @NotNull final ApplicationContext context) {
        this.repository = repository;
        this.context = context;
    }

    @Override
    public @NotNull Command.ExecutionSecurityPolicy getExecutionPolicy() {
        return Command.ExecutionSecurityPolicy.AdminsOnly;
    }

    protected boolean isTargetCommandValid(@NotNull String name) {
        if (this.validCommands == null) {
            this.validCommands = context.getBeansOfType(Command.class)
                    .values()
                    .stream()
                    .filter(command -> command.getExecutionPolicy() == ExecutionSecurityPolicy.DeterminedByACL)
                    .map(Command::getName)
                    .collect(Collectors.toList());
        }

        return validCommands.contains(name);
    }
}

