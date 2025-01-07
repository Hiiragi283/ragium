package hiiragi283.ragium.api.block.entity

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

abstract class HTCrateBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(type, pos, state),
    SidedStorageBlockEntity {
    abstract val itemStorage: SingleSlotStorage<ItemVariant>
    abstract val showCount: Boolean

    //    SidedStorageBlockEntity    //

    final override fun getItemStorage(side: Direction?): Storage<ItemVariant>? = itemStorage
}
