package hiiragi283.ragium.impl.storage.capability

import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.capability.HTViewCapability
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.common.extensions.IItemStackExtension
import net.neoforged.neoforge.common.extensions.ILevelExtension

open class HTViewCapabilityBase<HANDLER : Any, ITEM_HANDLER : HANDLER, STACK : ImmutableStack<*, STACK>>(
    blockCapability: BlockCapability<HANDLER, Direction?>,
    itemCapability: ItemCapability<ITEM_HANDLER, Void?>,
    protected val viewGetter: (HANDLER, Direction?) -> List<HTStackView<STACK>>,
) : HTMultiCapabilityBase<HANDLER, ITEM_HANDLER>(blockCapability, itemCapability),
    HTViewCapability<HANDLER, ITEM_HANDLER, STACK> {
    final override fun getCapabilityViews(level: ILevelExtension, pos: BlockPos, side: Direction?): List<HTStackView<STACK>> =
        getCapability(level, pos, side)?.let { viewGetter(it, side) } ?: listOf()

    final override fun getCapabilityViews(stack: IItemStackExtension): List<HTStackView<STACK>> =
        getCapability(stack)?.let { viewGetter(it, null) } ?: listOf()
}
