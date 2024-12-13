package hiiragi283.ragium.api.machine.block

import hiiragi283.ragium.api.extension.interactWithFluidStorage
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockManager
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPatternProvider
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.api.util.HTUnitResult
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTLargeMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTRecipeProcessorBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(type, pos, state),
    HTFluidSyncable {
    final override fun process(world: World, pos: BlockPos): HTUnitResult = when (this) {
        is HTMultiblockPatternProvider -> multiblockManager.updateValidation(cachedState)
        else -> HTUnitResult.success()
    }.flatMap { processor.process(world, key, tier) }

    protected abstract val inventory: SidedInventory

    protected abstract val fluidStorage: HTMachineFluidStorage

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

    //    HTFluidSyncable    //

    final override fun sendPacket(player: ServerPlayerEntity, handler: HTFluidSyncable.Handler) {
        fluidStorage.sendPacket(player, handler)
    }

    //    SidedStorageBlockEntity    //

    final override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage

    //    Large    //

    abstract class Large(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTRecipeProcessorBlockEntityBase(type, pos, state),
        HTMultiblockPatternProvider {
        final override val multiblockManager = HTMultiblockManager(::getWorld, pos, this)

        final override val inventory: SidedInventory = HTMachineInventory.ofLarge()

        final override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.ofSmall(this)

        override val processor = HTMachineRecipeProcessor(
            inventory,
            intArrayOf(0, 1, 2),
            intArrayOf(4, 5, 6),
            3,
            fluidStorage,
            intArrayOf(0, 1),
            intArrayOf(2, 3),
        )

        final override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
            HTLargeMachineScreenHandler(syncId, playerInventory, createContext())
    }
}
