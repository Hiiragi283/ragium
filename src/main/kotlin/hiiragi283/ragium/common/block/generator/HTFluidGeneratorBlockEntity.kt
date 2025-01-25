package hiiragi283.ragium.common.block.generator

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.capability.LimitedFluidHandler
import hiiragi283.ragium.api.fluid.HTTieredFluidTank
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.machine.property.HTGeneratorFuel
import hiiragi283.ragium.api.property.get
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTFluidGeneratorBlockEntity(pos: BlockPos, state: BlockState, override val machineKey: HTMachineKey) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.FLUID_GENERATOR, pos, state) {
    val fuelData: Set<HTGeneratorFuel>?
        get() = machineKey.getProperty()[HTMachinePropertyKeys.GENERATOR_FUEL]

    private val tank: HTTieredFluidTank = object : HTTieredFluidTank(this@HTFluidGeneratorBlockEntity) {
        override fun isFluidValid(stack: FluidStack): Boolean {
            val fuelData: Set<HTGeneratorFuel> = fuelData ?: return super.isFluidValid(stack)
            return fuelData.any { it.isAcceptableWithoutAmount(stack) }
        }
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tank.writeToNBT(registries, tag)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        tank.readFromNBT(registries, tag)
    }

    override fun process(level: ServerLevel, pos: BlockPos) {
        if (tank.isEmpty) throw HTMachineException.EmptyFluid(false)
        val fuelData: Set<HTGeneratorFuel> = fuelData ?: throw HTMachineException.FindFuel(false)
        val stackIn: FluidStack = tank.fluid
        for (generatorFuel: HTGeneratorFuel in fuelData) {
            if (!generatorFuel.isAcceptable(stackIn)) continue
            val amount: Int = generatorFuel.amount
            if (tank.drain(amount, IFluidHandler.FluidAction.SIMULATE).amount == amount) {
                tank.drain(amount, IFluidHandler.FluidAction.EXECUTE)
                return
            }
        }
        throw throw HTMachineException.ConsumeFuel(false)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun interactWithFluidStorage(player: Player): Boolean =
        FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, tank)

    override fun onUpdateTier(oldTier: HTMachineTier, newTier: HTMachineTier) {
        tank.onUpdateTier(oldTier, newTier)
    }

    //    HTBlockEntityHandlerProvider    //

    override fun getFluidHandler(direction: Direction?): LimitedFluidHandler =
        LimitedFluidHandler(mapOf(0 to HTStorageIO.INPUT), arrayOf(tank))
}
