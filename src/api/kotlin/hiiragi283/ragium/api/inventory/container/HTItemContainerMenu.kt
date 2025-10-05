package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.util.Optional

/**
 * [InteractionHand]と[ItemStack]を受け取る[HTContainerMenu]の拡張クラス
 * @see [mekanism.common.inventory.container.item.MekanismItemContainer]
 */
abstract class HTItemContainerMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    context: HTItemContainerContext,
) : HTContainerMenu(
        menuType,
        containerId,
        inventory,
    ) {
    protected val hand: Optional<InteractionHand> = context.hand
    protected val stack: ItemStack = context.stack

    override fun stillValid(player: Player): Boolean {
        if (stack.isEmpty) return false
        return hand
            .map { interactionHand: InteractionHand ->
                player.getItemInHand(interactionHand).`is`(stack.item)
            }.orElseGet {
                RagiumPlatform.INSTANCE.getAccessoryCap(player)?.getFirstEquipped(stack.item) != null
            }
    }
}
