package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.util.getOrDefault
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.SimpleSidedEnergyContainer

class HTBufferBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.BUFFER, pos, state),
    HTDelegatedInventory,
    HTEnergyStorageHolder {
    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, registryLookup)
        nbt.putLong("energy", energyStorageContainer.amount)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)
        energyStorageContainer.amount = nbt.getLong("energy")
    }

    //    HTDelegatedInventory    //

    override val parent: HTSidedInventory
        get() = HTSidedStorageBuilder(9)
            .setAll(HTStorageIO.GENERIC, HTStorageSide.ANY, 0..8)
            .buildSided()

    override fun markDirty() {
        super<HTBlockEntityBase>.markDirty()
    }

    //    HTEnergyStorageHolder    //

    private var energyStorageContainer: SimpleSidedEnergyContainer = object : SimpleSidedEnergyContainer() {
        override fun getCapacity(): Long = HTMachineTier.BASIC.energyCapacity

        override fun getMaxInsert(side: Direction?): Long {
            val facing: Direction = cachedState.getOrDefault(Properties.FACING, Direction.NORTH)
            return if (side == null || side != facing) capacity else 0
        }

        override fun getMaxExtract(side: Direction?): Long {
            val facing: Direction = cachedState.getOrDefault(Properties.FACING, Direction.NORTH)
            return if (side == null || side == facing) capacity else 0
        }
    }

    override fun getEnergyStorage(direction: Direction?): EnergyStorage = energyStorageContainer.getSideStorage(direction)
}
