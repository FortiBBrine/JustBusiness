package me.FortiBrine.JustBusiness;

import me.FortiBrine.JustBusiness.commands.AdminCommand;
import me.FortiBrine.JustBusiness.listeners.Listener;
import me.FortiBrine.JustBusiness.utils.Business;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JustBusiness extends JavaPlugin {

    private Inventory inv;
    private Map<Player, Business> buying = new HashMap<>();
    private Map<String, Business> businesses = new HashMap<>();

    private Map<Player, List<Business>> own;

    private static JustBusiness instance;

    public static JustBusiness getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        File config = new File(this.getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
        }

        this.loadBusinesses();
        this.loadBusiness();

        Bukkit.getPluginManager().registerEvents(new Listener(), this);

        this.getCommand("justbusiness").setExecutor(new AdminCommand());

    }

    public void loadBusinesses() {
        FileConfiguration config = this.getConfig();
        ConfigurationSection businessOwn = config.getConfigurationSection("business_own");

        this.own = new HashMap<>();

        for (String playerName : businessOwn.getKeys(false)) {
            Player player = Bukkit.getPlayer(playerName);

            List<String> businesses = businessOwn.getStringList(playerName);

            List<Business> result = new ArrayList<>();

            for (String business : businesses) {
                result.add(this.getBusiness(business));
            }

            own.put(player, result);

        }

    }

    public void loadBusiness() {
        FileConfiguration config = this.getConfig();
        ConfigurationSection configurationSection = config.getConfigurationSection("businesses");

        businesses = new HashMap<>();

        for (String business : configurationSection.getKeys(false)) {
            businesses.put(business, this.getBusiness(business));
        }

    }

    public void removeUserBusiness(Player player, String name) {
        FileConfiguration config = this.getConfig();

        List<String> b = config.getStringList("business_own");

        b.remove(name);

        this.saveConfig();
        this.reloadConfig();
        this.loadBusinesses();
    }

    public void deleteBusiness(String name) {
        FileConfiguration config = this.getConfig();

        config.set("businesses." + name, null);

        this.saveConfig();
        this.reloadConfig();
        this.loadBusiness();
    }

    // Later I fix it
    public void setBusiness(String name, BigInteger cost, BigInteger dd) {
        Business business = businesses.get(name);

        business.setCost(cost);
        business.setDD(dd);

        businesses.put(name, business);

        this.uploadBusiness(name, business);
    }

    public void uploadUser(Player player, String name) {
        FileConfiguration config = this.getConfig();
        String userName = player.getName();

        List<String> b = config.getStringList("business_own." + userName);

        b.add(name);

        config.set("business_own." + userName, b);

        this.saveConfig();
        this.reloadConfig();
        this.loadBusinesses();
    }

    public void uploadBusiness(String name, Business business) {
        FileConfiguration config = this.getConfig();

        Location location = business.getLocation();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        String world = location.getWorld().getName();

        BigInteger cost = business.getCost();
        BigInteger dd = business.getDD();

        config.set("businesses." + name + ".cost.", cost.toString());
        config.set("businesses." + name + ".dd", dd.toString());
        config.set("businesses." + name + ".x", (int) x);
        config.set("businesses." + name + ".y", (int) y);
        config.set("businesses." + name + ".z", (int) z);
        config.set("businesses." + name + ".world", world);

        this.saveConfig();
        this.reloadConfig();
        this.loadBusiness();

    }

    // LOL it's bad
    public Business getBusiness(String name) {
        FileConfiguration config = this.getConfig();
        ConfigurationSection businesses = config.getConfigurationSection("businesses");

        String world = businesses.getString(name+".world", "world");

        int x = businesses.getInt(name+".x", 0);
        int y = businesses.getInt(name+".y", 0);
        int z = businesses.getInt(name+".z", 0);

        BigInteger dd = new BigInteger(businesses.getString(name+".dd"));
        BigInteger cost = new BigInteger(businesses.getString(name+".cost"));

        Location location = new Location(Bukkit.getWorld(world), x, y, z);

        return new Business(location, cost, dd);
    }

}
