package kana.ArenaDac;

import org.bukkit.entity.Player;

import mc.alk.arena.executors.CustomCommandExecutor;
import mc.alk.arena.executors.MCCommand;

public class ArenaDacCommand extends CustomCommandExecutor{

	@MCCommand(cmds={"vie"}, admin=false)
	public boolean vie(Player p) {
		if(ArenaDacArena.nbr_vie_player == null){
			p.sendMessage("Aucune partie n'est en cours !");
			return false;
		}
		else{
			p.sendMessage("Vies: " + ArenaDacArena.nbr_vie_player.get(p.getName()));
			return true;
		}
    }
}
