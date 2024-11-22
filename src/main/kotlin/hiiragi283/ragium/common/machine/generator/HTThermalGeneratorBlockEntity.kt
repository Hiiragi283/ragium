package hiiragi283.ragium.common.machine.generator

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.toStorage
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.fluid.Fluids
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTThermalGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.THERMAL_GENERATOR, pos, state),
    HTFluidSyncable {
    override var key: HTMachineKey = RagiumMachineKeys.THERMAL_GENERATOR

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    private val inventory: SidedInventory = HTStorageBuilder(1)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .filter { _: Int, stack: ItemStack -> stack.isOf(Items.BLAZE_POWDER) }
        .buildSided()

    override fun asInventory(): SidedInventory = inventory

    private var fluidStorage: SingleFluidStorage = object : SingleFluidStorage() {
        override fun getCapacity(variant: FluidVariant): Long = tier.tankCapacity

        override fun canInsert(variant: FluidVariant): Boolean = variant == FluidVariant.of(Fluids.LAVA)
    }

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage = object : SingleFluidStorage() {
            override fun getCapacity(variant: FluidVariant): Long = tier.tankCapacity

            override fun canInsert(variant: FluidVariant): Boolean = variant == FluidVariant.of(Fluids.LAVA)
        }
        fluidStorage.readNbt(nbt, wrapperLookup)
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean =
        FluidStorageUtil.interactWithFluidStorage(fluidStorage, player, Hand.MAIN_HAND)

    override fun getRequiredEnergy(world: World, pos: BlockPos): DataResult<Pair<HTEnergyNetwork.Flag, Long>> = when {
        inventory.getStack(0).isOf(Items.BLAZE_POWDER) -> tier.createEnergyResult(HTEnergyNetwork.Flag.GENERATE)
        fluidStorage.amount >= FluidConstants.BUCKET -> tier.createEnergyResult(HTEnergyNetwork.Flag.GENERATE)
        else -> DataResult.error { "Could not find fuels from Thermal Generator!" }
    }

    override fun process(world: World, pos: BlockPos): DataResult<Unit> {
        // try to consume item
        val fuelStack: ItemStack = inventory.getStack(0)
        if (fuelStack.isOf(Items.BLAZE_POWDER)) {
            fuelStack.decrement(1)
            return DataResult.success(Unit)
        }
        // try to consume fluid
        return useTransaction { transaction: Transaction ->
            val extracted: Long = fluidStorage.extract(FluidVariant.of(Fluids.LAVA), FluidConstants.BUCKET, transaction)
            if (extracted == FluidConstants.BUCKET) {
                transaction.commit()
                DataResult.success(Unit)
            } else {
                transaction.abort()
                DataResult.error { "Failed to consume fuels!" }
            }
        }
    }

    //    SidedStorageBlockEntity    //

    override fun getItemStorage(side: Direction?): Storage<ItemVariant> = inventory.toStorage(side)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = FilteringStorage.insertOnlyOf(fluidStorage)

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        sender(player, 0, fluidStorage.variant, fluidStorage.amount)
    }

    //    ExtendedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTSmallMachineScreenHandler(
            syncId,
            playerInventory,
            packet,
            createContext(),
        )
}
