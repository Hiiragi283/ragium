package hiiragi283.ragium.impl.storage.capability

import hiiragi283.ragium.api.storage.capability.HTMultiCapability
import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.ItemCapability

/**
 * [hiiragi283.ragium.api.storage.capability.HTMultiCapability]の実装クラス
 * @see [hiiragi283.ragium.api.storage.capability.RagiumCapabilities]
 */
open class HTMultiCapabilityBase<HANDLER : Any, ITEM_HANDLER : HANDLER>(
    protected val blockCapability: BlockCapability<HANDLER, Direction?>,
    protected val itemCapability: ItemCapability<ITEM_HANDLER, Void?>,
) : HTMultiCapability<HANDLER, ITEM_HANDLER> {
    final override fun blockCapability(): BlockCapability<HANDLER, Direction?> = blockCapability

    final override fun itemCapability(): ItemCapability<ITEM_HANDLER, Void?> = itemCapability
}
