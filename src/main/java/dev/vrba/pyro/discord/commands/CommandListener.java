package dev.vrba.pyro.discord.commands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class CommandListener extends ListenerAdapter {
    private static final String COMMAND_PREFIX = "p:";

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
            // Parses `some-command` out of `p:some-command some arguments @everyone or idk`
            String name = content.split(" ")[0].replace(COMMAND_PREFIX, "");
            Optional<Command> match = commands
                    .stream()
                    .filter(item -> item.getName().equals(name))
                    .findFirst();

            match.ifPresent(command -> command.execute(createCommandContext(event)));
        }

    }

    private CommandContext createCommandContext(@NotNull GuildMessageReceivedEvent event) {
        return new CommandContext() {
            @Override
            public @NotNull TextChannel getChannel() {
                return event.getChannel();
            }

            @Override
            public @NotNull GuildMessageReceivedEvent getEvent() {
                return event;
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
