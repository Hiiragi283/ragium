package hiiragi283.ragium.impl.storage.capability

import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.capability.HTViewCapability
import hiiragi283.ragium.api.storage.capability.HTViewProvider
import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability

open class HTViewCapabilityBase<HANDLER : Any, ITEM_HANDLER : HANDLER, STACK : ImmutableStack<*, STACK>>(
    blockCapability: BlockCapability<HANDLER, Direction?>,
    entityCapability: EntityCapability<HANDLER, Direction?>,
    itemCapability: ItemCapability<ITEM_HANDLER, Void?>,
    private val provider: HTViewProvider<HANDLER, Direction, STACK>,
) : HTMultiCapabilityBase<HANDLER, ITEM_HANDLER>(blockCapability, entityCapability, itemCapability),
    HTViewCapability<HANDLER, ITEM_HANDLER, STACK> {
    final override fun apply(handler: HANDLER, side: Direction?): List<HTStackView<STACK>> = provider.apply(handler, side)
}
