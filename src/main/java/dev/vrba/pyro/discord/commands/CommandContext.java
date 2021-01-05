package dev.vrba.pyro.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CommandContext {
    @NotNull TextChannel getChannel();
    @NotNull Message getMessage();
    @NotNull User getAuthor();
    @NotNull GuildMessageReceivedEvent getEvent();

    @NotNull List<String> getArguments();

    @NotNull List<User> getMentionedUsers();
    @NotNull List<Role> getMentionedRoles();
    @NotNull List<TextChannel> getMentionedTextChannels();
}
