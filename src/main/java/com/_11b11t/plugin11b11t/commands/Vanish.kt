package com._11b11t.plugin11b11t.commands

import com._11b11t.plugin11b11t.NullBNullTPatch
import org.bukkit.entity.Player
import org.bukkit.command.CommandSender
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.plugin.java.JavaPlugin
import java.util.ArrayList


class VanishCommand(val plugin: JavaPlugin): CommandExecutor {
    var vanished = ArrayList<String>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (exists(sender)) {
                rem(sender)
                vanish(sender, false)
                sender.sendMessage(ChatColor.RED.toString() + "Now people can see you.")
                return true
            }
            add(sender)
            vanish(sender, true)
            sender.sendMessage(ChatColor.GREEN.toString() + "Now you are invisible to everyone.")
        }

        return true
    }

    private fun exists(target: Player): Boolean {
        for (player in vanished) if (player.contentEquals(target.name)) return true
        return false
    }

    private fun add(player: Player) {
        if (!exists(player)) vanished.add(player.name)
    }

    operator fun rem(player: Player) {
        if (exists(player)) vanished.remove(player.name)
    }

    private fun vanish(target: Player?, shouldVanish: Boolean) {
        for (player in Bukkit.getOnlinePlayers())
            if (shouldVanish)
                player.hidePlayer(NullBNullTPatch.INSTANCE!!, target!!)
            else
                player.showPlayer(NullBNullTPatch.INSTANCE!!, target!!)
    }
}