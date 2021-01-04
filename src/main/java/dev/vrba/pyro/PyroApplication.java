package dev.vrba.pyro;

import dev.vrba.pyro.discord.DiscordBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

@SpringBootApplication
public class PyroApplication implements CommandLineRunner {

    private final DiscordBotService bot;

    private final Logger logger = Logger.getAnonymousLogger();

    @Autowired
    public PyroApplication(final DiscordBotService bot) {
        this.bot = bot;
    }

    public static void main(String[] args) {
        SpringApplication.run(PyroApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try { this.bot.start(); }
        catch (LoginException exception) {
            logger.severe(
            "Cannot start the application!\n" +
                "The provided DISCORD_TOKEN is invalid and cannot be used to login within the gateway."
            );
        }
    }
}
