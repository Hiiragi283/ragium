package hiiragi283.ragium.common.machine.generator

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.modifyStack
import hiiragi283.ragium.api.extension.restDamage
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
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.item.HTDynamiteItem
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.fluid.Fluids
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTNuclearReactorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.NUCLEAR_REACTOR, pos, state),
    HTFluidSyncable {
    override var key: HTMachineKey = RagiumMachineKeys.NUCLEAR_REACTOR

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    private val inventory: SidedInventory = HTStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSided()

    private val fluidStorage: HTMachineFluidStorage = HTStorageBuilder(1)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .fluidFilter { _: Int, variant: FluidVariant -> variant.isOf(Fluids.WATER) }
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

    override val energyFlag: HTEnergyNetwork.Flag = HTEnergyNetwork.Flag.GENERATE

    override fun process(world: World, pos: BlockPos): DataResult<Unit> {
        val fuelStack: ItemStack = inventory.getStack(0)
        val wasteStack: ItemStack = inventory.getStack(1)
        val result: HTItemResult = when (fuelStack.item) {
            RagiumItems.URANIUM_FUEL -> RagiumItems.NUCLEAR_WASTE
            RagiumItems.PLUTONIUM_FUEL -> RagiumItems.SLAG
            else -> null
        }?.let(::HTItemResult) ?: return DataResult.error { "Input slot has no nuclear fuels!" }
        if (!result.canMerge(wasteStack)) return overheat(world, pos)
        useTransaction { transaction: Transaction ->
            val storageIn: SingleFluidStorage = fluidStorage.get(0)
            val foundVariant: FluidVariant =
                StorageUtil.findExtractableResource(storageIn, transaction) ?: return overheat(world, pos)
            if (storageIn.extract(foundVariant, FluidConstants.BUCKET, transaction) == FluidConstants.BUCKET) {
                transaction.commit()
                inventory.modifyStack(1, result::merge)
                fuelStack.damage += 1
                return DataResult.success(Unit)
            } else {
                transaction.abort()
                return overheat(world, pos)
            }
        }
    }

    private fun overheat(world: World, pos: BlockPos): DataResult<Unit> {
        val fuel: ItemStack = inventory.getStack(0).copy()
        inventory.clear()
        val power: Float = fuel.restDamage / 16f
        HTDynamiteItem.Component(power, true).createExplosion(world, pos)
        return DataResult.error<Unit> { "Overheated!" }
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSmallMachineScreenHandler(
            syncId,
            playerInventory,
            packet,
            createContext(),
        )

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.createWrapped()

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        fluidStorage.sendPacket(player, sender)
    }
}
