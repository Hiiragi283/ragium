package hiiragi283.ragium.common.machine

import com.google.common.base.Predicates
import hiiragi283.ragium.api.extension.fluidStorageOf
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTConsumerBlockEntityBase
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FluidDrainable
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTDrainBlockEntity(pos: BlockPos, state: BlockState) : HTConsumerBlockEntityBase(RagiumBlockEntityTypes.DRAIN, pos, state) {
    override var key: HTMachineKey = RagiumMachineKeys.DRAIN

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage = fluidStorageOf(tier.tankCapacity)
        fluidStorage.readNbt(nbt, wrapperLookup)
    }

    override fun consumeEnergy(world: World, pos: BlockPos): Boolean {
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
                    fluidStorage,
                    Predicates.alwaysTrue(),
                    maxAmount,
                    null,
                )
                if (transferred > 0) {
                    return true
                }
            }
        }
        return false
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? = null

    //    SidedStorageBlockEntity    //

    private var fluidStorage: SingleFluidStorage = fluidStorageOf(tier.tankCapacity)

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean =
        FluidStorageUtil.interactWithFluidStorage(fluidStorage, player, Hand.MAIN_HAND)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = FilteringStorage.extractOnlyOf(fluidStorage)
}
