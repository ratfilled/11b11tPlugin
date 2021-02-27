import org.bukkit.inventory.ItemStack

fun main(args: Array<String>) {
    println("Hello World!")

    var itemMap = mapOf("id" to "stone")

    var item = ItemStack.deserialize(itemMap);


}