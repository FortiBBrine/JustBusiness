package me.FortiBrine.JustBusiness.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class Listener implements org.bukkit.event.Listener {

    @EventHandler
    public void onShift(PlayerToggleSneakEvent event) {
        Player player = (Player) event;
    }

}
