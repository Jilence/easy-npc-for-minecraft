package de.jilence.easynpc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.mojang.authlib.GameProfile;
import de.jilence.easynpc.event.PlayerNPCHideEvent;
import de.jilence.easynpc.event.PlayerNPCInteractEvent;
import de.jilence.easynpc.event.PlayerNPCShowEvent;
import de.jilence.easynpc.modification.AnimationModifier;
import de.jilence.easynpc.modification.RotationModifier;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
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
     * the name from the npc
     */
    private final String name;

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
     * @param location           were the npc is
     * @param name               name of the npc
     * @param shouldLookAtPlayer a bool if the npc looking to the player
     */
    public NPC(Location location, String name, Boolean shouldLookAtPlayer, GameProfile gameProfile) {
        this.location = location;
        this.name = name;
        this.shouldLookAtPlayer = shouldLookAtPlayer;
        this.worldServer = ((CraftWorld) location.getWorld()).getHandle();
        this.gameProfile = gameProfile;
    }

    /**
     * spawning and creating the armor stand with the information
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

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
            playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            playerConnection.sendPacket(new PacketPlayOutEntityMetadata(npc.getId(), npc.getDataWatcher(), true));
        }
        return this;
    }

    /**
     * hide a npc for a specific player
     *
     * @param player the player for which the npc made invisible
     * @param plugin the plugin
     * @param reason the reason why the npc is hiding
     */
    public void hide(@NotNull Player player, @NotNull Plugin plugin, @NotNull PlayerNPCHideEvent.Reason reason) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        packetContainer.getIntegerArrays().write(0, new int[]{this.getEntityId()});
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        Bukkit.getPluginManager().callEvent(
                new PlayerNPCHideEvent(player, reason, false)
        );
    }

    /**
     * hide a npc for all people
     *
     * @param plugin the plugin
     * @param reason the reason why the npc is hiding
     */
    public void hide(@NotNull Plugin plugin, @NotNull PlayerNPCHideEvent.Reason reason) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        packetContainer.getIntegerArrays().write(0, new int[]{this.getEntityId()});
        Bukkit.getOnlinePlayers().forEach(player -> {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        Bukkit.getPluginManager().callEvent(
                new PlayerNPCHideEvent(null, reason, true)
        );
    }

    /**
     * show a npc for all players
     *
     * @param plugin the plugin
     * @param reason the reason why the npc is showing
     */
    @Deprecated
    public void show(@NotNull Plugin plugin, @NotNull PlayerNPCShowEvent.Reason reason) {
        new NPCBuilder(plugin)
                .lookingAtPlayer(this.isShouldLookAtPlayer())
                .setName(this.getName())
                .setLocation(this.getLocation()).spawn();
        Bukkit.getPluginManager().callEvent(
                new PlayerNPCShowEvent(null, reason, false)
        );
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

    public AnimationModifier getAnimationModifier() {
        return new AnimationModifier(this);
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
     * @return the name of the npc
     */
    public String getName() {
        return name;
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
