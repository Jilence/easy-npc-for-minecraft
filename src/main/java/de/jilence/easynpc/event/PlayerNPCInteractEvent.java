package de.jilence.easynpc.event;

import com.comphenix.protocol.wrappers.EnumWrappers;
import de.jilence.easynpc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerNPCInteractEvent extends PlayerEvent {

    /**
     * a list of the handler list
     */
    private static final HandlerList HANDLER_LIST = new HandlerList();

    /**
     * the npc which the player interacted
     */
    private final NPC npc;

    /**
     * the hand which the player hit the npc
     */
    private final EnumWrappers.Hand hand;

    /**
     * the action of the hit
     */
    private final EnumWrappers.EntityUseAction action;

    /**
     * a event which called when a player hit a npc
     *
     * @param player which hit the npx
     * @param npc which was hit
     * @param action the action of the hit
     * @param hand the hand
     */
    public PlayerNPCInteractEvent(Player player, NPC npc, EnumWrappers.EntityUseAction action, EnumWrappers.Hand hand) {
        super(player);
        this.npc = npc;
        this.action = action;
        this.hand = hand;
    }

    /**
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * get the npc which the player hit
     *
     * @return the npc which was hit
     */
    public NPC getNPC() {
        return this.npc;
    }

    /**
     * get the hand which the player hit the npc
     *
     * @return the hand of the event
     */
    public EnumWrappers.Hand getHand() {
        return hand;
    }

    /**
     * get the action of the player wile hitting the npc
     *
     * @return the action of the event
     */
    public EnumWrappers.EntityUseAction getAction() {
        return action;
    }

    /**
     * get the handler list
     *
     * @return the handler list
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
