package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.HTPipeType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTPipeBlockEntity(pos: BlockPos, state: BlockState) :
    HTTransporterBlockEntityBase(RagiumBlockEntityTypes.FLUID_PIPE, pos, state) {
    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier, type: HTPipeType) : this(pos, state) {
        this.tier = tier
        this.type = type
    }

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        itemStorage.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        itemStorage.readNbt(nbt, wrapperLookup)
        fluidStorage.readNbt(nbt, wrapperLookup)
    }

    fun canConnect(dir: Direction): Boolean = ifPresentWorld { world: World ->
        HTPipeType.canConnect(world, pos, dir, type)
    } ?: false

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        if (world.isClient) return
        // export containment
        if (type.isItem) {
            StorageUtil.move(
                itemStorage,
                getFrontStorage(world, pos, state, ItemStorage.SIDED),
                { true },
                type.getItemCount(tier),
                null,
            )
        }
        if (type.isFluid) {
            StorageUtil.move(
                fluidStorage,
                getFrontStorage(world, pos, state, FluidStorage.SIDED),
                { true },
                type.getFluidCount(tier),
                null,
            )
        }
    }

    //    SidedStorageBlockEntity    //

    private val itemStorage: SingleItemStorage = object : SingleItemStorage() {
        override fun getCapacity(variant: ItemVariant): Long = 64
    }

    private val fluidStorage: SingleFluidStorage = object : SingleFluidStorage() {
        override fun getCapacity(variant: FluidVariant): Long = FluidConstants.BUCKET * 16
    }

    override fun getItemStorage(side: Direction?): Storage<ItemVariant>? = if (type.isItem) itemStorage else null

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant>? = if (type.isFluid) fluidStorage else null
}
