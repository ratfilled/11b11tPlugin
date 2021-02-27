package com._11b11t.plugin11b11t.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class ReloadCommand(val plugin: JavaPlugin): CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isNotEmpty() && args[0] == "reload") {
            plugin.reloadConfig()
            plugin.logger.info("Plugin 11b11t reloaded")
        }
        return true
    }
}