package me.FortiBrine.JustBusiness.utils;

import org.bukkit.Location;

import java.math.BigInteger;

public class Business {

    private Location location;
    private BigInteger cost;
    private BigInteger dd;

    public Business(Location location, BigInteger cost, BigInteger dd) {
        this.location = location;
        this.cost = cost;
        this.dd = dd;
    }

    public Location getLocation() {
        return this.location;
    }

    public BigInteger getCost() {
        return this.cost;
    }

    public BigInteger getDD() {
        return this.dd;
    }

}
