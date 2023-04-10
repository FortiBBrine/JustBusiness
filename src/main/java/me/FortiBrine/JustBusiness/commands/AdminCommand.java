package me.FortiBrine.JustBusiness.commands;

import me.FortiBrine.JustBusiness.JustBusiness;
import me.FortiBrine.JustBusiness.utils.Business;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Consumer;

public class AdminCommand implements CommandExecutor {

    private JustBusiness plugin;
    public AdminCommand() {
        plugin = JustBusiness.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        FileConfiguration config = plugin.getConfig();

        if (!sender.hasPermission(plugin.getDescription().getPermissions().get(2))) {
            config.getStringList("messages.admin.permission").forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    sender.sendMessage(s);
                }
            });
            return true;
        }

        if (args.length < 1) {
            config.getStringList("messages.admin.usage").forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    sender.sendMessage(s);
                }
            });
            return true;
        }

        if (!Arrays.asList("create", "delete", "remove", "give", "set").contains(args[0].toLowerCase())) {
            config.getStringList("messages.admin.usage").forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    sender.sendMessage(s);
                }
            });
            return true;
        }

        if (!(sender instanceof Player) && args[0].equalsIgnoreCase("create")) {
            config.getStringList("messages.admin.non_player_command").forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    sender.sendMessage(s);
                }
            });
            return true;
        }

        if (args[0].equalsIgnoreCase("create") && args.length >= 4) {
            Player player = (Player) sender;
            Location location = player.getLocation();
            String id = args[1];
            BigInteger cost = new BigInteger(args[2]);
            BigInteger dd = new BigInteger(args[3]);

            Business business = new Business(location, cost, dd);

            plugin.uploadBusiness(id, business);

            config.getStringList("messages.admin.create").forEach((String message) -> {
                message = message.replace("%business", args[1]);
                message = message.replace("%cost", args[2]);
                message = message.replace("%dd", args[3]);

                player.sendMessage(message);
            });

            return true;

        }

        if (args[0].equalsIgnoreCase("give") && args.length >= 3) {
            String id = args[2];

            plugin.uploadUser(args[1], id);

            config.getStringList("messages.admin.give").forEach((String message) -> {
                message = message.replace("%business", args[2]);
                message = message.replace("%player", args[1]);

                sender.sendMessage(message);
            });

            return true;
        }

        if (args[0].equalsIgnoreCase("remove") && args.length >= 3) {
            String id = args[2];

            plugin.removeUserBusiness(args[1], id);

            config.getStringList("messages.admin.remove").forEach((String message) -> {
                message = message.replace("%business", args[2]);
                message = message.replace("%player", args[1]);

                sender.sendMessage(message);
            });

            return true;
        }

        if (args[0].equalsIgnoreCase("delete") && args.length >= 2) {

            String id = args[1];

            plugin.deleteBusiness(id);

            config.getStringList("messages.admin.delete").forEach((String message) -> {
                message = message.replace("%business", args[1]);

                sender.sendMessage(message);
            });

            return true;
        }

        if (args[0].equalsIgnoreCase("set") && args.length >= 4) {

            String id = args[1];
            BigInteger cost = new BigInteger(args[2]);
            BigInteger dd = new BigInteger(args[3]);

            plugin.setBusiness(id, cost, dd);

            config.getStringList("messages.admin.set").forEach((String message) -> {
                message = message.replace("%business", args[1]);
                message = message.replace("%cost", args[2]);
                message = message.replace("%dd", args[3]);

                sender.sendMessage(message);
            });

            return true;
        }

        config.getStringList("messages.admin.usage").forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                sender.sendMessage(s);
            }
        });

        return true;
    }

}
