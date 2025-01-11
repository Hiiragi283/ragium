package hiiragi283.ragium.common.block.machine.consume

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.insertSelf
import hiiragi283.ragium.api.extension.readFluidStorage
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.extension.writeFluidStorage
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
import hiiragi283.ragium.api.screen.HTScreenFluidProvider
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTTieredFluidStorage
import hiiragi283.ragium.api.util.HTMachineException
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTBiomassFermenterBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.BIOMASS_FERMENTER, pos, state),
    HTScreenFluidProvider {
    override var machineKey: HTMachineKey = RagiumMachineKeys.BIOMASS_FERMENTER

    private val inventory = HTMachineInventory(1, intArrayOf(0), intArrayOf())
    private var fluidStorage = HTTieredFluidStorage(tier, HTStorageIO.OUTPUT, null, this::markDirty, 1)

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

    override fun asInventory(): SidedInventory? = inventory

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactWithFluidStorage(player)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.wrapStorage()

    private val processor = HTRecipeProcessor { _: World, _: HTMachineTier ->
        runCatching {
            val inputStack: ItemStack = inventory.getStack(0)
            val chance: Float = CompostingChanceRegistry.INSTANCE.get(inputStack.item)
            val fixedAmount: Long = (FluidConstants.BUCKET * chance).toLong()
            if (fixedAmount <= 0) {
                throw HTMachineException.CalculateAmount(true)
            }
            useTransaction { transaction: Transaction ->
                val variant: FluidVariant = RagiumFluids.BIOMASS.variant
                if (fluidStorage.insertSelf(variant, fixedAmount, transaction) == fixedAmount) {
                    transaction.commit()
                    inputStack.decrement(1)
                } else {
                    throw HTMachineException.InsertFluid(true)
                }
            }
        }
    }

    override fun process(world: World, pos: BlockPos) {
        processor.process(world, tier).getOrThrow()
    }

    //    HTScreenFluidProvider    //

    override fun getFluidsToSync(): Map<Int, HTFluidVariantStack> = fluidStorage.getFluidsToSync()
}
