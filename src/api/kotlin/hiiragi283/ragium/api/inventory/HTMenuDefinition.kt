package hiiragi283.ragium.api.inventory

import com.google.common.base.Functions
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemStackHandler
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

data class HTMenuDefinition(val inventory: HTItemHandler, val upgrades: IItemHandler, val containerData: ContainerData) {
    companion object {
        @JvmStatic
        fun empty(size: Int): HTMenuDefinition = HTMenuDefinition(
            HTItemStackHandler(size, Functions.constant(Unit)::apply),
            ItemStackHandler(4),
            SimpleContainerData(2),
        )
    }

    fun getData(index: Int): Int = containerData.get(index)
}
