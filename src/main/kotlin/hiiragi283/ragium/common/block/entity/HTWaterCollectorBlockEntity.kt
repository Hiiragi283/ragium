package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.util.useTransaction
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.fluid.Fluids
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.FluidTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTWaterCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTBaseBlockEntity(TODO(), pos, state),
    SidedStorageBlockEntity {
    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, registryLookup)
        SingleFluidStorage.writeNbt(internalTank, FluidVariant.CODEC, nbt, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)
        SingleFluidStorage.readNbt(internalTank, FluidVariant.CODEC, FluidVariant::blank, nbt, registryLookup)
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        val sideWaters: Int =
            Direction.entries
                .filterNot { it.axis == Direction.Axis.Y }
                .map(pos::offset)
                .map(world::getFluidState)
                .count { it.isIn(FluidTags.WATER) }
        val amount: Long =
            FluidConstants.BUCKET *
                when (sideWaters) {
                    2 -> 1.0
                    3 -> 1.5
                    4 -> 2.0
                    else -> 0.0
                }.toLong()
        useTransaction { transaction: Transaction ->
            internalTank.insert(
                FluidVariant.of(Fluids.WATER),
                amount,
                transaction,
            )
            transaction.commit()
        }
    }

    //    SidedStorageBlockEntity    //

    private val internalTank: SingleFluidStorage =
        object : SingleFluidStorage() {
            override fun getCapacity(variant: FluidVariant): Long = FluidConstants.BUCKET * 8

            override fun canInsert(variant: FluidVariant): Boolean = variant.isOf(Fluids.WATER)

            override fun onFinalCommit() {
                markDirty()
            }
        }

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant>? = when (side) {
        Direction.UP -> internalTank
        null -> internalTank
        else -> null
    }
}
