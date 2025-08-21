package hiiragi283.ragium.api.inventory.container.type

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu

/**
 * @see [mekanism.common.inventory.container.type.MekanismContainerType.IMekanismContainerFactory]
 */
fun interface HTContainerFactory<MENU : AbstractContainerMenu, C> {
    fun create(containerId: Int, inventory: Inventory, context: C): MENU
}
