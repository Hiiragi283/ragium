package hiiragi283.ragium.api.inventory.container.type

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuConstructor

/**
 * [C]を引数にとる[MenuConstructor]の代替インターフェース
 * @see [mekanism.common.inventory.container.type.MekanismContainerType.IMekanismContainerFactory]
 */
fun interface HTContainerFactory<MENU : AbstractContainerMenu, C> {
    fun create(containerId: Int, inventory: Inventory, context: C): MENU
}
