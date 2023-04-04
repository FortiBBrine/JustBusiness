package me.FortiBrine.JustBusiness;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JustBusiness extends JavaPlugin {

    private Inventory inv;
    private Map<Player, Business> buying = new HashMap<>();

    private Map<Player, Business> own;

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

        Bukkit.getPluginManager().registerEvents(new Listener(), this);

    }

    public void loadBusinesses() {
        FileConfiguration config = this.getConfig();
        ConfigurationSection businessOwn = config.getConfigurationSection("business_own");

        this.own = new HashMap<>();

        for (String playerName : businessOwn.getKeys(false)) {
            Player player = Bukkit.getPlayer(playerName);

            List<String> businesses = businessOwn.getStringList(playerName);

            for (String business : businesses) {
                own.put(player, this.getBusiness(business));
            }

        }

    }

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
