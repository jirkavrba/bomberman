package dev.vrba.pyro.discord.modules;

import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Component;

@Component
public interface PyroModule {
    void register(JDA api);
}
