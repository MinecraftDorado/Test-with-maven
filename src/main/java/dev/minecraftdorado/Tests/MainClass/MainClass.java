package dev.minecraftdorado.Tests.MainClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class MainClass extends JavaPlugin {
	
	public static MainClass main;
	
	private HashMap<UUID, String> list = new HashMap<UUID, String>();
	private HashMap<UUID, Integer> cooldown = new HashMap<UUID, Integer>();
	
	public void onEnable() {
		main = this;
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			
			@EventHandler
			private void as(PlayerInteractEvent e) {
				String s = list.containsKey(e.getPlayer().getUniqueId()) ? list.get(e.getPlayer().getUniqueId()) : "";
				
				String a = e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK) ? "R" : e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK) ? "L" : "";
				s+= a;
				
				if(a != "") {
					cooldown.put(e.getPlayer().getUniqueId(), 2);
					
					e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§6" + s));
					
					if(s.length() == 3) {
						ejectMagin(e.getPlayer(), s);
						s = "";
					}
					list.put(e.getPlayer().getUniqueId(), s);
				}
			}
			
		}, this);
		
		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			
			@Override
			public void run() {
				ArrayList<UUID> clear = new ArrayList<UUID>();
				for(UUID uuid : cooldown.keySet()) {
					cooldown.put(uuid, cooldown.get(uuid)-1);
					if(cooldown.get(uuid) == 0) {
						list.remove(uuid);
						Bukkit.getConsoleSender().sendMessage("§6Clear");
						clear.add(uuid);
					}
				}
				for(UUID uuid : clear)
					cooldown.remove(uuid);
			}
		}, 20, 20);
	}
	
	private void ejectMagin(Player p, String con) {
		if(con.equals("RRL")) {
			Block b = p.getTargetBlockExact(10);
			if(b!=null) {
				p.teleport(b.getLocation());
				p.sendMessage("§b*Teleport*");
			}
		}else if(con.equals("RRR")) {
			p.getWorld().createExplosion(p.getTargetBlockExact(20).getLocation(), 5);
			p.sendMessage("§b*Explote*");
		}
	}
}
