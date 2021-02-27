package com._11b11t.plugin11b11t

import com._11b11t.plugin11b11t.commands.ReloadCommand
import com._11b11t.plugin11b11t.commands.VanishCommand
import com._11b11t.plugin11b11t.events.ToolListener
import org.bukkit.plugin.java.JavaPlugin

class NullBNullTPatch : JavaPlugin() {
    companion object {
        var INSTANCE: NullBNullTPatch? = null
    }

    override fun onEnable() {
        INSTANCE = this

        getCommand("11breload")?.setExecutor(ReloadCommand(this))
       // getCommand("11bvanish")?.setExecutor(VanishCommand(this))

        server.pluginManager.registerEvents(ToolListener(this), this)

        super.onEnable()
    }
}