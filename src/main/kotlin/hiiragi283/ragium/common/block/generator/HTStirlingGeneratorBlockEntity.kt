package hiiragi283.ragium.common.block.generator

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

class HTStirlingGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.STIRLING_GENERATOR, pos, state, RagiumMachineKeys.STIRLING_GENERATOR) {
    private val itemInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val fluidInput: HTMachineFluidTank = RagiumAPI.getInstance().createTank(this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.of(
        listOf(itemInput.createSlot(0)),
        listOf(fluidInput),
    )

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        fluidInput.updateCapacity(this)
    }

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData {
        TODO("Not yet implemented")
    }

    override fun process(level: ServerLevel, pos: BlockPos) {
        val fuelTime: Int = itemInput
            .getStackInSlot(0)
            .itemHolder
            .getData(NeoForgeDataMaps.FURNACE_FUELS)
            ?.burnTime
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

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun interactWithFluidStorage(player: Player): Boolean = fluidInput.interactWithFluidStorage(player, HTStorageIO.GENERIC)

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable = HTStorageIO.INPUT.wrapItemHandler(itemInput)

    override fun getFluidHandler(direction: Direction?): IFluidHandler = HTStorageIO.INPUT.wrapFluidHandler(fluidInput)
}
