package de.jilence.easynpc.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerNPCHideEvent extends PlayerEvent {

    /**
     * the handler list
     * @see PlayerNPCHideEvent#getHandlerList()
     */
    private static final HandlerList HANDLER_LIST = new HandlerList();

    /**
     * the reason why the npc is hiding
     */
    private final Reason reason;

    /**
     * a custom event which triggered when a npc get invisible
     *
     * @param player which player don't see the npc anymore
     * @param reason why the npc is hiding
     */
    public PlayerNPCHideEvent(@NotNull Player player, Reason reason) {
        super(player);
        this.reason = reason;
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

    /**
     * get the handler list
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum Reason {
        /**
         * the npc was removed from the world
         */
        REMOVED,
        /**
         * the npc was removed because the distance to the player was to high
         */
        DISTANCE,
        /**
         * the player were not allowed to see the npc
         */
        HIDING;
    }

}
