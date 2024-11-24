package hiiragi283.ragium.common.machine.consume

import com.google.common.base.Predicates
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
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

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
        fluidStorage.update(newTier)
    }
    
    private var fluidStorage: HTMachineFluidStorage = HTStorageBuilder(1)
        .set(0, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildMachineFluidStorage()

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage.readNbt(nbt, wrapperLookup, tier)
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactByPlayer(player)
    
    override fun process(world: World, pos: BlockPos): DataResult<Unit> {
        Direction.entries.forEach { dir: Direction ->
            val posTo: BlockPos = pos.offset(dir)
            val stateTo: BlockState = world.getBlockState(posTo)
            val blockTo: Block = stateTo.block
            if (blockTo is FluidDrainable) {
                val drained: ItemStack = blockTo.tryDrainFluid(null, world, posTo, stateTo)
                val storage: Storage<FluidVariant> =
                    FluidStorage.ITEM.find(drained, ContainerItemContext.withConstant(drained)) ?: return@forEach
                val maxAmount: Long = storage.sumOf(StorageView<FluidVariant>::getCapacity)
                val transferred: Long = StorageUtil.move(
                    storage,
                    fluidStorage.get(0),
                    Predicates.alwaysTrue(),
                    maxAmount,
                    null,
                )
                if (transferred > 0) {
                    return DataResult.success(Unit)
                }
            }
        }
        return DataResult.error { "Failed to extract fluid from any sides!" }
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? = null

    //    SidedStorageBlockEntity    //   

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.createWrapped()
}
