package kana.ArenaDac;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import mc.alk.arena.BattleArena;
import mc.alk.arena.util.Log;
import kana.ArenaDac.ArenaDacCommand;

public class ArenaDac extends JavaPlugin{
	
	@Override
	public void onEnable(){
		
		BattleArena.registerCompetition(this, "ArenaDac", "dac", ArenaDacArena.class, new ArenaDacCommand());

		saveDefaultConfig();
		loadConfig();


		Log.info("[" + getName()+ "] v" + getDescription().getVersion()+ " enabled!");
	}
	@Override
	public void onDisable(){
		Log.info("[" + getName()+ "] v" + getDescription().getVersion()+ " stopping!");
	}

	@Override
	public void reloadConfig(){
		super.reloadConfig();
	}
	
	public void loadConfig(){
		FileConfiguration config = getConfig();
		ArenaDacArena.vie = config.getInt("nbrVie", 2);
		ArenaDacArena.verifVie = config.getBoolean("vie", true);
	}	
}
