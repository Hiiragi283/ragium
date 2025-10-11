package hiiragi283.ragium.api.storage.capability

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.common.extensions.IItemStackExtension
import net.neoforged.neoforge.common.extensions.ILevelExtension

/**
 * [HTMultiCapability]の実装クラス
 * @see [RagiumCapabilities]
 */
open class HTMultiCapabilityBase<HANDLER : Any, ITEM_HANDLER : HANDLER, SLOTTED_HANDLER : HANDLER, SLOT : Any>(
    protected val blockCapability: BlockCapability<HANDLER, Direction?>,
    protected val itemCapability: ItemCapability<ITEM_HANDLER, Void?>,
    protected val slottedWrapper: (HANDLER) -> SLOTTED_HANDLER,
    protected val slotProvider: (SLOTTED_HANDLER, Direction?) -> List<SLOT>,
) : HTMultiCapability<HANDLER, ITEM_HANDLER, SLOTTED_HANDLER, SLOT> {
    final override fun blockCapability(): BlockCapability<HANDLER, Direction?> = blockCapability

    final override fun itemCapability(): ItemCapability<ITEM_HANDLER, Void?> = itemCapability

    final override fun getSlottedCapability(level: ILevelExtension, pos: BlockPos, side: Direction?): SLOTTED_HANDLER? =
        getCapability(level, pos, side)?.let(slottedWrapper)

    final override fun getCapabilitySlots(level: ILevelExtension, pos: BlockPos, side: Direction?): List<SLOT> =
        getSlottedCapability(level, pos, side)?.let { slotProvider(it, side) } ?: listOf()

    final override fun getSlottedCapability(stack: IItemStackExtension): SLOTTED_HANDLER? = getCapability(stack)?.let(slottedWrapper)

    final override fun getCapabilitySlots(stack: IItemStackExtension): List<SLOT> =
        getSlottedCapability(stack)?.let { slotProvider(it, null) } ?: listOf()
}
