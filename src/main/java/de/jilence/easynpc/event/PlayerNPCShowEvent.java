package de.jilence.easynpc.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerNPCShowEvent extends PlayerEvent {

    /**
     * the handler list
     * @see PlayerNPCShowEvent#getHandlers() ()
     */
    private static final HandlerList HANDLER_LIST = new HandlerList();

    /**
     * the reason why the npc is hiding
     */
    private final PlayerNPCShowEvent.Reason reason;

    /**
     * a bool if the npc is made for all players invisible
     */
    private final Boolean allPlayer;

    /**
     * triggered when a npc is showing for a player
     *
     * @param player for which player the npc was made showing
     */
    public PlayerNPCShowEvent(Player player, PlayerNPCShowEvent.Reason reason, Boolean allPlayer) {
        super(player);
        this.allPlayer = allPlayer;
        this.reason = reason;
    }

    /**
     * @return why the npc is showing
     */
    public Reason getReason() {
        return reason;
    }

    /**
     * @return if the npc is showing for all people
     */
    public Boolean getAllPlayer() {
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

    public enum Reason {
        /**
         * the npc was spawning in the world
         */
        SPAWNING,
        /**
         * the npc was showing because the distance to the player low
         */
        DISTANCE,
        /**
         * show the player only for one player
         */
        SHOWING,
        /**
         * show the npc for all people
         */
        SHOWING_FOR_ALL;
    }

}
