package hiiragi283.ragium.api.inventory.container.type

import hiiragi283.ragium.api.inventory.container.HTItemContainerContext
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuConstructor
import net.minecraft.world.item.ItemStack

/**
 * [InteractionHand]と[ItemStack]を引数にとる[MenuConstructor]の代替インターフェース
 * @see [mekanism.common.inventory.container.type.MekanismItemContainerType.IMekanismItemContainerFactory]
 */
fun interface HTItemContainerFactory<MENU : AbstractContainerMenu> {
    fun create(
        containerId: Int,
        inventory: Inventory,
        context: HTItemContainerContext,
        isClientSide: Boolean,
    ): MENU

    fun create(
        containerId: Int,
        inventory: Inventory,
        hand: InteractionHand?,
        stack: ItemStack,
        isClientSide: Boolean,
    ): MENU = create(containerId, inventory, HTItemContainerContext(hand, stack), isClientSide)
}
