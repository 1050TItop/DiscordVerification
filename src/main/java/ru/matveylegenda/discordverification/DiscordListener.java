package ru.matveylegenda.discordverification;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DiscordListener extends ListenerAdapter {
    private final JavaPlugin plugin;

    public DiscordListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getId().equals(plugin.getConfig().getString("discord.channel_id"))) {
            Message message = event.getMessage();
            String content = message.getContentRaw();

            if (content.split(" ").length == 1) {
                Role role = event.getGuild().getRoleById(plugin.getConfig().getString("discord.role_id"));
                Member member = event.getMember();
                event.getGuild().addRoleToMember(member, role).queue();

                String command = plugin.getConfig().getString("minecraft.command").replace("%player%", content);
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command));

                User user = event.getAuthor();
                user.openPrivateChannel().queue(channel ->
                        channel.sendMessage(plugin.getConfig().getString("discord.success_message")).queue());
                message.delete().queue();
            } else {
                User user = event.getAuthor();
                user.openPrivateChannel().queue(channel ->
                        channel.sendMessage(plugin.getConfig().getString("discord.error_message")).queue());
                message.delete().queue();
            }
        }
    }
}