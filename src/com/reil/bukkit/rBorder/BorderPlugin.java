package com.reil.bukkit.rBorder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

public class BorderPlugin extends JavaPlugin{
	Logger log = Logger.getLogger("Minecraft");
	File Folder;
	String BorderAlert;
	Properties Props = new Properties();
	HashMap <World, ArrayList<Border>>Borders = new HashMap <World, ArrayList<Border>>();
	rBorderListener Listener = new rBorderListener(this);
	rBorderVehicleListener Listener2 = new rBorderVehicleListener(this);
	
	/* If a player has an X and Z between +-DefiniteSquare, there is no need to calculate distance. */	
	
	public void onEnable(){
		Server MCServer = getServer(); 
		PluginManager loader = MCServer.getPluginManager();
		loader.registerEvent(Event.Type.PLAYER_MOVE, Listener, Event.Priority.Highest, this);
		loader.registerEvent(Event.Type.PLAYER_TELEPORT, Listener, Event.Priority.Highest, this);
		loader.registerEvent(Event.Type.PLAYER_JOIN, Listener, Event.Priority.Highest, this);
		loader.registerEvent(Event.Type.VEHICLE_MOVE, Listener2, Event.Priority.Highest, this);

		Configuration config = getConfiguration();
		config.load();
		Map<String, ConfigurationNode> nodes = config.getNodes("borders");
		for(ConfigurationNode border : nodes.values()){ 
			World thisWorld = MCServer.getWorld(border.getString("world"));
			Location spawn = thisWorld.getSpawnLocation();
			Border newBorder = new Border(  border.getDouble("x"     , spawn.getX() ),
											border.getDouble("z"     , spawn.getZ() ),
											border.getDouble("radius", 1400)        );
			log.info("Border loaded:" + newBorder);
			if (!Borders.containsKey(thisWorld)){
				ArrayList <Border> newArray = new ArrayList<Border>();
				Borders.put(thisWorld, newArray);
			}
			Borders.get(thisWorld).add(newBorder);
		}
		BorderAlert = config.getString("borderalert");
		log.info("[rBorders] Loaded!");
	}

	public void onDisable() {
		
	}
	public boolean inBorder(Location checkHere) {
		// If there aren't borders, we assume it's a limitless map.
		if (!Borders.containsKey(checkHere.getWorld()))
			return true;
		for (Border amIHere : Borders.get(checkHere.getWorld())){
			int X = (int) Math.abs(amIHere.centerX - checkHere.getBlockX());
			int Z = (int) Math.abs(amIHere.centerZ - checkHere.getBlockZ());
			// If statements are cheaper than squaring twice!
			// Definitely in the circle
			if (X < amIHere.definiteSq && Z < amIHere.definiteSq)
				return true;
			// Definitely not in the circle?
			if (X > amIHere.radius || Z > amIHere.radius)
				continue;
			// Must know for sure.
			if ( X*X + Z*Z < amIHere.radiusSq )
				return true;
		}
		return false;
	}

}
