package de.jilence.easynpc;

import com.mojang.authlib.GameProfile;
import de.jilence.easynpc.event.NPCEventManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class NPCBuilder {


    /**
     * the npc display name
     */
    private String name;

    /**
     * the location were the npc is spawning
     */
    private Location location;

    /**
     * the plugin
     */
    private final Plugin plugin;

    private String skinId;

    /**
     * a bool if the scheduler is running
     */
    private Boolean isRunning = false;
    /**
     * a bool, if the npc should look to the player
     */
    private Boolean lookOnPlayer = false;

    /**
     * the npc builder to build a npc
     */
    public NPCBuilder(Plugin plugin) {
        this.plugin = plugin;
        npcTick();
        NPCEventManager.atNPCInteract(plugin);
    }

    /**
     * set the name of the npc.
     * @throws Exception if the name is over 18 characters
     * @param name of the npc
     * @return the builder
     */
    public NPCBuilder setName(String name) {
        if(name.length() > 16) try {
            throw new IllegalArgumentException("name cannot be longer then 16 characters");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.name = name;
        return this;
    }

    public NPCBuilder lookingAtPlayer(Boolean bool) {
        this.lookOnPlayer = bool;
        return this;
    }

    /**
     * set the spawn location of the npc
     *
     * @param location of the npc should spawn
     * @return the builder
     */
    public NPCBuilder setLocation(Location location) {
        this.location = location;
        return this;
    }

    /**
     * set the skin from the npc
     *
     * @param uuid of the skin
     * @return the builder
     */
    public NPCBuilder setSkin(String uuid) {
        this.skinId = uuid;
        return this;
    }

    /**
     *
     * spawning the npc
     *
     * @return a spawned npc
     */
    public NPC spawn() {
        return new NPC(this.location, this.name, this.lookOnPlayer, new GameProfile(UUID.fromString(this.skinId), this.name)).spawn();
    }

    /**
     * a scheduler which control the npc`s
     */
    protected void npcTick() {
        if(isRunning) return;
        isRunning = true;
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for(NPC npc : NPC.npcMap.values()) {
                if(npc.isShouldLookAtPlayer()) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        npc.getRotationModifier().lookAtLocation(onlinePlayer.getLocation());
                    }
                }
            }

        }, 20, 2);
    }

}
