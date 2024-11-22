package hiiragi283.ragium.common.machine.generator

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.isIn
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.tags.RagiumFluidTags
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTCombustionGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.COMBUSTION_GENERATOR, pos, state),
    HTFluidSyncable {
    override var key: HTMachineKey = RagiumMachineKeys.COMBUSTION_GENERATOR

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage = object : SingleFluidStorage() {
            override fun getCapacity(variant: FluidVariant): Long = tier.tankCapacity

            override fun canInsert(variant: FluidVariant): Boolean = variant.isIn(RagiumFluidTags.FUEL)
        }
        fluidStorage.readNbt(nbt, wrapperLookup)
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean =
        FluidStorageUtil.interactWithFluidStorage(fluidStorage, player, Hand.MAIN_HAND)

    override fun getRequiredEnergy(world: World, pos: BlockPos): DataResult<Pair<HTEnergyNetwork.Flag, Long>> = when {
        fluidStorage.amount >= FluidConstants.BUCKET -> tier.createEnergyResult(HTEnergyNetwork.Flag.GENERATE)
        else -> DataResult.error { "Could not find fuels from Combustion Generator!" }
    }

    override fun process(world: World, pos: BlockPos): DataResult<Unit> = useTransaction { transaction: Transaction ->
        if (fluidStorage.variant.isBlank) false
        val extracted: Long = fluidStorage.extract(fluidStorage.variant, FluidConstants.BUCKET, transaction)
        if (extracted == FluidConstants.BUCKET) {
            transaction.commit()
            DataResult.success(Unit)
        } else {
            transaction.abort()
            DataResult.error { "Failed to consume fuels!" }
        }
    }

    //    SidedStorageBlockEntity    //

    private var fluidStorage: SingleFluidStorage = object : SingleFluidStorage() {
        override fun getCapacity(variant: FluidVariant): Long = tier.tankCapacity

        override fun canInsert(variant: FluidVariant): Boolean = variant.isIn(RagiumFluidTags.FUEL)
    }

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
