package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.tag.RagiumModTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import java.util.Optional

/**
 * [InteractionHand]と[ImmutableItemStack]を受け取る[HTContainerMenu]の拡張クラス
 * @see [mekanism.common.inventory.container.item.MekanismItemContainer]
 */
abstract class HTItemContainerMenu(
    menuType: HTDeferredMenuType.OnHand<*>,
    containerId: Int,
    inventory: Inventory,
    context: HTItemContainerContext,
) : HTContainerMenu(
        menuType,
        containerId,
        inventory,
    ) {
    protected val hand: Optional<InteractionHand> = context.hand
    protected val stack: ImmutableItemStack = context.stack

    override fun stillValid(player: Player): Boolean = hand
        .map { interactionHand: InteractionHand ->
            stack.isOf(player.getItemInHand(interactionHand).item)
        }.orElseGet {
            stack.isOf(RagiumModTags.Items.BYPASS_MENU_VALIDATION)
        }
}
