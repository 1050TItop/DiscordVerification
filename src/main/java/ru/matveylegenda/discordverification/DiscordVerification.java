package ru.matveylegenda.discordverification;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public final class DiscordVerification extends JavaPlugin {
    private JDA jda;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("discordverification").setExecutor(this);
        String token = getConfig().getString("discord.token");
        try {
            jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(new DiscordListener(this))
                    .build();
        } catch (Exception e) {
            getLogger().severe("§4Неверный токен, введите его в конфиге!");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            sender.sendMessage(getConfig().getString("minecraft.reload").replace("&", "§"));
            return true;
        } else {
            sender.sendMessage("§bКоманды:");
            sender.sendMessage("§b/dv reload §7- §fПерезагрузить конфиг");
            sender.sendMessage("§b/dv send §7- §fОтправить сообщение в канал с верификацией");
            return true;
        }
    }
}