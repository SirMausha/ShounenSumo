package net.dmjin.shounensumo.listeners;

import net.dmjin.shounensumo.ShounenSumo;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        //Get the player
        Player p = e.getPlayer();

        ShounenSumo.getNpcs().stream()
                .forEach(npc -> {

                    //The location of the NPC
                    Location loc = npc.getBukkitEntity().getLocation();
                    //Calculate a new direction by subtracting the location of the player vector from the location vector of the npc
                    loc.setDirection(p.getLocation().subtract(loc).toVector());

                    //yaw and pitch used to calculate head movement
                    float yaw = loc.getYaw();
                    float pitch = loc.getPitch();

                    //get the connection so we can send packets in NMS
                    ServerGamePacketListenerImpl ps = ((CraftPlayer) p).getHandle().connection;

                    //used for horizontal head movement
                    ps.send(new ClientboundRotateHeadPacket(npc, (byte) ((yaw%360)*256/360)));
                    //used for body movement and vertical head movement
                    ps.send(new ClientboundMoveEntityPacket.Rot(npc.getBukkitEntity().getEntityId(), (byte) ((yaw%360.)*256/360), (byte) ((pitch%360.)*256/360), false));

                });
    }

}
