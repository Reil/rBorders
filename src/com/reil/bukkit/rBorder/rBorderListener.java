package com.reil.bukkit.rBorder;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

public class rBorderListener extends PlayerListener{
	public BorderPlugin rBorder;
	public rBorderListener(BorderPlugin rBorder) {
		this.rBorder = rBorder;
	}
	public void onPlayerJoin(PlayerEvent event){
		Player checkMe = event.getPlayer();
		//Location spawnedHere = checkMe.getLocation();
		if (rBorder.inBorder(checkMe.getLocation())) return;
		checkMe.teleportTo(rBorder.SpawnLocation);
		/*
		double angle = Math.atan2(spawnedHere.getZ(), spawnedHere.getX());
		spawnedHere.setX(rBorder.BorderSize * Math.cos(angle));
		spawnedHere.setZ(rBorder.BorderSize * Math.sin(angle));
		checkMe.teleportTo(spawnedHere);*/
	}
	public void onPlayerMove(PlayerMoveEvent event){
		if (rBorder.inBorder(event.getTo()))
			return;
		event.setCancelled(true);
		Player barMe = event.getPlayer();
		barMe.sendMessage(rBorder.BorderAlert);
		barMe.teleportTo(event.getFrom());
		return;
	}
	public void onPlayerTeleport(PlayerMoveEvent event){
		if (rBorder.inBorder(event.getTo()))
			return;
		event.setCancelled(true);
		Player barMe = event.getPlayer();
		barMe.sendMessage(rBorder.BorderAlert);
		barMe.teleportTo(event.getFrom());
		return;
	}
	
}
