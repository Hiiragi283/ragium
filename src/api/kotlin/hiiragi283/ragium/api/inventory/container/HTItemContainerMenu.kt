package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

/**
 * [InteractionHand]と[ItemStack]を受け取る[HTContainerMenu]の拡張クラス
 * @see [mekanism.common.inventory.container.item.MekanismItemContainer]
 */
abstract class HTItemContainerMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    private val hand: InteractionHand,
    private val stack: ItemStack,
) : HTContainerMenu(
        menuType,
        containerId,
        inventory,
    ) {
    override fun stillValid(player: Player): Boolean = when {
        stack.isEmpty -> false
        !player.getItemInHand(hand).`is`(stack.item) -> {
            RagiumAPI.getInstance().getAccessoryCap(player)?.getFirstEquipped(stack.item) != null
        }

        else -> true
    }
}
