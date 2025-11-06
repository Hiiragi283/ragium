package hiiragi283.ragium.api.inventory.container.type

import hiiragi283.ragium.api.inventory.container.HTItemContainerContext
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuConstructor
import net.neoforged.api.distmarker.Dist

/**
 * [InteractionHand]と[ImmutableItemStack]を引数にとる[MenuConstructor]の代替インターフェース
 * @see mekanism.common.inventory.container.type.MekanismItemContainerType.IMekanismItemContainerFactory
 */
fun interface HTItemContainerFactory<MENU : AbstractContainerMenu> {
    fun create(
        containerId: Int,
        inventory: Inventory,
        context: HTItemContainerContext,
        isClientSide: Dist,
    ): MENU

    fun create(
        containerId: Int,
        inventory: Inventory,
        hand: InteractionHand?,
        stack: ImmutableItemStack,
        isClientSide: Dist,
    ): MENU = create(containerId, inventory, HTItemContainerContext(hand, stack), isClientSide)
}
