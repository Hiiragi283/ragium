package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.storage.item.HTPotionBundleItemHandler
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack

class HTPotionBundleContainerMenu(
    containerId: Int,
    inventory: Inventory,
    hand: InteractionHand,
    stack: ItemStack,
    isClientSide: Boolean,
) : HTGenericItemContainerMenu(
        RagiumMenuTypes.POTION_BUNDLE,
        containerId,
        inventory,
        hand,
        stack,
        isClientSide,
        1,
    ) {
    override fun createHandler(rows: Int): HTItemHandler = HTGenericContainerRows.createHandler(
        rows,
        canInsert = HTPotionBundleItemHandler::filterPotion,
        filter = HTPotionBundleItemHandler::filterPotion,
    )
}
