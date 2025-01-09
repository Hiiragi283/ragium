package hiiragi283.ragium.common.block.machine.consume

import com.google.common.base.Predicates
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.screen.HTScreenFluidProvider
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTTieredFluidStorage
import hiiragi283.ragium.api.util.HTMachineException
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FluidDrainable
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTDrainBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.DRAIN, pos, state),
    HTScreenFluidProvider {
    override var machineKey: HTMachineKey = RagiumMachineKeys.DRAIN

    private val inventory: HTMachineInventory = HTMachineInventory.ofSmall()

    override fun asInventory(): SidedInventory = inventory

    private var fluidStorage = HTTieredFluidStorage(tier, HTStorageIO.OUTPUT, null, this::markDirty, 1)

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

    override fun process(world: World, pos: BlockPos) {
        if (fluidStorage.isFilledMax) throw HTMachineException.MaxFluid(false)
        var result = false
        // drain fluid from input slot
        result = extractFromCube()
        // drain experience from player
        if (!result) {
            result = extractFromPlayer(world)
        }
        // drain fluid from front
        if (!result) {
            result = extractFromFront(world)
        }
        if (!result) {
            throw HTMachineException.FluidInteract(false)
        }
    }

    private fun extractFromCube(): Boolean {
        // drain fluid from input slot
        val inputStack: ItemStack = inventory.getStack(0)
        if (inputStack.isOf(RagiumItems.FILLED_FLUID_CUBE)) {
            val storage: Storage<FluidVariant> =
                ContainerItemContext.withConstant(inputStack).find(FluidStorage.ITEM) ?: return false
            val emptyResult: HTItemResult = HTItemResult.ofItem(RagiumItems.EMPTY_FLUID_CUBE)
            if (!emptyResult.canMerge(inventory.getStack(1))) return false
            val cubeVariant: FluidVariant = StorageUtil.findStoredResource(storage) ?: return false
            useTransaction { transaction: Transaction ->
                if (fluidStorage.insert(cubeVariant, FluidConstants.BUCKET, transaction) == FluidConstants.BUCKET) {
                    inventory.removeStack(0, 1)
                    inventory.mergeStack(1, emptyResult)
                    transaction.commit()
                    return true
                }
            }
        }
        return false
    }

    private fun extractFromPlayer(world: World): Boolean {
        val abovePlayer: ServerPlayerEntity = world
            .asServerWorld()
            ?.players
            ?.firstOrNull { it.blockPos.down() == this.pos }
            ?: return false
        if (abovePlayer.experienceLevel > 0) {
            useTransaction { transaction: Transaction ->
                if (fluidStorage.insertSelf(
                        RagiumFluids.EXPERIENCE.variant,
                        FluidConstants.BUCKET,
                        transaction,
                    ) == FluidConstants.BUCKET
                ) {
                    abovePlayer.applyEnchantmentCosts(ItemStack.EMPTY, 1)
                    transaction.commit()
                    return true
                }
            }
        }
        return false
    }

    private fun extractFromFront(world: World): Boolean {
        val posTo: BlockPos = pos.offset(facing)
        val stateTo: BlockState = world.getBlockState(posTo)
        val blockTo: Block = stateTo.block
        if (blockTo is FluidDrainable) {
            val drained: ItemStack = blockTo.tryDrainFluid(null, world, posTo, stateTo)
            val storage: Storage<FluidVariant> =
                ContainerItemContext.withConstant(drained).find(FluidStorage.ITEM)
                    ?: return false
            val maxAmount: Long = storage.sumOf(StorageView<FluidVariant>::getCapacity)
            return StorageUtil.move(
                storage,
                fluidStorage,
                Predicates.alwaysTrue(),
                maxAmount,
                null,
            ) > 0
        } else {
            return false
        }
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTSmallMachineScreenHandler(syncId, playerInventory, createContext())

    //    SidedStorageBlockEntity    //

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.wrapStorage()

    //    HTScreenFluidProvider    //

    override fun getFluidsToSync(): Map<Int, HTFluidVariantStack> = fluidStorage.getFluidsToSync()
}
