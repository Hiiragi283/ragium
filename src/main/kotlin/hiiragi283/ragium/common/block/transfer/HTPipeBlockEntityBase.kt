package hiiragi283.ragium.common.block.transfer

import hiiragi283.ragium.api.extension.fluidStorageOf
import hiiragi283.ragium.api.extension.ifPresentWorld
import hiiragi283.ragium.api.util.HTPipeType
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.state.property.Properties
import net.minecraft.util.ItemScatterer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTPipeBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTTransporterBlockEntityBase(type, pos, state),
    SidedStorageBlockEntity {
    final override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        itemStorage.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    final override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        itemStorage.readNbt(nbt, wrapperLookup)
        fluidStorage.readNbt(nbt, wrapperLookup)
    }

    fun canConnect(dir: Direction): Boolean = ifPresentWorld { world: World ->
        HTPipeType.canConnect(world, pos, dir, type)
    } == true

    final override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean,
    ) {
        ItemScatterer.spawn(
            world,
            pos.x.toDouble(),
            pos.y.toDouble(),
            pos.z.toDouble(),
            itemStorage.variant.toStack(itemStorage.amount.toInt()),
        )
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    protected open val front: Direction
        get() = cachedState.get(Properties.FACING)

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        if (world.isClient) return
        // export containment
        if (type.isItem) {
            StorageUtil.move(
                itemStorage,
                getFrontItemStorage(world, pos, front),
                { true },
                type.getItemCount(tier),
                null,
            )
        }
        if (type.isFluid) {
            StorageUtil.move(
                fluidStorage,
                getFrontFluidStorage(world, pos, front),
                { true },
                type.getFluidCount(tier),
                null,
            )
        }
    }

    protected fun moveItem(world: World, pos: BlockPos, front: Direction): Boolean = StorageUtil.move(
        itemStorage,
        getFrontItemStorage(world, pos, front),
        { true },
        type.getItemCount(tier),
        null,
    ) > 0

    protected fun moveFluid(world: World, pos: BlockPos, front: Direction): Boolean = StorageUtil.move(
        fluidStorage,
        getFrontFluidStorage(world, pos, front),
        { true },
        type.getFluidCount(tier),
        null,
    ) > 0

    //    SidedStorageBlockEntity    //

    val itemStorage: SingleItemStorage =
        object : SingleItemStorage() {
            override fun getCapacity(variant: ItemVariant): Long = 64
        }

    val fluidStorage: SingleFluidStorage = fluidStorageOf(FluidConstants.BUCKET * 16)

    override fun getItemStorage(side: Direction?): Storage<ItemVariant>? = if (type.isItem && side != front) itemStorage else null

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant>? = if (type.isFluid && side != front) fluidStorage else null
}
