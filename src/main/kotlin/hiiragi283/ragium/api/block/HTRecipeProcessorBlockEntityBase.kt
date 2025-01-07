package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
import hiiragi283.ragium.api.screen.HTScreenFluidProvider
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.api.util.HTUnitResult
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

/**
 * レシピを用いた加工を行う機械
 */
abstract class HTRecipeProcessorBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(type, pos, state),
    HTScreenFluidProvider {
    final override fun process(world: World, pos: BlockPos): HTUnitResult = when (this) {
        is HTMultiblockProvider -> multiblockManager.updateValidation(cachedState)
        else -> HTUnitResult.success()
    }.flatMap { processor.process(world, machineKey, tier) }

    /**
     * 機械のインベントリ
     */
    protected abstract val inventory: HTMachineInventory

    /**
     * 機械の液体ストレージ
     */
    protected abstract val fluidStorage: HTMachineFluidStorage

    /**
     * レシピの処理部分
     */
    protected abstract val processor: HTRecipeProcessor

    final override fun asInventory(): SidedInventory = inventory

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

    final override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactWithFluidStorage(player)

    //    HTScreenFluidProvider    //

    final override fun getFluidsToSync(): Map<Int, HTFluidVariantStack> = fluidStorage.getFluidsToSync()

    //    SidedStorageBlockEntity    //

    final override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.wrapStorage()
}
