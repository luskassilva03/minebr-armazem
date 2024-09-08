package net.minebr.armazem.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationConverter {

    // Método para converter Location em String
    public static String convertLocationToString(Location loc, boolean asPlayer) {
        if (loc == null) {
            return "";
        }
        if (asPlayer) {
            return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getPitch() + "," + loc.getYaw();
        } else {
            return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
        }
    }

    // Método para converter String em Location
    public static Location convertStringToLocation(String locString) {
        if (locString == null || locString.isEmpty()) {
            return null;
        }
        String[] parts = locString.split(",");
        if (parts.length == 4) { // Sem yaw e pitch
            String world = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            return new Location(Bukkit.getServer().getWorld(world), x, y, z);
        } else if (parts.length == 6) { // Com yaw e pitch
            String world = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float pitch = Float.parseFloat(parts[4]);
            float yaw = Float.parseFloat(parts[5]);
            return new Location(Bukkit.getServer().getWorld(world), x, y, z, yaw, pitch);
        }
        return null;
    }
}
