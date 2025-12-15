package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.container.HTItemContainerContext
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.storage.item.HTSimpleItemHandler
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.Items
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
    companion object {
        @JvmStatic
        fun filterPotion(stack: ImmutableItemStack): Boolean = stack.isOf(Items.POTION)
    }

    override fun createHandler(rows: Int): HTItemHandler = HTSimpleItemHandler.create(
        rows,
    ) { x: Int, y: Int -> HTBasicItemSlot.create(null, x, y, filter = ::filterPotion) }
}
