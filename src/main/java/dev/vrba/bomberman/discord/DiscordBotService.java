package dev.vrba.bomberman.discord;

import dev.vrba.bomberman.discord.modules.BombermanModule;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.logging.Logger;

@Service
public class DiscordBotService {

    private final String token;

    private final Logger logger = Logger.getAnonymousLogger();

    private final List<BombermanModule> modules;

    @Autowired
    public DiscordBotService(@Value("${DISCORD_TOKEN:default}") final String token, final List<BombermanModule> modules) {
        this.token = token;
        this.modules = modules;
    }

    public void start() throws LoginException {
        logger.info("Starting the Bomberman Discord bot service.");

        JDABuilder.createDefault(token)
                .addEventListeners(new ModuleLoader())
                .build();
    }

    private class ModuleLoader extends ListenerAdapter {
        @Override
        public void onReady(@NotNull ReadyEvent event) {
            logger.info("Logged into Discord Gateway.");
            logger.info("Loading Bomberman modules.");

            modules.forEach(module -> {
                logger.info("Registering the module [" + module.getClass().getName() + "]");
                module.register(event.getJDA());
            });
        }
    }
}
