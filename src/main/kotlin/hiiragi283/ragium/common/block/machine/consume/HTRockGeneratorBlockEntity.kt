package hiiragi283.ragium.common.block.machine.consume

import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.aroundPos
import hiiragi283.ragium.api.extension.createContext
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.screen.HTScreenFluidProvider
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.api.util.HTUnitResult
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTRockGeneratorScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.FluidTags
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTRockGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.ROCK_GENERATOR, pos, state),
    HTScreenFluidProvider {
    override var machineKey: HTMachineKey = RagiumMachineKeys.ROCK_GENERATOR

    val inventory: HTMachineInventory = HTMachineInventory.Builder(2).output(1).build()

    val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.ofSmall(this)

    val processor = HTMachineRecipeProcessor(
        inventory,
        intArrayOf(),
        intArrayOf(1),
        0,
        fluidStorage,
        intArrayOf(0),
        intArrayOf(1),
    )

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

    override fun process(world: World, pos: BlockPos): HTUnitResult = when {
        pos.aroundPos.none { posIn: BlockPos ->
            world.getFluidState(posIn).isIn(FluidTags.WATER)
        } -> HTUnitResult.errorString { "Require one water source at least around Rock Generator!" }

        pos.aroundPos.none { posIn: BlockPos ->
            world.getFluidState(posIn).isIn(FluidTags.LAVA)
        } -> HTUnitResult.errorString { "Require one lava source at least around Rock Generator!" }

        else -> processor.process(world, machineKey, tier)
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTRockGeneratorScreenHandler(syncId, playerInventory, createContext())

    //    HTScreenFluidProvider    //

    override fun getFluidsToSync(): Map<Int, HTFluidVariantStack> = fluidStorage.getFluidsToSync()

    //    SidedStorageBlockEntity    //

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.wrapStorage()
}
