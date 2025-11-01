package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.items.IItemHandler

data object HTItemCapabilities : HTStackViewCapability.Simple<IItemHandler, ImmutableItemStack> {
    override val block: BlockCapability<IItemHandler, Direction?> = Capabilities.ItemHandler.BLOCK
    override val entity: EntityCapability<IItemHandler, Direction?> = Capabilities.ItemHandler.ENTITY_AUTOMATION
    override val item: ItemCapability<IItemHandler, Void?> = Capabilities.ItemHandler.ITEM

    override fun apply(handler: IItemHandler, context: Direction?): List<HTStackView<ImmutableItemStack>> = if (handler is HTItemHandler) {
        handler.getItemSlots(context)
    } else {
        handler.slotRange.map { slot: Int ->
            object : HTStackView<ImmutableItemStack> {
                override fun getStack(): ImmutableItemStack? = handler.getStackInSlot(slot).toImmutable()

                override fun getCapacity(stack: ImmutableItemStack?): Int = handler.getSlotLimit(slot)
            }
        }
    }

    override fun getCapability(entity: Entity, side: Direction?): IItemHandler? {
        if (side == null) {
            val handler: IItemHandler? = entity.getCapability(entityAlt)
            if (handler != null) return handler
        }
        return super.getCapability(entity, side)
    }

    val entityAlt: EntityCapability<IItemHandler, Void?> = Capabilities.ItemHandler.ENTITY
}
