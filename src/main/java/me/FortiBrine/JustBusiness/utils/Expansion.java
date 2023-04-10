package me.FortiBrine.JustBusiness.utils;

import me.FortiBrine.JustBusiness.JustBusiness;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.math.BigInteger;
import java.util.List;

public class Expansion extends PlaceholderExpansion {
    private JustBusiness plugin;

    public Expansion() {
        this.plugin = JustBusiness.getInstance();
    }

    @Override
    public String getAuthor() {
        return "IJustFortiLive";
    }

    @Override
    public String getIdentifier() {
        return "justbusiness";
    }

    @Override
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {

        if (params.equalsIgnoreCase("dd")) {
            List<Business> b = plugin.get(player.getPlayer());

            BigInteger result = new BigInteger("0");

            for (Business bb : b) {
                result.add(bb.getDD());
            }

            return result.toString();
        }

        return null;
    }
}
