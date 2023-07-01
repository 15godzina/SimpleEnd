package pl.karol.end.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import pl.karol.end.configuration.PluginConfiguration;
import pl.karol.end.util.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class WaterListener implements Listener {

    private final PluginConfiguration pluginConfiguration;
    private final Plugin plugin;

    public WaterListener(PluginConfiguration pluginConfiguration, Plugin plugin) {
        this.pluginConfiguration = pluginConfiguration;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = event.getFrom();

        if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
            player.teleport(new Location(player.getWorld(), 0, 100, -0));
            player.sendTitle(TextUtil.color("&dSwiat END"), TextUtil.color("&7Walcz o przetrwanie!"));
        }
    }

    private final List<UUID> uuids = new ArrayList<>();

    @EventHandler
    public void handle(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            Player player = (Player) event.getEntity();
            player.teleport(findRandomLocation(player.getWorld(), -100, 100));
            player.resetTitle();
            player.sendTitle(TextUtil.color("&bRob watera bo zginiesz!"), "");
            uuids.add(player.getUniqueId());
            event.setCancelled(true);

            uuids.forEach(System.out::println);
        }
    }

    public Location findRandomLocation(World paramWorld, int paramInt1, int paramInt2) {
        Random random = new Random();
        int i = random.nextInt(paramInt2 - paramInt1 + 1) + paramInt1;
        int j = random.nextInt(paramInt2 - paramInt1 + 1) + paramInt1;
        return new Location(paramWorld, i, 200, j);
    }

    @EventHandler
    public void handle(BlockBreakEvent event) {
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.THE_END) {

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(BlockPlaceEvent event) {
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.THE_END) {
            if (event.getItemInHand().getType() == Material.WATER_BUCKET) {
                event.setCancelled(false);
            }
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (!uuids.contains(event.getPlayer().getUniqueId())) return;   
        if (event.getMaterial() != Material.ENDER_PEARL) return;

        event.setUseItemInHand(Event.Result.DENY);
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockClicked().getRelative(BlockFace.UP);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            block.setType(Material.AIR);
        }, 40L);

        if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
            if (!uuids.contains(player.getUniqueId())) {
                return;
            }
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (block.getType() != Material.WATER) {
                    player.spigot().respawn();
                }
            }, 1L);
            player.sendTitle(TextUtil.color("&a&l&oGratulacje"), "");
            }

            uuids.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (uuids.contains(player.getUniqueId())) {
            uuids.remove(player.getUniqueId());
        }
    }
}