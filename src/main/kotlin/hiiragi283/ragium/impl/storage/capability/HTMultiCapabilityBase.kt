package hiiragi283.ragium.impl.storage.capability

import hiiragi283.ragium.api.storage.capability.HTMultiCapability
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability

/**
 * [HTMultiCapability]の実装クラス
 * @see [hiiragi283.ragium.api.storage.capability.RagiumCapabilities]
 */
open class HTMultiCapabilityBase<HANDLER : Any, ITEM_HANDLER : HANDLER>(
    override val block: BlockCapability<HANDLER, Direction?>,
    override val entity: EntityCapability<HANDLER, Direction?>,
    override val item: ItemCapability<ITEM_HANDLER, Void?>,
) : HTMultiCapability<HANDLER, ITEM_HANDLER> {
    companion object {
        @JvmStatic
        inline fun <reified HANDLER : Any> createSimple(id: ResourceLocation): HTMultiCapabilityBase<HANDLER, HANDLER> = create(id)

        @JvmStatic
        inline fun <reified HANDLER : Any, reified ITEM_HANDLER : HANDLER> create(
            id: ResourceLocation,
        ): HTMultiCapabilityBase<HANDLER, ITEM_HANDLER> = HTMultiCapabilityBase(
            BlockCapability.createSided(id, HANDLER::class.java),
            EntityCapability.createSided(id, HANDLER::class.java),
            ItemCapability.createVoid(id, ITEM_HANDLER::class.java),
        )
    }
}
