package kana.ArenaDac;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import mc.alk.arena.BattleArena;
import mc.alk.arena.util.Log;

public class ArenaDac extends JavaPlugin{
	
	@Override
	public void onEnable(){
		/// Registers this plugin with BattleArena
		/// this: our plugin
		/// "OneInTheChamber": The name of our competition
		/// "oic": the name of our command alias
		/// OITCArena.class: which arena should this competition use
		BattleArena.registerCompetition(this, "ArenaDac", "dac", ArenaDacArena.class);

		/// Load our config options
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
		/// Allow the damage to be set through the config.yml, if it exists and has the section: 'damage: <value>'
		/// Like 'damage: 15'
		FileConfiguration config = getConfig();
		ArenaDacArena.vie = config.getInt("nbrVie", 2);
		ArenaDacArena.verifVie = config.getBoolean("vie", true);
	}
}
