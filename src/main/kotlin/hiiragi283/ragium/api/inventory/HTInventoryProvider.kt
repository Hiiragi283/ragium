package hiiragi283.ragium.api.inventory

import net.minecraft.inventory.SidedInventory

fun interface HTInventoryProvider {
    fun asInventory(): SidedInventory?
}
