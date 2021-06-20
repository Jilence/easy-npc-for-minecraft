package de.jilence.easynpc.modification;

import de.jilence.easynpc.NPC;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPose;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EntityPoseModifier {

    /**
     * the npc which get to the pose
     */
    private final NPC npc;

    /**
     * pose a npc to a specific pose
     *
     * @param npc which npc would pose a pose
     */
    public EntityPoseModifier(@NotNull NPC npc) {
        this.npc = npc;
    }
    /**
     * pose the npc for a specific player
     *
     * @param pose the pose for the npc
     * @return the entity pose modifier to modify the npc
     */
    public EntityPoseModifier pose(EntityPose pose, Player player) {
        DataWatcher dataWatcher = this.npc.getEntityPlayer().getDataWatcher();
        dataWatcher.set(DataWatcherRegistry.s.a(6), pose);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                new PacketPlayOutEntityMetadata(this.npc.getEntityId(), dataWatcher, false));
        return this;
    }

    /**
     * pose the npc for all online player
     *
     * @param pose the pose for the npc
     * @return the entity pose modifier to modify the npc
     */
    public EntityPoseModifier pose(EntityPose pose) {
        DataWatcher dataWatcher = this.npc.getEntityPlayer().getDataWatcher();
        dataWatcher.set(DataWatcherRegistry.s.a(6), pose);
        Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                new PacketPlayOutEntityMetadata(this.npc.getEntityId(), dataWatcher, false)
        ));
        return this;
    }
}
