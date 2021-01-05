package dev.vrba.pyro.discord.commands;

import dev.vrba.pyro.discord.commands.acl.ACLResolver;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandListener extends ListenerAdapter {
    public static final String COMMAND_PREFIX = "p:";

    private final List<Command> commands;

    public CommandListener(@NotNull final List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (this.commands.isEmpty() || event.getAuthor().isBot()) {
            return;
        }

        String content = event.getMessage().getContentDisplay();

        if (content.startsWith(COMMAND_PREFIX)) {
            // Parses `some-command` out of `p:some-command some arguments ping @everyone or something idk`
            String name = content.split(" ")[0].replace(COMMAND_PREFIX, "");
            Optional<Command> match = commands
                    .stream()
                    .filter(item -> item.getName().equals(name))
                    .findFirst();

            match.ifPresent(command -> executeCommand(command, createCommandContext(event)));
        }
    }

    private void executeCommand(@NotNull Command command, @NotNull CommandContext context) {
        Member member = context.getEvent().getMember();

        if (member == null) {
            return;
        }

        if (!isMemberEligibleToExecute(member, command)) {
            String reason = command.getExecutionPolicy() == Command.ExecutionSecurityPolicy.AdminsOnly
                    ? "This command can be only used by administrators."
                    : "Access was denied by this command's ACL.";

            CommandUtils.sendError(
                    context,
                    "Permissions error.",
                    "You are not eligible to run this command.\n" + reason + "\n" +
                         "If you think this is an error, please open an issue on Github:\n" +
                         "https://github.com/jirkavrba/pyro/issues/new"
            );
            return;
        }

        command.execute(context);
    }

    private boolean isMemberEligibleToExecute(@NotNull Member member, @NotNull Command command) {
        // If user is an admin (has MANAGER_SERVER permission) automatically grant execution permission
        if (member.isOwner() || member.hasPermission(Permission.MANAGE_SERVER)) {
            return true;
        }

        if (command.getExecutionPolicy() == Command.ExecutionSecurityPolicy.DeterminedByACL) {
            return new ACLResolver().isAuthorized(member, command);
        }

        return false;
    }

    private CommandContext createCommandContext(@NotNull GuildMessageReceivedEvent event) {
        return new CommandContext() {
            @Override
            public @NotNull TextChannel getChannel() {
                return event.getChannel();
            }

            @Override
            public @NotNull Message getMessage() {
                return event.getMessage();
            }

            @Override
            public @NotNull User getAuthor() {
                return event.getAuthor();
            }

            @Override
            public @NotNull GuildMessageReceivedEvent getEvent() {
                return event;
            }

            @Override
            public @NotNull List<String> getArguments() {
                 String[] parts = event.getMessage().getContentDisplay().split(" ");

                 return Arrays.stream(parts)
                         .skip(1) // The actual command invocation
                         .collect(Collectors.toList());
            }

            @Override
            public @NotNull List<User> getMentionedUsers() {
                return event.getMessage().getMentionedUsers();
            }

            @Override
            public @NotNull List<Role> getMentionedRoles() {
                return event.getMessage().getMentionedRoles();
            }

            @Override
            public @NotNull List<TextChannel> getMentionedTextChannels() {
                return event.getMessage().getMentionedChannels();
            }
        };
    }
}
