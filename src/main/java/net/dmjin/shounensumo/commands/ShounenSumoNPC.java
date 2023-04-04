package net.dmjin.shounensumo.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.dmjin.shounensumo.ShounenSumo;

import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class ShounenSumoNPC implements CommandExecutor {
    private final ShounenSumo plugin;

    public ShounenSumoNPC(ShounenSumo plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player) || !commandSender.hasPermission("shounensumo.admin")) {
            commandSender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        Player player = (Player) commandSender;
        Location location = player.getLocation();

        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        MinecraftServer server = serverPlayer.getServer();
        ServerLevel level = serverPlayer.getLevel();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "Sumo");

        String signature = "ZdqvDNbyocLjumftqRJoJwrZUJ4oVmEtbk8mWDn5IWBYflEd1XF1XJqdIE0PCcy2o3qtLdTcwa2jU0p5CyrQb2x45adYsoPBpIQp8dhAuwcdnu3pbp3HZQc0G/mRwpYhool5qFKKO3hoJsrULsuA2L0zpHDsrZK8nUt7AYu+Kk0R4lk9Eg88VfEQQlxdKmVxXGnbRJ2RuRMUf6v2mTfHQ6DbMNI7PXjX2m5i7SHzWUWGw13kZgXAkYcUYEocALcXgTl1Wlvs4leFcZ/z1zPEgVNUJu1jZ3tX6j9DU1M0oNkntss9hB/vb8xvvO9mZCJW4kM7kQhQX7qtt4nynjTcl+mu5JJe4+JMaJfMKFGTEpkaGk5iqpkamZy+sNaD2wQfbTqw8ZMRhfde9ioVhX7pcMQw+nHWQf6lCRlRoE/XjM92Ez8eVCbJwZMWsx1VdoCw9zLTkLu6S11BaZYVZNZ9gymStkS3mdIkYEn++OHL8AUjP5503okOqA11Ag4qPGm2cwnVsNLsm3C5qdp5RYME+J6EBsWWgBYvY/t7gjA0d+usjheVdAUhLJGPgZebycp2tmg/F2j813GHwn25SMCTu5vzk2+61MMrmM4+vNqIbLBF6vnAxY1D+hRgtNvl/iN3P/NWtm2QaEtTTLg5ModfSI1xNSDULkC/A1VGPuisj+Y=";
        String texture = "ewogICJ0aW1lc3RhbXAiIDogMTY4MDY0MDQ3NTQyNSwKICAicHJvZmlsZUlkIiA6ICI1ODliZmQ5ZTc1YWQ0ZWRjOTY1MzE3ZDE5YzgwODdkOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJWZW5lcmFibGVBc3VyYSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85YTllM2Q1NmE4YzY0OTdlZjU5NjI1NzdmMzY3Mzg2OWJhYmYzYTQ0MWYwYjJmNDJjYTgyYzI5YTBmYmQ3ZDljIgogICAgfQogIH0KfQ==";
        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        ServerPlayer sumoNPC = new ServerPlayer(server, level, gameProfile);
        sumoNPC.setPos(location.getX(), location.getY(), location.getZ());

        ServerGamePacketListenerImpl ps = serverPlayer.connection;

        // Player Info Packet
        ps.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, sumoNPC));

        ps.send(new ClientboundAddPlayerPacket(sumoNPC));

        ShounenSumo.getNpcs().add(sumoNPC);
        player.sendMessage("NPC created.");

        return true;
    }

}