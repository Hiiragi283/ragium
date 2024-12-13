package hiiragi283.ragium.common.block.machine.generator

import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTTieredFluidStorage
import hiiragi283.ragium.api.util.HTUnitResult
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTSteamGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.STEAM_GENERATOR, pos, state),
    HTFluidSyncable {
    override var key: HTMachineKey = RagiumMachineKeys.STEAM_GENERATOR

    private val inventory: HTMachineInventory = object : HTMachineInventory(
        2,
        mapOf(0 to HTStorageIO.INPUT, 1 to HTStorageIO.OUTPUT),
    ) {
        override fun isValid(slot: Int, stack: ItemStack): Boolean = when (slot) {
            0 -> stack.isIn(ItemTags.COALS)
            1 -> stack.isOf(RagiumContents.Dusts.ASH)
            else -> false
        }
    }

    private var fluidStorage =
        HTTieredFluidStorage(tier, HTStorageIO.INPUT, ConventionalFluidTags.WATER, this::markDirty)

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

    override fun asInventory(): SidedInventory = inventory

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactWithFluidStorage(player)

    override val energyFlag: HTEnergyNetwork.Flag = HTEnergyNetwork.Flag.GENERATE

    override fun process(world: World, pos: BlockPos): HTUnitResult {
        val fuelStack: ItemStack = inventory.getStack(0)
        return if (fuelStack.isIn(ItemTags.COALS)) {
            useTransaction { transaction: Transaction ->
                if (fluidStorage.extractSelf(FluidConstants.INGOT, transaction) == FluidConstants.INGOT) {
                    transaction.commit()
                    fuelStack.decrement(1)
                    inventory.mergeStack(1, HTItemResult(RagiumContents.Dusts.ASH))
                    HTUnitResult.success()
                } else {
                    HTUnitResult.errorString { "Failed to consume fuels!" }
                }
            }
        } else {
            HTUnitResult.errorString { "Required fuels with #minecraft:coals!" }
        }
    }

    //    SidedStorageBlockEntity    //

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.wrapStorage()

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, handler: HTFluidSyncable.Handler) {
        fluidStorage.sendPacket(player, handler)
    }

    //    ExtendedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTSmallMachineScreenHandler(syncId, playerInventory, createContext())
}
