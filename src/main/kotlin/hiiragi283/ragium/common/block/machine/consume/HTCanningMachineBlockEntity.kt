package hiiragi283.ragium.common.block.machine.consume

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.util.HTUnitResult
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
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

class HTCanningMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.CANNING_MACHINE, pos, state),
    HTFluidSyncable {
    override var key: HTMachineKey = RagiumMachineKeys.CANNING_MACHINE

    private val inventory: HTMachineInventory = object : HTMachineInventory(
        2,
        mapOf(0 to HTStorageIO.INPUT, 1 to HTStorageIO.OUTPUT),
    ) {
        override fun isValid(slot: Int, stack: ItemStack): Boolean =
            stack.isOf(RagiumItems.EMPTY_FLUID_CUBE) || stack.isOf(RagiumItems.FILLED_FLUID_CUBE)
    }

    private val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.ofSmall(this)

    override fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
        fluidStorage.update(newTier)
    }

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage.readNbt(nbt, wrapperLookup, tier)
    }

    override fun asInventory(): SidedInventory = inventory

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactWithFluidStorage(player)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage

    override fun process(world: World, pos: BlockPos): HTUnitResult {
        val cubeStack: ItemStack = inventory.getStack(0)
        return when {
            cubeStack.isOf(RagiumItems.EMPTY_FLUID_CUBE) ->
                fluidStorage
                    .getStorage(0)
                    .unitMap { tryInsertCube(cubeStack, it) }

            cubeStack.isOf(RagiumItems.FILLED_FLUID_CUBE) ->
                fluidStorage
                    .getStorage(1)
                    .unitMap { tryExtractCube(cubeStack, it) }

            else -> HTUnitResult.errorString { "Failed to process Canning Machine!" }
        }
    }

    private fun tryInsertCube(stack: ItemStack, storageIn: SingleFluidStorage): HTUnitResult {
        if (storageIn.isResourceBlank) return HTUnitResult.errorString { "Input tank is empty!" }
        if (storageIn.amount < FluidConstants.BUCKET) return HTUnitResult.errorString { "Required 81,000 Units or more for canning!" }
        val variantIn: FluidVariant = storageIn.resource
        if (variantIn.isBlank) return HTUnitResult.errorString { "Blank Fluid!" }
        val filledStack: ItemStack = RagiumAPI.getInstance().createFilledCube(variantIn.fluid)
        val filledResult = HTItemResult(filledStack)
        return if (filledResult.canMerge(inventory.getStack(1))) {
            useTransaction { transaction: Transaction ->
                if (storageIn.extractSelf(FluidConstants.BUCKET, transaction) == FluidConstants.BUCKET) {
                    inventory.modifyStack(1, filledResult::merge)
                    stack.decrement(1)
                    HTUnitResult.success()
                } else {
                    HTUnitResult.errorString { "Failed to extract 81,000 Units from input tank!" }
                }
            }
        } else {
            HTUnitResult.errorString { "Failed to merge result into output!" }
        }
    }

    private fun tryExtractCube(stack: ItemStack, storageOut: SingleFluidStorage): HTUnitResult {
        if (storageOut.isFilledMax) return HTUnitResult.errorString { "Output tank is full!" }
        val cubeStorage: Storage<FluidVariant> =
            FluidStorage.ITEM.findFromStack(stack)
                ?: return HTUnitResult.errorString { "Failed to find fluid storage from cube" }
        val emptyResult = HTItemResult(RagiumItems.EMPTY_FLUID_CUBE)
        if (!emptyResult.canMerge(inventory.getStack(1))) return HTUnitResult.errorString { "Failed to merge result into output!" }
        val cubeResource: FluidVariant = StorageUtil.findStoredResource(cubeStorage)
            ?: return HTUnitResult.errorString { "No extractable fluid in given cube!" }
        return if (storageOut.isResourceBlank || storageOut.variant.isOf(cubeResource.fluid)) {
            useTransaction { transaction: Transaction ->
                if (storageOut.insert(
                        cubeResource,
                        FluidConstants.BUCKET,
                        transaction,
                    ) == FluidConstants.BUCKET
                ) {
                    inventory.removeStack(0, 1)
                    inventory.modifyStack(1, emptyResult::merge)
                    transaction.commit()
                    HTUnitResult.success()
                } else {
                    HTUnitResult.errorString { "Failed to extract 81,000 Units from input tank!" }
                }
            }
        } else {
            HTUnitResult.errorString { "Failed to merge result into output!" }
        }
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSmallMachineScreenHandler(syncId, playerInventory, createContext())

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, handler: HTFluidSyncable.Handler) {
        fluidStorage.sendPacket(player, handler)
    }
}
