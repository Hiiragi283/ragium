package hiiragi283.ragium.common.machine.consume

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.findFromStack
import hiiragi283.ragium.api.extension.isFilledMax
import hiiragi283.ragium.api.extension.modifyStack
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
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

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
        fluidStorage.update(newTier)
    }

    private val inventory: SidedInventory = HTStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .stackFilter { _: Int, stack: ItemStack ->
            stack.isOf(RagiumItems.EMPTY_FLUID_CUBE) || stack.isOf(RagiumItems.FILLED_FLUID_CUBE)
        }.buildInventory()

    private val fluidStorage: HTMachineFluidStorage = HTStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildMachineFluidStorage()

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage.readNbt(nbt, wrapperLookup, tier)
    }

    override fun asInventory(): SidedInventory = inventory

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactByPlayer(player)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.createWrapped()

    override fun process(world: World, pos: BlockPos): DataResult<Unit> {
        val cubeStack: ItemStack = inventory.getStack(0)
        return when {
            cubeStack.isOf(RagiumItems.EMPTY_FLUID_CUBE) -> fluidStorage.flatMap(0) { tryInsertCube(cubeStack, it) }
            cubeStack.isOf(RagiumItems.FILLED_FLUID_CUBE) -> fluidStorage.flatMap(1) { tryExtractCube(cubeStack, it) }
            else -> DataResult.error { "Failed to process Canning Machine!" }
        }
    }

    private fun tryInsertCube(stack: ItemStack, storageIn: SingleFluidStorage): DataResult<Unit> {
        if (storageIn.isResourceBlank) return DataResult.error { "Input tank is empty!" }
        if (storageIn.amount < FluidConstants.BUCKET) return DataResult.error { "Required 81,000 Units or more for canning!" }
        val variantIn: FluidVariant = storageIn.resource
        val filledStack: ItemStack = RagiumAPI.getInstance().createFilledCube(variantIn.fluid)
        val filledResult = HTItemResult(filledStack)
        return if (filledResult.canMerge(inventory.getStack(1))) {
            useTransaction { transaction: Transaction ->
                if (storageIn.extract(variantIn, FluidConstants.BUCKET, transaction) == FluidConstants.BUCKET) {
                    inventory.modifyStack(1, filledResult::merge)
                    stack.decrement(1)
                    transaction.commit()
                    DataResult.success(Unit)
                } else {
                    transaction.abort()
                    DataResult.error { "Failed to extract 81,000 Units from input tank!" }
                }
            }
        } else {
            DataResult.error { "Failed to merge result into output!" }
        }
    }

    private fun tryExtractCube(stack: ItemStack, storageOut: SingleFluidStorage): DataResult<Unit> {
        if (storageOut.isFilledMax) return DataResult.error { "Output tank is full!" }
        val cubeStorage: Storage<FluidVariant> =
            FluidStorage.ITEM.findFromStack(stack)
                ?: return DataResult.error { "Failed to find fluid storage from cube" }
        val emptyResult = HTItemResult(RagiumItems.EMPTY_FLUID_CUBE)
        if (!emptyResult.canMerge(inventory.getStack(1))) return DataResult.error { "Failed to merge result into output!" }
        val cubeResource: FluidVariant = StorageUtil.findStoredResource(cubeStorage)
            ?: return DataResult.error { "No extractable fluid in given cube!" }
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
                    DataResult.success(Unit)
                } else {
                    transaction.abort()
                    DataResult.error { "Failed to extract 81,000 Units from input tank!" }
                }
            }
        } else {
            DataResult.error { "Failed to merge result into output!" }
        }
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSmallMachineScreenHandler(syncId, playerInventory, packet, createContext())

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        fluidStorage.sendPacket(player, sender)
    }
}
