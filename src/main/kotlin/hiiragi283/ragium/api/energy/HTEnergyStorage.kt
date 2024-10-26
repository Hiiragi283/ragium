package hiiragi283.ragium.api.energy

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.util.math.Direction

object HTEnergyStorage {
    @JvmField
    val SIDED: BlockApiLookup<Storage<HTEnergyType>, Direction?> = BlockApiLookup.get(
        RagiumAPI.id("sided_energy_storage"),
        Storage.asClass(),
        Direction::class.java,
    )

    @JvmField
    val ITEM: ItemApiLookup<Storage<HTEnergyType>, ContainerItemContext> = ItemApiLookup.get(
        RagiumAPI.id("energy_storage"),
        Storage.asClass(),
        ContainerItemContext::class.java,
    )
}
