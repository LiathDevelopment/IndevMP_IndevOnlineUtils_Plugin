import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import net.minecraft.game.entity.Entity;
import net.minecraft.server.player.EntityPlayerMP;
import plugins.PluginBase;

public class Plugin_IndevOnlineUtils extends PluginBase {
	private int counter = 0;
	
	public void onServerStartup(Logger logger) {
        this.server.log("[IndevOnlineUtils] Loaded!");
        this.updateWebsite("Starting server");
        this.minecraftLogger = logger;
    }
	
	public void onServerTick() {
		if(this.counter < 1200) {
			++this.counter;
		} else {
			this.counter = 0;
			this.updateWebsite("");
		}
	}
	
    public void onPlayerJoined(EntityPlayerMP entityPlayerMP) {
    	String bottomBorder = "--------";
    	String worldDifficulty = "";
    	
    	switch(this.server.worldMngr.difficultySetting) {
    	case 0:
    		worldDifficulty = "Peaceful";
    		break;
    	case 1:
    		worldDifficulty = "Easy";
    		break;
    	case 2:
    		worldDifficulty = "Normal";
    		break;
    	case 3:
    		worldDifficulty = "Hard";
    		break;
    	}
    	
    	for(int i = 0; i < this.server.serverName.length(); i++) {
    		bottomBorder += "-";
    	}
    	
    	this.server.configManager.sendChatMessageToPlayer(entityPlayerMP.username, "-----" + this.server.serverName + "-----");
    	this.server.configManager.sendChatMessageToPlayer(entityPlayerMP.username, "§aWelcome to the server, §b" + entityPlayerMP.username + "§a!");
    	this.server.configManager.sendChatMessageToPlayer(entityPlayerMP.username, "§aPlayers online: §f" + this.server.worldMngr.playerEntities.size() + "/" + this.server.configManager.maxPlayers);
    	this.server.configManager.sendChatMessageToPlayer(entityPlayerMP.username, "§aLoaded level: §f" + this.server.worldMngr.name);
    	this.server.configManager.sendChatMessageToPlayer(entityPlayerMP.username, "§aLevel difficulty: §f" + worldDifficulty);
    	//this.server.configManager.sendChatMessageToPlayer(entityPlayerMP.username, "§aPlugins: §f" + this.server.plugins.toString());
    	this.server.configManager.sendChatMessageToPlayer(entityPlayerMP.username, bottomBorder);
    	this.server.configManager.sendChatMessageToPlayer(entityPlayerMP.username, " ");
    	this.server.configManager.sendChatMessageToPlayer(entityPlayerMP.username, "§aMake sure to read the rules using §b!rules§a.");
    	this.server.configManager.sendChatMessageToPlayer(entityPlayerMP.username, "§aGet a list of commands with §b!help§a.");
    	this.server.configManager.sendChatMessageToPlayer(entityPlayerMP.username, "§aVisit §bindevonline.liath.xyz §afor more info.");
    	
    	this.updateWebsite(entityPlayerMP.username + " logged in with entity id " + entityPlayerMP.entityId);
    }
    
    public void onPlayerLeft(EntityPlayerMP entityPlayerMP) {
    	this.updateWebsite(entityPlayerMP.username + " lost connection");
    }
    
    public void onPlayerDeath(String username, Entity entityKiller) {
    	this.updateWebsite(username + " died");
    }
    
    public void onPlayerKicked(EntityPlayerMP entityPlayerMP) {
    	this.updateWebsite(entityPlayerMP.username + " was kicked");
    }
    
    public void onChatMessageSent(String username, String message) {
    	this.updateWebsite(message);
    	
    	//slash commands aren't working for non-ops so this is a "temporary" fix
    	if (message.toLowerCase().contains("!help")) {
        	this.server.configManager.sendChatMessageToPlayer(username, " ");
    		this.server.configManager.sendChatMessageToPlayer(username, "-----Command Help-----");
    		this.server.configManager.sendChatMessageToPlayer(username, "§cAn ! rather than a / is required because of a bug.");
    		this.server.configManager.sendChatMessageToPlayer(username, "§a!help §f- Lists command help");
    		this.server.configManager.sendChatMessageToPlayer(username, "§a!rules §f- Lists server rules");
    		this.server.configManager.sendChatMessageToPlayer(username, "§a!version §f- Retrieve server version");
    		this.server.configManager.sendChatMessageToPlayer(username, "§a!coords §f- Displays your location");
    		this.server.configManager.sendChatMessageToPlayer(username, "§a!spawn §f- Takes you back to spawn");
    		this.server.configManager.sendChatMessageToPlayer(username, "§a!clearchat §f- Clears your chat");
        } else if (message.toLowerCase().contains("!rules")) {
    		this.server.configManager.sendChatMessageToPlayer(username, " ");
    		this.server.configManager.sendChatMessageToPlayer(username, "-----Server Rules-----");
    		this.server.configManager.sendChatMessageToPlayer(username, "§4indevonline.liath.xyz/modguide.html");
    		this.server.configManager.sendChatMessageToPlayer(username, "§c-No spamming");
    		this.server.configManager.sendChatMessageToPlayer(username, "§c-No exploiting");
    		this.server.configManager.sendChatMessageToPlayer(username, "§c-No griefing");
    		this.server.configManager.sendChatMessageToPlayer(username, "§c-No stealing");
    		this.server.configManager.sendChatMessageToPlayer(username, "§c-Use common sense");
        } else if (message.toLowerCase().contains("!version")) {
    		this.server.configManager.sendChatMessageToPlayer(username, "This server is running §bIndev 20100223 MP");
    		this.server.configManager.sendChatMessageToPlayer(username, "Version §av0.1.12 (indevmulti-1.0.12)");
        } else if (message.toLowerCase().contains("!coords")) {
        	EntityPlayerMP entityPlayerMP = this.server.configManager.getPlayerEntity(username);
    		this.server.configManager.sendChatMessageToPlayer(username, "§aYour location: x:§b" + entityPlayerMP.posX + "§a, y:§b" + entityPlayerMP.posY + "§a, z:§b" + entityPlayerMP.posZ);
        } else if (message.toLowerCase().contains("!spawn")) {
        	EntityPlayerMP entityPlayerMP = this.server.configManager.getPlayerEntity(username);
        	entityPlayerMP.playerNetServerHandler.teleportTo(this.server.worldMngr.xSpawn, this.server.worldMngr.ySpawn, this.server.worldMngr.zSpawn, entityPlayerMP.rotationYaw, entityPlayerMP.rotationPitch);
        	this.server.configManager.sendChatMessageToPlayer(username, "§aYou have been returned to spawn!");
        	this.updateWebsite(username + " returned to spawn");
        } else if (message.toLowerCase().contains("!clearchat")) {
        	for(int i = 0; i < 19; i++) {
        		this.server.configManager.sendChatMessageToPlayer(username, " ");
        	}
        	this.server.configManager.sendChatMessageToPlayer(username, "§aChat has been cleared!");
        }
    }
    
    @SuppressWarnings("unused")
	public void updateWebsite(String message) {
    	try {
			URL var1 = new URL("http://127.0.0.1:80/updateserver?text=" + message.replace(" ", "~") + "&players=" + this.server.configManager.getPlayerList() + "&maxplayers=" + this.server.configManager.maxPlayers +  "&playercount=" + this.server.worldMngr.playerEntities.size() + "&world=" + this.server.worldMngr.name);
			BufferedReader var3 = new BufferedReader(new InputStreamReader(var1.openConnection().getInputStream()));
			String var4 = var3.readLine();
			this.server.log("[IndevOnlineUtils] Updated webserver with response: " + var4);
		} catch (Exception var2) {
			this.server.log("[IndevOnlineUtils] Webserver update failed, offline?");
		}
    }
}
