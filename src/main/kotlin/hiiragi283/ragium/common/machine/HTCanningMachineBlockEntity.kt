package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.isFilledMax
import hiiragi283.ragium.api.extension.modifyStack
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTConsumerBlockEntityBase
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSteamGeneratorScreenHandler
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
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
    HTConsumerBlockEntityBase(RagiumBlockEntityTypes.CANNING_MACHINE, pos, state),
    HTFluidSyncable {
    override var key: HTMachineKey = RagiumMachineKeys.CANNING_MACHINE

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage.readNbt(nbt, wrapperLookup)
    }

    private val inventory: SidedInventory = HTStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSided()

    override fun asInventory(): SidedInventory = inventory

    private val fluidStorage: HTMachineFluidStorage = HTStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildMachineFluidStorage()

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactByPlayer(player)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.createWrapped()

    // always not consume energy
    override fun consumeEnergy(world: World, pos: BlockPos): Boolean {
        val cubeStack: ItemStack = inventory.getStack(0)
        val storageIn: SingleFluidStorage = fluidStorage.get(0)
        val storageOut: SingleFluidStorage = fluidStorage.get(1)
        when {
            cubeStack.isOf(RagiumItems.EMPTY_FLUID_CUBE) -> tryInsertCube(cubeStack, storageIn)
            cubeStack.isOf(RagiumItems.FILLED_FLUID_CUBE) -> tryExtractCube(cubeStack, storageOut)
        }
        return false
    }

    private fun tryInsertCube(stack: ItemStack, storageIn: SingleFluidStorage) {
        if (storageIn.isResourceBlank) return
        if (storageIn.amount < FluidConstants.BUCKET) return
        val variantIn: FluidVariant = storageIn.resource
        val filledStack: ItemStack = RagiumAPI.getInstance().createFilledCube(variantIn.fluid)
        val filledResult = HTItemResult(filledStack)
        if (filledResult.canMerge(inventory.getStack(1))) {
            useTransaction { transaction: Transaction ->
                if (storageIn.extract(variantIn, FluidConstants.BUCKET, transaction) == FluidConstants.BUCKET) {
                    inventory.modifyStack(1, filledResult::merge)
                    stack.decrement(1)
                    transaction.commit()
                } else {
                    transaction.abort()
                }
            }
        }
    }

    private fun tryExtractCube(stack: ItemStack, storageOut: SingleFluidStorage) {
        if (storageOut.isFilledMax) return
        val cubeStorage: Storage<FluidVariant> =
            ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM) ?: return
        val emptyResult = HTItemResult(RagiumItems.EMPTY_FLUID_CUBE)
        if (!emptyResult.canMerge(inventory.getStack(1))) return
        val cubeResource: FluidVariant = StorageUtil.findStoredResource(cubeStorage) ?: return
        if (storageOut.isResourceBlank || storageOut.variant.isOf(cubeResource.fluid)) {
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
                } else {
                    transaction.abort()
                }
            }
        }
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSteamGeneratorScreenHandler(syncId, playerInventory, packet, createContext())
    
    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        fluidStorage.sendPacket(player, sender)
    }
}
