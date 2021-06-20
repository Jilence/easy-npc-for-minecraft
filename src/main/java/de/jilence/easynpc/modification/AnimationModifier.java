package de.jilence.easynpc.modification;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import de.jilence.easynpc.NPC;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class AnimationModifier {


    /**
     * the marked npc
     */
    private final NPC npc;

    /**
     * animate a npc
     *
     * @param npc the npc which would be animated
     */
    public AnimationModifier(@NotNull NPC npc) {
        this.npc = npc;
    }

    /**
     * queues the animation to be played.
     *
     * @param entityAnimation The animation to play.
     * @return a animation modifier
     */
    @NotNull
    public AnimationModifier queue(@NotNull EntityAnimation entityAnimation) {
        return this.queue(entityAnimation.id);
    }

    /**
     * Queues the animation to be played.
     *
     * @param animationId The id of the animation to play.
     * @return  a animation modifier
     */
    @NotNull
    public AnimationModifier queue(int animationId) {
        newContainer(PacketType.Play.Server.ANIMATION, true).getIntegers().write(1, animationId);
        return this;
    }

    /**
     * sending the packets to the player
     *
     * @param packetType the packets which will send to the player
     * @param withEntityId with entity id
     * @return a packet container
     */
    protected PacketContainer newContainer(@NotNull PacketType packetType, boolean withEntityId) {
        PacketContainer packetContainer = new PacketContainer(packetType);
        if (withEntityId) {
            packetContainer.getIntegers().write(0, this.npc.getEntityId());
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return packetContainer;
    }

    /**
     * All official supported entity animations
     */
    public enum EntityAnimation {
        /**
         * Swings the main hand (hitting)
         */
        SWING_MAIN_ARM(0),
        /**
         * The damage effect
         */
        TAKE_DAMAGE(1),
        /**
         * When a player enters a bed
         */
        LEAVE_BED(2),
        /**
         * Swings the off hand
         */
        SWING_OFF_HAND(3),
        /**
         * When a player takes a critical effect
         */
        CRITICAL_EFFECT(4),
        /**
         * When a player takes a critical effect caused by magic
         */
        MAGIC_CRITICAL_EFFECT(5);

        /**
         * The id of the effect
         */
        private final int id;

        EntityAnimation(int id) {
            this.id = id;
        }
    }

}
