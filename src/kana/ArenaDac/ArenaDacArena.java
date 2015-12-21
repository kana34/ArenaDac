package kana.ArenaDac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.events.ArenaEventHandler;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.arena.util.TeamUtil;

public class ArenaDacArena extends Arena{	
	Location locationSpawnStart;
	Location locationSpawnPlongeoir;
	int nbr_teams;
	int numero_team = 0;
	List<ArenaTeam> team;
	Player playerSaut = null;
	static int vie = 2;
	static boolean verifVie = true;
	HashMap<Player, Integer> nbr_vie_player;
	int vieok = 0;
	Material mm;
	
	
	//-----------------
	//---- ONSTART ----
	//-----------------
	@Override
    public void onStart(){
		locationSpawnStart = getSpawn(2, false).getLocation();
		locationSpawnPlongeoir = getSpawn(3, false).getLocation();
		
		Nbr_Teams();
		
		TpAllPlayerSpawn();
		
		setLivePlayer();		
		
		TpPlayerPlongeoir(Player_Team(numero_team));
	}
	
	//-----------------------------------------
	//---- On récupère le nombre de joueur ----
	//-----------------------------------------
	public int Nbr_Teams(){
		team = getTeams();
		nbr_teams = team.size();
		return nbr_teams;
	}
	
	//-------------------------------------------
	//---- On récupère le joueurs de la team ----
	//-------------------------------------------
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Player Player_Team(int num_team){
		Set<Player> SetPlayerInTeam = team.get(num_team).getBukkitPlayers();
		List<Player> PlayerInTeam = new ArrayList(SetPlayerInTeam);
		return PlayerInTeam.get(0);
	}
	
	//-------------------------------------------------
	//---- On téléporte tous les joueurs au départ ----
	//-------------------------------------------------
	public void TpAllPlayerSpawn(){
		for(int i=0; i < nbr_teams; i++){
			TpPlayerSpawn(Player_Team(i));
		}
	}
	
	//------------------------------------------------
	//---- On téléporte tous les joueurs au spawn ----
	//------------------------------------------------
	public void TpPlayerSpawn(Player player){
		player.teleport(locationSpawnStart);		
	}
	
	//---------------------------------------------
	//---- On téléporte le joueur au plongeoir ----
	//---------------------------------------------
	public void TpPlayerPlongeoir(Player player){
		player.teleport(locationSpawnPlongeoir);
		playerSaut = player;
	}
	
	//-----------------------------------------------------
	//---- On créé une liste avec les vies des joueurs ----
	//-----------------------------------------------------
	public void setLivePlayer(){
		nbr_vie_player = new HashMap<Player, Integer>();
		
		for(int i=0; i< nbr_teams; i++){
			nbr_vie_player.put(Player_Team(i), vie);
		}				
	}
	
	//------------------------------------------------
	//---- On regarde si le joueur réusi son saut ----
	//------------------------------------------------
	@SuppressWarnings("deprecation")
	@ArenaEventHandler(suppressCastWarnings=true)
	public void onPlayerMove(PlayerMoveEvent e) {
    	Location l = e.getPlayer().getLocation();
		Block b = l.getBlock();
    	Material m = b.getType();

		// Si le joueur touche l'eau ou la lave
    	//-------------------------------------
	    if (m == Material.STATIONARY_WATER || m == Material.WATER || m == Material.LAVA){	    	
	    	if(playerSaut == e.getPlayer()){
	    			    		
	    		// On récupère la couleur de la team
		    	//----------------------------------
		    	DyeColor c = TeamUtil.getDyeColor(numero_team);
		    	byte d = c.getData();
		    	
		    	// On Vérifi si le joueur a fait une vie
		        //--------------------------------------
		        if(verifVie == true){
		        	verif_vie(l, e.getPlayer());
		        }
		    	
		        // On défini le Material à mettre
		        //-------------------------------
		        if(vieok == 0){
		        	mm = Material.WOOL;
		        }
		        else{
		        	mm = Material.GLASS;
		        }

		        // On change les blocs en verre ou en laine
		        //-----------------------------------------
		        for(int i = 0;i < 3;i++){
		        	if(b.getRelative(0, i, 0).getType() == Material.WATER || b.getRelative(0, i, 0).getType() == Material.STATIONARY_WATER || b.getRelative(0, i, 0).getType() == Material.LAVA){
		        		b.getRelative(0, i, 0).setType(mm);
			        	b.getRelative(0, i, 0).setData(d);
		        	}		        	
		        }
		        for(int ii = -4;ii < 0;ii++){
		        	if(b.getRelative(0, ii, 0).getType() == Material.WATER || b.getRelative(0, ii, 0).getType() == Material.STATIONARY_WATER || b.getRelative(0, ii, 0).getType() == Material.LAVA){
		        		b.getRelative(0, ii, 0).setType(mm);
			        	b.getRelative(0, ii, 0).setData(d);
		        	}
		        }
		        
		        // On verifi si tous les joueurs ont fait leur saut
		        //-------------------------------------------------
		        if(nbr_teams != numero_team + 1){
		        	numero_team = numero_team +1;		        	
		        	TpPlayerPlongeoir(Player_Team(numero_team));
		        }
		        else{
		        	Nbr_Teams();
		        	numero_team = 0;
		        	TpPlayerPlongeoir(Player_Team(numero_team));
		        }
		        
		        // On téléporte le joueur au spawn après son saut
		        //-----------------------------------------------
		        e.getPlayer().teleport(locationSpawnStart);
	    	}
	    }
	}
	
	//------------------------------------------------
	//---- On regarde si le joueur loupe son saut ----
	//------------------------------------------------
	@ArenaEventHandler(suppressCastWarnings=true)
	public void onEntityDamage(EntityDamageEvent e){
		Player p = (Player) e.getEntity();
			if(playerSaut == p){			
				int v = nbr_vie_player.get(p) - 1;
				
				// On verifi le nombre de vie restant
				//-----------------------------------
				if(v == 0){
					e.setDamage(20);
					p.sendMessage("Vous avez loupez votre saut !");
				}
				else{
					e.setDamage(0);
					p.sendMessage("Vous avez loupez votre saut !");
					p.sendMessage("Vous perdez une vie !");
					nbr_vie_player.replace(p, v);
					p.sendMessage("il vous en reste : " + v);
					p.teleport(locationSpawnStart);
				}
				
				// On verifi si c'est le dernier joueur qui saute
				//-----------------------------------------------
				if(nbr_teams != numero_team + 1){
		        	numero_team = numero_team + 1;	        	
		        	TpPlayerPlongeoir(Player_Team(numero_team));
		        }
		        else{
		        	Nbr_Teams();
		        	numero_team = 0;
		        	TpPlayerPlongeoir(Player_Team(numero_team));
		        }
			}
			else{
				e.setDamage(0);
			}
	}
	
	//---------------------------------------------
	//---- On vérifi si le joueur gagne un vie ----
	//---------------------------------------------
	public void verif_vie(Location loc, Player player){
		Material m1 = loc.getBlock().getRelative(1, -1, 0).getType();
		Material m2 = loc.getBlock().getRelative(-1, -1, 0).getType();
		Material m3 = loc.getBlock().getRelative(0, -1, 1).getType();
		Material m4 = loc.getBlock().getRelative(0, -1, -1).getType();
		
		if(m1 == Material.WOOL && m2 == Material.WOOL && m3 == Material.WOOL && m4 == Material.WOOL){
			int v = nbr_vie_player.get(player) + 1;
			nbr_vie_player.replace(player, v);
			player.sendMessage("Félicitation vous avez gagner une vie");
			player.sendMessage("il vous en reste : " + v);
			vieok = 1;
		}
		else{
			vieok = 0;
		}
	}
	
	//---------------------------------------------
	//---- On vérifi si il reste que 2 joueurs ----
	//---------------------------------------------
}
