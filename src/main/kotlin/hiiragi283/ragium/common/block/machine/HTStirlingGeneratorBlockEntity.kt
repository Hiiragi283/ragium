package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.extension.getItemData
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

class HTStirlingGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.STIRLING_GENERATOR, pos, state, RagiumMachineKeys.STIRLING_GENERATOR) {
    private val itemInput = HTMachineItemHandler(1, this::setChanged)
    private val fluidInput: HTMachineFluidTank =
        object : HTMachineFluidTank(8000, this@HTStirlingGeneratorBlockEntity::setChanged) {
            override fun isFluidValid(stack: FluidStack): Boolean = stack.`is`(Tags.Fluids.WATER)
        }

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.of(
        listOf(itemInput.createSlot(0)),
        listOf(fluidInput),
    )

    override fun process(level: ServerLevel, pos: BlockPos) {
        val fuelTime: Int = itemInput.getStackInSlot(0).getItemData(NeoForgeDataMaps.FURNACE_FUELS)?.burnTime
            ?: throw HTMachineException.FindFuel(false)
        val requiredWater: Int = fuelTime / 10
        if (fluidInput.drain(requiredWater, IFluidHandler.FluidAction.SIMULATE).amount < requiredWater) {
            throw HTMachineException.ExtractFluid(false)
        }
        if (!itemInput.consumeItem(0, 1, true)) {
            throw HTMachineException.ConsumeInput(false)
        }
        fluidInput.drain(requiredWater, IFluidHandler.FluidAction.EXECUTE)
        itemInput.getStackInSlot(0).shrink(1)
    }

    override val energyFlag: HTEnergyNetwork.Flag = HTEnergyNetwork.Flag.GENERATE

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun interactWithFluidStorage(player: Player): Boolean =
        FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, fluidInput)
}
