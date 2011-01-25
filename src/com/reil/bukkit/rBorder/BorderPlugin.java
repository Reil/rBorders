package com.reil.bukkit.rBorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BorderPlugin extends JavaPlugin{
	Logger log = Logger.getLogger("Minecraft");
	File Folder;
	Properties Props = new Properties();
	Location SpawnLocation;
	int BorderSizeSq;
	int BorderSize;
	String BorderAlert;
	rBorderListener Listener = new rBorderListener(this);
	
	/* If a player has an X and Z between +-DefiniteSquare, there is no need to calculate distance. */
	int DefiniteSquare;
	
	public BorderPlugin(PluginLoader pluginLoader, Server instance,
			PluginDescriptionFile desc, File folder, File plugin,
			ClassLoader cLoader) {
		super(pluginLoader, instance, desc, folder, plugin, cLoader);
		this.Folder = folder;
	}
	
	public void onEnable() {
		// Open plugin properties (just the border size, for now)
		try {
			Props.load(new FileInputStream(Folder + "/rBorder.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PluginManager loader = getServer().getPluginManager();
		loader.registerEvent(Event.Type.PLAYER_MOVE, Listener, Event.Priority.Highest, this);
		loader.registerEvent(Event.Type.PLAYER_TELEPORT, Listener, Event.Priority.Highest, this);
		
		BorderSize = new Integer(Props.getProperty("size", "5000"));
		BorderAlert = Props.getProperty("Alert", "You have reached the border!");
		BorderSizeSq = BorderSize * BorderSize;
		DefiniteSquare = (int) Math.sqrt(.5 * BorderSizeSq);
		log.info("[rBorder] Loaded.  Size:" + BorderSize);
		SpawnLocation = getServer().getWorlds()[0].getSpawnLocation();
		log.info("[rBorder]: Spawn location:" + SpawnLocation.getBlockX() + ", " + SpawnLocation.getBlockY() + ", " + SpawnLocation.getBlockZ()+ ".");
	}

	public void onDisable() {
		
	}
	public boolean inBorder(Location checkHere) {
		int X = Math.abs(SpawnLocation.getBlockX() - checkHere.getBlockX());
		int Z = Math.abs(SpawnLocation.getBlockZ() - checkHere.getBlockZ());
		// If statements are cheaper than squaring twice!
		// Definitely in the circle?
		if (X < DefiniteSquare && Z < DefiniteSquare)
			return true;
		// Definitely not in the circle?
		if (X > BorderSize || Z > BorderSize)
			return false;
		// Must know for sure.
		if ( X*X + Z*Z > BorderSizeSq )
			return false;
		else return true;
	}

}
