package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

data class HTMenuDefinition(
    private val inputSlots: List<HTItemSlot>,
    private val outputSlots: List<HTItemSlot>,
    val upgrades: IItemHandler,
    val containerData: ContainerData,
) {
    companion object {
        @JvmField
        val EMPTY = HTMenuDefinition(
            listOf(),
            listOf(),
            ItemStackHandler(4),
            SimpleContainerData(2),
        )
    }

    fun getInputSlot(index: Int): HTItemSlot = inputSlots.getOrNull(index) ?: HTItemSlot.create("")

    fun getOutputSlot(index: Int): HTItemSlot = outputSlots.getOrNull(index) ?: HTItemSlot.create("")

    fun getData(index: Int): Int = containerData.get(index)
}
