package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.HTPipeType
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.registry.RegistryWrapper
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTTransporterBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(type, pos, state),
    SidedStorageBlockEntity {
    protected var tier: HTMachineTier = HTMachineTier.PRIMITIVE
    protected var type: HTPipeType = HTPipeType.NONE

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        HTMachineTier.CODEC
            .encodeStart(NbtOps.INSTANCE, tier)
            .ifSuccess { nbt.put("tier", it) }
        HTPipeType.CODEC
            .encodeStart(NbtOps.INSTANCE, type)
            .ifSuccess { nbt.put("type", it) }
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        HTMachineTier.CODEC
            .parse(NbtOps.INSTANCE, nbt.get("tier"))
            .ifSuccess { tier = it }
        HTPipeType.CODEC
            .parse(NbtOps.INSTANCE, nbt.get("type"))
            .ifSuccess { type = it }
    }

    final override val tickRate: Int = 20

    protected val front: Direction
        get() = cachedState.get(Properties.FACING)

    protected fun <A : Any> getBackStorage(world: World, pos: BlockPos, lookup: BlockApiLookup<A, Direction?>): A? {
        val posFrom: BlockPos = pos.offset(front.opposite)
        return lookup.find(world, posFrom, front)
    }

    protected fun <A : Any> getFrontStorage(world: World, pos: BlockPos, lookup: BlockApiLookup<A, Direction?>): A? {
        val posTo: BlockPos = pos.offset(front)
        return lookup.find(world, posTo, front.opposite)
    }
}
