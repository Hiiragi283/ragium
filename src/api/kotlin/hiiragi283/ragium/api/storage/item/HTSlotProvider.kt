package hiiragi283.ragium.api.storage.item

import net.minecraft.world.inventory.ContainerData
import net.neoforged.neoforge.items.IItemHandler

interface HTSlotProvider {
    fun addInputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit)

    fun addOutputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit)

    fun addContainerData(consumer: (ContainerData) -> Unit) {}

    interface Empty : HTSlotProvider {
        override fun addInputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {}

        override fun addOutputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {}
    }
}
