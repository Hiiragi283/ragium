package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.container.HTItemContainerContext
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.storage.item.HTPotionBundleItemHandler
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist

class HTPotionBundleContainerMenu(
    containerId: Int,
    inventory: Inventory,
    context: HTItemContainerContext,
    isClientSide: Dist,
) : HTGenericItemContainerMenu(
        RagiumMenuTypes.POTION_BUNDLE,
        containerId,
        inventory,
        context,
        isClientSide,
        1,
    ) {
    override fun createHandler(rows: Int): HTItemHandler = HTGenericContainerRows.createHandler(
        rows,
        canInsert = HTPotionBundleItemHandler::filterPotion,
        filter = HTPotionBundleItemHandler::filterPotion,
    )
}
