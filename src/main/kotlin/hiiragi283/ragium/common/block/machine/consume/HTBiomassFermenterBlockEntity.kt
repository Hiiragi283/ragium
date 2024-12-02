package hiiragi283.ragium.common.block.machine.consume

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry
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
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
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

    override fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
        fluidStorage.update(newTier)
    }

    private val inventory: SidedInventory = HTStorageBuilder(1)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .buildInventory()

    private var fluidStorage: HTMachineFluidStorage = HTStorageBuilder(1)
        .set(0, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .fluidFilter { _: Int, variant: FluidVariant -> variant.isOf(RagiumFluids.BIOMASS.value) }
        .buildMachineFluidStorage()

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage.readNbt(nbt, wrapperLookup, tier)
    }

    override fun asInventory(): SidedInventory? = inventory

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactByPlayer(player)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.createWrapped()

    override fun process(world: World, pos: BlockPos): DataResult<Unit> = HTRecipeProcessor { _: World, _: HTMachineKey, _: HTMachineTier ->
        val inputStack: ItemStack = inventory.getStack(0)
        val chance: Float = CompostingChanceRegistry.INSTANCE.get(inputStack.item)
        val fixedAmount: Long = (FluidConstants.BUCKET * chance).toLong()
        if (fixedAmount <= 0) {
            return@HTRecipeProcessor DataResult.error { "Failed to calculate biomass amount!" }
        }
        useTransaction { transaction: Transaction ->
            fluidStorage.map(0) {
                if (it.insert(FluidVariant.of(RagiumFluids.BIOMASS.value), fixedAmount, transaction) == fixedAmount) {
                    transaction.commit()
                    inputStack.decrement(1)
                } else {
                    transaction.abort()
                }
            }
        }
    }.process(world, key, tier)

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTSmallMachineScreenHandler(syncId, playerInventory, packet, createContext())

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        fluidStorage.sendPacket(player, sender, 1)
    }
}
