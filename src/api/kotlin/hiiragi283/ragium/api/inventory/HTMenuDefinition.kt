package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.world.inventory.ContainerData
import net.neoforged.neoforge.items.IItemHandler

data class HTMenuDefinition(val inventory: HTItemHandler, val upgrades: IItemHandler, val containerData: ContainerData) {
    companion object {
        @JvmStatic
        fun empty(size: Int): HTMenuDefinition = RagiumAPI.getInstance().createEmptyMenuDefinition(size)
    }

    fun getData(index: Int): Int = containerData.get(index)
}
