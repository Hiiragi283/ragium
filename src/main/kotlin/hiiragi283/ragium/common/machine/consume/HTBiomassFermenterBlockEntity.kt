package hiiragi283.ragium.common.machine.consume

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry
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
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTBiomassFermenterBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.BIOMASS_FERMENTER, pos, state),
    HTFluidSyncable {
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

    private val inventory: SidedInventory = HTStorageBuilder(1)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .buildSided()

    override fun asInventory(): SidedInventory? = inventory

    private var fluidStorage: SingleFluidStorage = object : SingleFluidStorage() {
        override fun getCapacity(variant: FluidVariant): Long = tier.tankCapacity

        override fun canInsert(variant: FluidVariant): Boolean = variant.isOf(RagiumFluids.BIOMASS.value)
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean =
        FluidStorageUtil.interactWithFluidStorage(fluidStorage, player, Hand.MAIN_HAND)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = FilteringStorage.extractOnlyOf(fluidStorage)

    override fun process(world: World, pos: BlockPos): DataResult<Unit> = HTRecipeProcessor { _: World, _: HTMachineKey, _: HTMachineTier ->
        val inputStack: ItemStack = inventory.getStack(0)
        val chance: Float = CompostingChanceRegistry.INSTANCE.get(inputStack.item)
        val fixedAmount: Long = (FluidConstants.BUCKET * chance).toLong()
        if (fixedAmount <= 0) {
            return@HTRecipeProcessor DataResult.error { "Failed to calculate biomass amount!" }
        }
        useTransaction { transaction: Transaction ->
            val inserted: Long =
                fluidStorage.insert(FluidVariant.of(RagiumFluids.BIOMASS.value), fixedAmount, transaction)
            if (inserted > 0) {
                transaction.commit()
                inputStack.decrement(1)
                DataResult.success(Unit)
            } else {
                transaction.abort()
                DataResult.error { "Failed to insert result into output!" }
            }
        }
    }.process(world, key, tier)

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTSmallMachineScreenHandler(syncId, playerInventory, packet, createContext())

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        sender(player, 1, fluidStorage.resource, fluidStorage.amount)
    }
}
