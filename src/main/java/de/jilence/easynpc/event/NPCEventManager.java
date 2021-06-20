package de.jilence.easynpc.event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import de.jilence.easynpc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class NPCEventManager {

    /**
     * triggers the event when somebody is hitting a npc
     *
     * @param plugin the plugin
     */
    public static void atNPCInteract(Plugin plugin) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packetContainer = event.getPacket();
                int targetId = packetContainer.getIntegers().read(0);

                if(NPC.npcMap.containsKey(targetId)) {
                    NPC npc = NPC.npcMap.get(targetId);
                    EnumWrappers.EntityUseAction action = packetContainer.getEntityUseActions().read(0);

                    EnumWrappers.Hand hand = action == EnumWrappers.EntityUseAction.ATTACK ? EnumWrappers.Hand.MAIN_HAND
                            : packetContainer.getHands().optionRead(0).orElse(EnumWrappers.Hand.MAIN_HAND);


                    Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerNPCInteractEvent(
                            event.getPlayer(), npc, action, hand
                    )));
                }
            }
        });
    }
}
