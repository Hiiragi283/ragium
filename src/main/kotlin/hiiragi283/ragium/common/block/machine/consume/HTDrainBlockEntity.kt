package hiiragi283.ragium.common.block.machine.consume

import com.google.common.base.Predicates
import hiiragi283.ragium.api.extension.readFluidStorage
import hiiragi283.ragium.api.extension.writeFluidStorage
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTTieredFluidStorage
import hiiragi283.ragium.api.util.HTUnitResult
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FluidDrainable
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTDrainBlockEntity(pos: BlockPos, state: BlockState) : HTMachineBlockEntityBase(RagiumBlockEntityTypes.DRAIN, pos, state) {
    override var key: HTMachineKey = RagiumMachineKeys.DRAIN

    private var fluidStorage = HTTieredFluidStorage(tier, HTStorageIO.OUTPUT, null, this::markDirty)

    override fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
        fluidStorage = fluidStorage.updateTier(newTier)
    }

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        nbt.writeFluidStorage(FLUID_KEY, fluidStorage, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        nbt.readFluidStorage(FLUID_KEY, fluidStorage, wrapperLookup)
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactWithFluidStorage(player)

    override fun process(world: World, pos: BlockPos): HTUnitResult {
        var result = false
        Direction.entries.forEach { dir: Direction ->
            val posTo: BlockPos = pos.offset(dir)
            val stateTo: BlockState = world.getBlockState(posTo)
            val blockTo: Block = stateTo.block
            if (blockTo is FluidDrainable) {
                val drained: ItemStack = blockTo.tryDrainFluid(null, world, posTo, stateTo)
                val storage: Storage<FluidVariant> =
                    FluidStorage.ITEM.find(drained, ContainerItemContext.withConstant(drained)) ?: return@forEach
                val maxAmount: Long = storage.sumOf(StorageView<FluidVariant>::getCapacity)
                result = StorageUtil.move(
                    storage,
                    fluidStorage,
                    Predicates.alwaysTrue(),
                    maxAmount,
                    null,
                ) > 0
                if (result) {
                    return HTUnitResult.success()
                }
            }
        }
        return HTUnitResult.errorString { "Failed to extract fluid from any sides!" }
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? = null

    //    SidedStorageBlockEntity    //

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.wrapStorage()
}
