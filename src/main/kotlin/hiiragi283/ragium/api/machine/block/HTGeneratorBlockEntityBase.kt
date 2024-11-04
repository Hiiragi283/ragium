package hiiragi283.ragium.api.machine.block

import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTGeneratorBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(type, pos, state) {
    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        val energy: Long = generateEnergy(world, pos)
        if (energy > 0) {
            useTransaction { transaction: Transaction ->
                val inserted: Long = world.energyNetwork?.insert(energy, transaction) ?: 0
                if (inserted > 0) {
                    onSucceeded(world, pos)
                    transaction.commit()
                } else {
                    onFailed(world, pos)
                    transaction.abort()
                }
            }
        }
    }

    abstract fun generateEnergy(world: World, pos: BlockPos): Long

    open fun onSucceeded(world: World, pos: BlockPos) {}

    open fun onFailed(world: World, pos: BlockPos) {}

    //    Simple    //

    class Simple(pos: BlockPos, state: BlockState) : HTGeneratorBlockEntityBase(RagiumBlockEntityTypes.SIMPLE_GENERATOR, pos, state) {
        override var key: HTMachineKey = RagiumMachineKeys.SOLAR_PANEL

        constructor(pos: BlockPos, state: BlockState, key: HTMachineKey, tier: HTMachineTier) : this(pos, state) {
            this.key = key
            this.tier = tier
        }

        override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
            super.writeNbt(nbt, wrapperLookup)
            nbt.putMachineKey(MACHINE_KEY, key)
            nbt.putTier(TIER_KEY, tier)
        }

        override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
            super.readNbt(nbt, wrapperLookup)
            key = nbt.getMachineKey(MACHINE_KEY)
            tier = nbt.getTier(TIER_KEY)
        }

        override fun interactWithFluidStorage(player: PlayerEntity): Boolean = false
        
        override fun generateEnergy(world: World, pos: BlockPos): Long = when {
            key.asProperties().getOrDefault(HTMachinePropertyKeys.GENERATOR_PREDICATE)(
                world,
                pos,
            ) -> tier.recipeCost

            else -> 0
        }

        override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? = null
    }
}
