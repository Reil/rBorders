package com.reil.bukkit.rBorder;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class rBorderListener extends PlayerListener{
	public BorderPlugin rBorder;
	public rBorderListener(BorderPlugin rBorder) {
		this.rBorder = rBorder;
	}
	public void onPlayerJoin(PlayerJoinEvent event){
		Player checkMe = event.getPlayer();
		if (rBorder.inBorder(checkMe.getLocation())) return;
		checkMe.teleport(checkMe.getWorld().getSpawnLocation());
	}
	public void onPlayerMove(PlayerMoveEvent event){
		if (rBorder.inBorder(event.getTo()))
			return;
		Player barMe = event.getPlayer();
		if(rBorder.inBorder(event.getFrom())){
			barMe.sendMessage(rBorder.BorderAlert);
			event.setTo(event.getFrom());
			barMe.teleport(event.getFrom());
			return;
		} else {
			barMe.damage(1);
		}
		return;
	}
	public void onPlayerTeleport(PlayerTeleportEvent event){
		if (rBorder.inBorder(event.getTo()))
			return;
		Player barMe = event.getPlayer();
		barMe.sendMessage(rBorder.BorderAlert);
		if(rBorder.inBorder(event.getFrom())){
			event.setTo(event.getFrom());
			barMe.teleport(event.getFrom());
		} else
			barMe.teleport(barMe.getWorld().getSpawnLocation());
	}
	
}
