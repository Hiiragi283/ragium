package hiiragi283.ragium.common.block.machine.generator

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.component.HTExplosionComponent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.screen.HTScreenFluidProvider
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTTieredFluidStorage
import hiiragi283.ragium.api.tags.RagiumFluidTags
import hiiragi283.ragium.api.util.HTUnitResult
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
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
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTNuclearReactorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.NUCLEAR_REACTOR, pos, state),
    HTScreenFluidProvider {
    override var machineKey: HTMachineKey = RagiumMachineKeys.NUCLEAR_REACTOR

    private val inventory: HTMachineInventory = HTMachineInventory.ofSmall()

    private var fluidStorage = HTTieredFluidStorage(tier, HTStorageIO.INPUT, RagiumFluidTags.COOLANTS, this::markDirty)

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
        val wasteStack: ItemStack = inventory.getStack(1)
        val result: HTItemResult = when (fuelStack.item) {
            RagiumItems.Radioactives.URANIUM_FUEL.get() -> RagiumItems.Radioactives.NUCLEAR_WASTE
            RagiumItems.Radioactives.PLUTONIUM_FUEL.get() -> RagiumItems.SLAG
            else -> null
        }?.let(::HTItemResult) ?: return HTUnitResult.errorString { "Input slot has no nuclear fuels!" }
        if (!result.canMerge(wasteStack)) return overheat(world, pos)
        return useTransaction { transaction: Transaction ->
            val maxAmount: Long = RagiumAPI
                .getInstance()
                .config.machine.generator.coolant
            if (fluidStorage.extractSelf(maxAmount, transaction) == maxAmount) {
                transaction.commit()
                inventory.mergeStack(1, result)
                fuelStack.damage += 1
                HTUnitResult.success()
            } else {
                overheat(world, pos)
            }
        }
    }

    private fun overheat(world: World, pos: BlockPos): HTUnitResult {
        val fuel: ItemStack = inventory.getStack(0).copy()
        inventory.clear()
        val power: Float = fuel.restDamage / 16f
        HTExplosionComponent(power, true).createExplosion(world, pos)
        return HTUnitResult.errorString { "Overheated!" }
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSmallMachineScreenHandler(syncId, playerInventory, createContext())

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.wrapStorage()

    //    HTScreenFluidProvider    //
    override fun getFluidsToSync(): Map<Int, HTFluidVariantStack> = fluidStorage.getFluidsToSync()
}
