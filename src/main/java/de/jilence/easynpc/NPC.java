package de.jilence.easynpc;

import com.mojang.authlib.GameProfile;
import de.jilence.easynpc.modification.RotationModifier;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NPC {

    /**
     * the minecraft server
     */
    private final MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();

    /**
     * the world server
     */
    private final WorldServer worldServer;

    /**
     * the game profile of the living npc
     */
    private final GameProfile gameProfile;

    /**
     * the location which the npc is
     */
    private final Location location;

    /**
     * a bool if the npc should look to the player
     */
    private final Boolean shouldLookAtPlayer;

    /**
     * the entity id of the npc
     */
    private Integer entityId;

    /**
     * the entity player of the npc
     */
    private EntityPlayer entityPlayer;

    /**
     * a map with all the npc`s
     */
    public static Map<Integer, NPC> npcMap = new HashMap<>();

    /**
     * create a npc
     *
     * @param location were the npc is
     * @param name name of the npc
     * @param shouldLookAtPlayer a bool if the npc looking to the player
     */
    public NPC(Location location, String name, Boolean shouldLookAtPlayer) {
        this.location = location;
        this.shouldLookAtPlayer = shouldLookAtPlayer;
        this.worldServer = ((CraftWorld) location.getWorld()).getHandle();
        this.gameProfile = new GameProfile(UUID.fromString("f93f7b0e-7c90-4954-937e-6c30ef1dd0bc"), name);
    }

    /**
     * spawning and creating the armor stand with the informations
     *
     * @return a npc
     */
    public NPC spawn() {
        EntityPlayer npc = new EntityPlayer(minecraftServer, worldServer, gameProfile, new PlayerInteractManager(worldServer));
        this.entityPlayer = npc;
        this.entityId = npc.getId();

        npcMap.put(npc.getId(), this);
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        npc.getDataWatcher().set(DataWatcherRegistry.a.a(16), (byte) 127);

        for(Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
            playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            playerConnection.sendPacket(new PacketPlayOutEntityMetadata(npc.getId(), npc.getDataWatcher(), true));
        }
        return this;
    }

    /**
     * @return the minecraft server
     */
    public MinecraftServer getMinecraftServer() {
        return this.minecraftServer;
    }

    /**
     * the rotation modifier can rotate the npc
     *
     * @return the rotation modifier
     */
    public RotationModifier getRotationModifier() {
        return new RotationModifier(this);
    }

    /**
     * @return the entity id of the npc
     */
    public Integer getEntityId() {
        return entityId;
    }

    /**
     * @return the entity player of the npc
     */
    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    /**
     * @return a bool if the npc looking to the player
     */
    public Boolean isShouldLookAtPlayer() {
        return shouldLookAtPlayer;
    }

    /**
     * @return the world server were the npc is
     */
    public WorldServer getWorldServer() {
        return this.worldServer;
    }

    /**
     * @return the game profile of the npc
     */
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    /**
     * @return the location of the npc
     */
    public Location getLocation() {
        return this.location;
    }
}