package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.api.extension.isOf
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.fabricmc.fabric.api.transfer.v1.storage.base.BlankVariantView
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.component.ComponentChanges

class HTEmptyFluidCubeStorage(val context: ContainerItemContext) : InsertionOnlyStorage<FluidVariant> {
    private val blankView: MutableList<StorageView<FluidVariant>> =
        mutableListOf(BlankVariantView(FluidVariant.blank(), FluidConstants.BUCKET))

    override fun insert(resource: FluidVariant, maxAmount: Long, transaction: TransactionContext): Long {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount)
        if (!context.itemVariant.isOf(RagiumContents.Misc.EMPTY_FLUID_CUBE)) return 0
        if (maxAmount >= FluidConstants.BUCKET) {
            val newVariant: ItemVariant = ItemVariant.of(
                RagiumContents.Misc.FILLED_FLUID_CUBE,
                ComponentChanges
                    .builder()
                    .add(RagiumComponentTypes.FLUID, resource.fluid)
                    .build(),
            )
            if (context.exchange(newVariant, 1, transaction) == 1L) {
                return FluidConstants.BUCKET
            }
        }
        return 0
    }

    override fun iterator(): MutableIterator<StorageView<FluidVariant>> = blankView.iterator()
}
