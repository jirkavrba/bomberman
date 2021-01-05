package dev.vrba.pyro.discord.commands.acl;

import dev.vrba.pyro.discord.commands.Command;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class ACLResolver {
    public boolean isAuthorized(@NotNull Member member, @NotNull Command command) {
        String name = command.getName();

        // TODO: Load entries from JPA repository, perform actual resolution
        return false;
    }
}
