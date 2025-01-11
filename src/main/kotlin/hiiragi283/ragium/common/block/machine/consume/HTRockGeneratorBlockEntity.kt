package hiiragi283.ragium.common.block.machine.consume

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.aroundPos
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.screen.HTScreenFluidProvider
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.api.util.HTMachineException
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.processor.HTMachineRecipeProcessor
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.FluidTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTRockGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.ROCK_GENERATOR, pos, state),
    HTScreenFluidProvider {
    override var machineKey: HTMachineKey = RagiumMachineKeys.ROCK_GENERATOR

    val inventory: HTMachineInventory = HTMachineInventory(2, intArrayOf(), 0, intArrayOf(1))

    val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.ofSmall(this)

    val processor: HTMachineRecipeProcessor = HTMachineRecipeProcessor(machineKey, inventory, fluidStorage)

    override fun asInventory(): SidedInventory = inventory

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage.readNbt(nbt, wrapperLookup, tier)
    }

    override fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
        fluidStorage.update(newTier)
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactWithFluidStorage(player)

    override fun process(world: World, pos: BlockPos) {
        when {
            pos.aroundPos.none { posIn: BlockPos ->
                world.getFluidState(posIn).isIn(FluidTags.WATER)
            } -> throw HTMachineException.AroundBlock(false, Blocks.WATER, 1)

            pos.aroundPos.none { posIn: BlockPos ->
                world.getFluidState(posIn).isIn(FluidTags.LAVA)
            } -> throw HTMachineException.AroundBlock(false, Blocks.LAVA, 1)

            else -> processor.process(world, machineKey, tier)
        }
    }

    //    HTScreenFluidProvider    //

    override fun getFluidsToSync(): Map<Int, HTFluidVariantStack> = fluidStorage.getFluidsToSync()

    //    SidedStorageBlockEntity    //

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.wrapStorage()
}
