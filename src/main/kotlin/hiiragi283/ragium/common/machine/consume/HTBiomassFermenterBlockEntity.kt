package hiiragi283.ragium.common.machine.consume

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTBiomassFermenterBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.BIOMASS_FERMENTER, pos, state) {
    override var key: HTMachineKey = RagiumMachineKeys.BIOMASS_FERMENTER

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

            override fun canInsert(variant: FluidVariant): Boolean = variant.isOf(RagiumFluids.BIOMASS.value)
        }
        fluidStorage.readNbt(nbt, wrapperLookup)
    }

    override val shouldTick: Boolean = false

    override fun getRequiredEnergy(world: World, pos: BlockPos): DataResult<Pair<HTEnergyNetwork.Flag, Long>> =
        tier.createEnergyResult(HTEnergyNetwork.Flag.CONSUME)

    override fun process(world: World, pos: BlockPos): DataResult<Unit> = DataResult.error { "WIP" }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? = null

    //    SidedStorageBlockEntity    //

    private val itemStorage: SingleSlotStorage<ItemVariant> = object : SingleSlotStorage<ItemVariant> {
        override fun insert(resource: ItemVariant, maxAmount: Long, transaction: TransactionContext): Long {
            StoragePreconditions.notBlankNotNegative(resource, maxAmount)
            val chance: Float = CompostingChanceRegistry.INSTANCE.get(resource.item)
            val fixedAmount: Long = (FluidConstants.BUCKET * chance).toLong()
            val inserted: Long =
                fluidStorage.insert(FluidVariant.of(RagiumFluids.BIOMASS.value), fixedAmount, transaction)
            return if (inserted > 0) {
                world?.let { tier.consumerEnergy(it, transaction.openNested()) }
                1
            } else {
                0
            }
        }

        override fun extract(resource: ItemVariant, maxAmount: Long, transaction: TransactionContext): Long = 0

        override fun isResourceBlank(): Boolean = true

        override fun getResource(): ItemVariant = ItemVariant.blank()

        override fun getAmount(): Long = 0

        override fun getCapacity(): Long = Long.MAX_VALUE
    }

    override fun getItemStorage(side: Direction?): Storage<ItemVariant> = itemStorage

    private var fluidStorage: SingleFluidStorage = object : SingleFluidStorage() {
        override fun getCapacity(variant: FluidVariant): Long = tier.tankCapacity

        override fun canInsert(variant: FluidVariant): Boolean = variant.isOf(RagiumFluids.BIOMASS.value)
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean =
        FluidStorageUtil.interactWithFluidStorage(FilteringStorage.extractOnlyOf(fluidStorage), player, Hand.MAIN_HAND)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = FilteringStorage.extractOnlyOf(fluidStorage)
}
