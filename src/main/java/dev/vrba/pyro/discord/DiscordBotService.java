package dev.vrba.pyro.discord;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

@Service
public class DiscordBotService {

    private final String token;

    private final Logger logger = Logger.getAnonymousLogger();

    @Autowired
    public DiscordBotService(@Value("${DISCORD_TOKEN:default}") final String token) {
        this.token = token;
    }

    public void start() throws LoginException {
        logger.info("Starting the Pyro Discord bot service.");

        JDABuilder.createDefault(token)
                .addEventListeners(new ModulesLoader())
                .build();
    }

    private class ModulesLoader extends ListenerAdapter {
        @Override
        public void onReady(@NotNull ReadyEvent event) {
            logger.info("Logged into Discord Gateway.");
            logger.info("Loading Pyro modules.");
        }
    }
}
