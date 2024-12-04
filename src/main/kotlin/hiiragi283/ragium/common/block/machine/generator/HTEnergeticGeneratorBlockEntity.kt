package hiiragi283.ragium.common.block.machine.generator

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.isIn
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.fabricmc.fabric.api.util.TriState
import net.minecraft.block.BlockState
import net.minecraft.component.type.ItemEnchantmentsComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTEnergeticGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.ENERGETIC_GENERATOR, pos, state),
    HTFluidSyncable {
    override var key: HTMachineKey = RagiumMachineKeys.ENERGETIC_GENERATOR

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
        fluidStorage.update(tier)
    }

    private val inventory: SidedInventory = HTStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .countFilter { 1 }
        .buildInventory()

    private var fluidStorage: HTMachineFluidStorage = HTStorageBuilder(1)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .buildMachineFluidStorage(tier)

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
        // try to consume fluid fuel
        val fluidResult: DataResult<Boolean> = useTransaction { transaction: Transaction ->
            fluidStorage.map(0) { storageIn: SingleFluidStorage ->
                StorageUtil
                    .findExtractableResource(
                        storageIn,
                        { it.isIn(ConventionalFluidTags.EXPERIENCE) },
                        transaction,
                    )?.let { foundVariant: FluidVariant ->
                        if (storageIn.extract(
                                foundVariant,
                                FluidConstants.BUCKET,
                                transaction,
                            ) == FluidConstants.BUCKET
                        ) {
                            transaction.commit()
                            true
                        } else {
                            transaction.abort()
                            false
                        }
                    }
            }
        }
        if (fluidResult.result().orElse(false)) {
            return DataResult.success(Unit)
        }
        // try to consume item fuel
        val fuelStack: ItemStack = inventory.getStack(0)
        var enchantResult = TriState.FALSE
        if (fuelStack.isOf(Items.ENCHANTED_BOOK)) {
            EnchantmentHelper.apply(fuelStack) { builder: ItemEnchantmentsComponent.Builder ->
                builder.enchantments.firstOrNull()?.let { first: RegistryEntry<Enchantment> ->
                    val level: Int = builder.getLevel(first)
                    if (level > 0) {
                        builder.set(first, level - 1)
                        enchantResult = TriState.TRUE
                        return@apply
                    }
                }
                enchantResult = TriState.DEFAULT
            }
        }
        if (enchantResult.orElse(true)) {
            fuelStack.decrement(1)
            return DataResult.success(Unit)
        }
        if (HTEnergeticFuelRegistry.changeComponent(fuelStack)) {
            return DataResult.success(Unit)
        }
        return DataResult.error { "Failed to consume fuels!" }
    }

    //    SidedStorageBlockEntity    //

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.createWrapped()

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        fluidStorage.sendPacket(player, sender)
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
