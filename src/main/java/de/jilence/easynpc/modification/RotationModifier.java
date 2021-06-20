package de.jilence.easynpc.modification;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import de.jilence.easynpc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RotationModifier {

    /**
     * the npc which were modified
     */
    private final NPC npc;

    /**
     * a list with packets, which would be sendet in a time
     */
    private final List<PacketContainer> packetContainers = new CopyOnWriteArrayList<>();

    /**
     * rotation modifier, to rotate the npc
     *
     * @param npc the npc which were modified
     */
    public RotationModifier(NPC npc) {
        this.npc = npc;
    }

    /**
     * create a new container and send it when the send method is called
     *
     * @see RotationModifier#send(Iterable, boolean)
     *
     * @param packetType the packet type
     * @param withEntityId with the entity id
     */
    protected PacketContainer newContainer(@NotNull PacketType packetType, boolean withEntityId) {
        PacketContainer packetContainer = new PacketContainer(packetType);
        if (withEntityId) {
            packetContainer.getIntegers().write(0, this.npc.getEntityId());
        }

        this.packetContainers.add(packetContainer);
        return packetContainer;
    }

    /**
     * send the packets in the packet container list
     *
     * @param players player list which get the packets
     * @param createClone create a clone of the packet Container
     */
    public void send(@NotNull Iterable<? extends Player> players, boolean createClone) {
        players.forEach(player -> {
            try {
                for (PacketContainer packetContainer : this.packetContainers) {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player,
                            createClone ? packetContainer.shallowClone() : packetContainer);
                }
            } catch (InvocationTargetException exception) {
                exception.printStackTrace();
            }
        });
        this.packetContainers.clear();
    }

    /**
     * add the packets which rotate the npc to the packets container list and send them
     *
     * @param yaw of the location
     * @param pitch of the location
     */
    public RotationModifier queueRotate(float yaw, float pitch) {
        byte yawAngle = (byte) (yaw * 256F / 360F);
        byte pitchAngle = (byte) (pitch * 256F / 360F);

        PacketContainer entityHeadLookContainer = newContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION, true);
        entityHeadLookContainer.getBytes().write(0, yawAngle);
        PacketContainer bodyRotateContainer = newContainer(PacketType.Play.Server.ENTITY_LOOK, true);
        bodyRotateContainer.getBytes()
                .write(0, yawAngle)
                .write(1, pitchAngle);
        bodyRotateContainer.getBooleans().write(0, true);
        send(Bukkit.getOnlinePlayers(), false);
        return this;
    }

    /**
     *
     * calc the yaw and pitch and call the queueRotate method
     * @see RotationModifier#queueRotate(float, float) 
     * 
     * @param location of the location who the npc
     */
    public RotationModifier lookAtLocation(@NotNull Location location) {
        double xDifference = location.getX() - npc.getLocation().getX();
        double yDifference = location.getY() - npc.getLocation().getY();
        double zDifference = location.getZ() - npc.getLocation().getZ();

        double r = Math
                .sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2) + Math.pow(zDifference, 2));

        float yaw = (float) (-Math.atan2(xDifference, zDifference) / Math.PI * 180D);
        yaw = yaw < 0 ? yaw + 360 : yaw;

        float pitch = (float) (-Math.asin(yDifference / r) / Math.PI * 180D);

        return this.queueRotate(yaw, pitch);
    }

}
