package nl.lucasridder.core;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class core extends JavaPlugin implements Listener, PluginMessageListener {

    boolean lock;
    String lockreason;
    HashMap<Player, String> PlayerBoolean = new HashMap<Player, String>();

    //send server
    public void sendServer(String server, Player player) {
        player.sendMessage(ChatColor.DARK_GRAY + "Je wordt nu doorverbonden naar: " + ChatColor.GOLD + server);
        //BUNGEE
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(this, "BungeeCord", b.toByteArray());
        PlayerBoolean.put(player, server);
    }

    //Start-up
    @Override
    public void onEnable() {
        //Bungeecord channel
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        //register events
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        //config
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();

        //enable complete
        System.out.println(ChatColor.GRAY + "Survival plugin by LucasRidder " + ChatColor.GREEN + "Enabled");
    }

    //Chat
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(getConfig().getBoolean("chatlistener")) {
            Player player = e.getPlayer();
            String name = e.getPlayer().getName();
            String message = e.getMessage();
            if (player.isOp()) {
                e.setFormat(ChatColor.GOLD + name + ChatColor.DARK_GRAY + " >> " + ChatColor.RESET + message);
            } else {
                e.setFormat(ChatColor.GRAY + name + ChatColor.DARK_GRAY + " >> " + ChatColor.RESET + message);
            }
        }
    }

    //Listener bungee
    @SuppressWarnings("NullableProblems")
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            if(getConfig().getBoolean("bungeelistener")) {
                System.out.println(Arrays.toString(message));
            }
        }
    }
}
