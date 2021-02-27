package com._11b11t.plugin11b11t.events

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.HumanEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.spigotmc.event.entity.EntityDismountEvent
import org.spigotmc.event.entity.EntityMountEvent

class ToolListener(private val plugin: JavaPlugin): Listener {
    @Suppress("DEPRECATION")
    @EventHandler
    fun onPlayerItemUse(event: org.bukkit.event.player.PlayerItemHeldEvent) {
        val player = event.player
        val inventory = player.inventory
        val item = inventory.getItem(event.newSlot)

        item?.let {
            val itemName = it.type.name

            val section = plugin.config.getConfigurationSection("items.${itemName}")

            var remove = true
            section?.let { sec ->
                sec.getKeys(false).forEach { enchantment ->
                    item.enchantments.forEach { e ->
                        try {
                            if (e.key.name == enchantment) {
                                val level = Integer.parseInt(sec[enchantment].toString())
                                item.addEnchantment(e.key, level)
                            }
                        } catch (ex: Exception) {
                            plugin.logger.info("Error: $ex")
                        }
                    }
                    remove = false
                }
                if (remove) {
                    item.amount = 0
                }
            }
        }
    }

    /*
    @Suppress("DEPRECATION")
    @EventHandler
    fun onEntityPickupItemEvent(event: EntityPickupItemEvent) {
        limitaEnchanmentsDunaEspasa(event.item.itemStack)
    }
    */

    @Suppress("DEPRECATION")
    @EventHandler
    fun onEntityPickupItemEvent(event: org.bukkit.event.entity.EntityPickupItemEvent) {
        //limitaEnchantmentDiamondSword(event.player)
        val name = event.item.itemStack.type.name

        //limitaEnchantmentDiamondSword(event.player)

        //plugin.logger.info("Pickup Item $name")

        if (name == "TOTEM" && event.item.itemStack.amount > 1) {
            //event.item.itemStack.amount = event.item.itemStack.amount.coerceAtMost(1)
            event.isCancelled = true
        }
    }

    private fun limitaEnchantmentDiamondSword(player: HumanEntity) {
        if (player.inventory.itemInMainHand.type == Material.DIAMOND_SWORD) {
            limitaEnchanmentsDunaEspasa(player.inventory.itemInMainHand)
        }

        if (player.inventory.itemInOffHand.type == Material.DIAMOND_SWORD) {
            limitaEnchanmentsDunaEspasa(player.inventory.itemInOffHand)
        }
    }

    @EventHandler
    fun onInventoryCloseEvent(event: org.bukkit.event.inventory.InventoryCloseEvent) {
        onInventoryManage(event.inventory)

        limitaEnchantmentDiamondSword(event.player)
    }

    @EventHandler
    fun onInventoryOpenEvent(event: org.bukkit.event.inventory.InventoryOpenEvent) {
        onInventoryManage(event.inventory)
    }

    private fun limitaEnchanmentsDunaEspasa(it: ItemStack) {
        if (it.type == Material.DIAMOND_SWORD) {
            it.enchantments.forEach { e->
                try {
                    var maxim = e.value
                    maxim = when(e.key) {
                        Enchantment.LOOT_BONUS_MOBS -> 3
                        Enchantment.KNOCKBACK -> 2
                        Enchantment.DAMAGE_ALL -> 5
                        Enchantment.FIRE_ASPECT -> 2
                        Enchantment.DURABILITY -> 2
                        else -> maxim
                    }
                    it.addEnchantment(e.key,maxim)
                } catch(ex: Exception) {
                    plugin.logger.info("Error: $ex")
                }
            }
        }

        if (it.type.name == "TOTEM") {
            it.amount = it.amount.coerceAtMost(1)
        }

        //plugin.logger.info("Limita ${it.type.name} ${it.amount}")
    }

    private fun onInventoryManage(inventory: Inventory) {
        inventory.forEachIndexed { _, it ->
            if (it != null) {
                val name = it.type.name

                if (it.type == Material.WRITTEN_BOOK) {
                    it.amount = 0
                }

                if (name  == "TOTEM") {
                    it.amount = it.amount.coerceAtMost(1)
                }

                //plugin.logger.info("Inventory Manage $name ${it.amount}")

                limitaEnchanmentsDunaEspasa(it)
            }
        }
    }

    @EventHandler
    fun onPlayerItemHeldEvent(event: org.bukkit.event.player.PlayerItemHeldEvent) {
    }

    @EventHandler
    fun onPlayerDropItemEvent(event: org.bukkit.event.player.PlayerDropItemEvent) {
    }

    @EventHandler
    fun onInventoryDragEvent(event: org.bukkit.event.inventory.InventoryDragEvent) {
        limitaEnchanmentsDunaEspasa(event.whoClicked.inventory.itemInMainHand)
    }

    @EventHandler
    fun onPlayerBedEnterEvent(event: org.bukkit.event.player.PlayerBedEnterEvent) {
        try {
            event.player.world.time = 0
        }
        catch(ex: Exception) {
            plugin.logger.warning("Error: $ex")
        }
    }
/*
    @Suppress("DEPRECATION")
    @EventHandler
    fun onPlayerJoin(event: org.bukkit.event.player.PlayerJoinEvent) {
        event.player.inventory.forEachIndexed { index, it ->
            if (it != null) {
                if (it.amount > it.maxStackSize) {
                    it.amount = it.maxStackSize

                    val meta = it.itemMeta
                    it.itemMeta = meta
                }

                val name = it.type.name

                if (name == "DRAGON_EGG") {
                    event.player.inventory.setItem(index, ItemStack(Material.STONE,it.amount))
                }
                else if (isShulkerBox(it.type)) {
                    val meta = it.itemMeta
                    val box = (meta as BlockStateMeta).blockState as ShulkerBox

                    for (i in box.inventory.contents) {
                        if (i != null) {
                            if (i.type == Material.DRAGON_EGG)
                                i.amount = 0
                            else if (i.amount > i.maxStackSize) {
                                i.amount = i.maxStackSize
                            }
                        }
                    }

                    meta.blockState = box
                    it.itemMeta = meta
                }
            }
        }

    }
*/

    @Suppress("DEPRECATION")
    @EventHandler
    fun onPlayerJoin(event: org.bukkit.event.player.PlayerJoinEvent) {
        event.player.inventory.forEach {
            if (it != null) {
                val name = it.type.name

                if (name == "TOTEM") {
                    it.amount = 1
                    //event.player.inventory.setItem(index, ItemStack("TOTEM",1))
                }
            }
        }
    }

    /*
    private fun isShulkerBox(m: Material): Boolean {
        return when (m) {
            //Material.LIGHT_GRAY_SHULKER_BOX,
            Material.BLACK_SHULKER_BOX,
            Material.BLUE_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX,
            Material.CYAN_SHULKER_BOX,
            Material.GRAY_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX,
            Material.LIGHT_BLUE_SHULKER_BOX,
            Material.LIME_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX,
            Material.ORANGE_SHULKER_BOX,
            Material.PINK_SHULKER_BOX,
            Material.PURPLE_SHULKER_BOX,
            Material.RED_SHULKER_BOX,
            Material.WHITE_SHULKER_BOX,
            Material.YELLOW_SHULKER_BOX -> true
            else -> false
        }
    }
*/
    @EventHandler
    fun onPlayerInteractEvent(event: org.bukkit.event.player.PlayerInteractEvent) {
        val blocked = arrayOf(
                "ARMOR_STAND"
        )

        if (event.hasItem() && blocked.any { event.item?.type?.name == it }) {
            event.player.sendMessage("11b11t patches all kind of crash exploits")
            event.isCancelled = true
        }

        // plugin.logger.info("Interact ${event.item?.type?.name}")
    }

    @EventHandler
    fun onBlockPlaceEvent (event: org.bukkit.event.block.BlockPlaceEvent) {
        //plugin.logger.info(event.blockPlaced.location.y.toString())

        val blockedHigh = arrayOf(
                "NETHERRACK",
                "QUARTZ",
                "QUARTZ_BLOCK",
                "QUARTZ_SLAB",
                "QUARTZ_PILLAR")

        if (event.blockPlaced.location.y > 200.0 &&
                blockedHigh.any { event.blockPlaced.type.name == it }) {
            event.player.sendMessage("11b11t patches all kind of crash exploits")
            event.isCancelled = true
        }

        val blocked = arrayOf(
                "DISPENSER",
                "PISTON_STICKY_BASE",
                "REDSTONE_WIRE",
                "DIODE_BLOCK_OFF",
                "REDSTONE_COMPARATOR_OFF",
                "REDSTONE_TORCH_ON",
                "DROPPER",
                "IRON_PLATE",
                "STONE_PLATE",
                "WOOD_PLATE",
                "GOLD_PLATE",
                "IRON_TRAPDOOR",
                "WOOD_BUTTON",
                "STONE_BUTTON",
                "TRIPWIRE_HOOK",
                "PISTON_BASE",
                "LEVER",
                "STONE_PRESSURE_PLATE",
                "OAK_PRESSURE_PLATE",
                "STONE_BUTTON",
                "REDSTONE_LAMP",
                "REDSTONE",
                "REPEATER",
                "COMPARATOR",
                "REDSTONE_BLOCK",
                "OBSERVER",
                "DAYLIGHT_DETECTOR",
                "ARMOR_STAND"
        )

        if (blocked.any { event.blockPlaced.type.name == it }) {
            event.player.sendMessage("11b11t patches all kind of crash exploits")
            event.isCancelled = true
        }

        // plugin.logger.info("Placing ${event.blockPlaced.type.name}")
    }

    @EventHandler
    fun onEntityMount (event: EntityMountEvent) {
        plugin.logger.info("Mounting ${event.mount.type.name}")
    }

    @EventHandler
    fun onEntityMount (event: EntityDismountEvent) {
        plugin.logger.info("Dismount ${event.dismounted.type.name}")
    }

}
