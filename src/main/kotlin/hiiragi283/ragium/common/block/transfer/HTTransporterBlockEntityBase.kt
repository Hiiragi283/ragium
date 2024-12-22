package hiiragi283.ragium.common.block.transfer

import hiiragi283.ragium.api.block.HTBlockEntityBase
import hiiragi283.ragium.api.data.HTNbtCodecs
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.util.HTPipeType
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTTransporterBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(type, pos, state),
    HTMachineTierProvider {
    override var tier: HTMachineTier = HTMachineTier.PRIMITIVE
        protected set
    protected var type: HTPipeType = HTPipeType.NONE

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        HTNbtCodecs.MACHINE_TIER.writeTo(nbt, tier)
        HTNbtCodecs.PIPE_TYPE.writeTo(nbt, type)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        HTNbtCodecs.MACHINE_TIER.readAndSet(nbt) { newTier: HTMachineTier ->
            val oldTier: HTMachineTier = tier
            tier = newTier
            onTierUpdated(oldTier, tier)
        }
        HTNbtCodecs.PIPE_TYPE.readAndSet(nbt, this::type)
    }

    open fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {}

    final override val tickRate: Int = 20

    protected fun getBackItemStorage(world: World, pos: BlockPos, front: Direction): Storage<ItemVariant>? {
        val posFrom: BlockPos = pos.offset(front.opposite)
        return ItemStorage.SIDED.find(world, posFrom, front)
    }

    protected fun getBackFluidStorage(world: World, pos: BlockPos, front: Direction): Storage<FluidVariant>? {
        val posFrom: BlockPos = pos.offset(front.opposite)
        return FluidStorage.SIDED.find(world, posFrom, front)
    }

    protected fun getFrontItemStorage(world: World, pos: BlockPos, front: Direction): Storage<ItemVariant>? {
        val posTo: BlockPos = pos.offset(front)
        return ItemStorage.SIDED.find(world, posTo, front.opposite)
    }

    protected fun getFrontFluidStorage(world: World, pos: BlockPos, front: Direction): Storage<FluidVariant>? {
        val posTo: BlockPos = pos.offset(front)
        return FluidStorage.SIDED.find(world, posTo, front.opposite)
    }
}
