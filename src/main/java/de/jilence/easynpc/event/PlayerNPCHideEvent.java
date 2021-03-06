package de.jilence.easynpc.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * a bool if the npc is made for all players invisible
     */
    private final Boolean allPlayer;

    /**
     * a custom event which triggered when a npc get invisible
     *
     * @param player which player don't see the npc anymore
     * @param reason why the npc is hiding
     */
    public PlayerNPCHideEvent(@Nullable Player player, Reason reason, Boolean allPlayer) {
        super(player);
        this.reason = reason;
        this.allPlayer = allPlayer;
    }

    /**
     *
     * @return a bool if the npc for all players were made invisible
     * return true if for all, false otherwise
     */
    public Boolean getIfAllPlayer() {
        return allPlayer;
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

    /**
     * @return the reason why the npc was hiding
     */
    public Reason getReason() {
        return reason;
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
        HIDING,
        /**
         * hiding the npc for all people
         */
        HIDING_FOR_ALL;
    }

}
