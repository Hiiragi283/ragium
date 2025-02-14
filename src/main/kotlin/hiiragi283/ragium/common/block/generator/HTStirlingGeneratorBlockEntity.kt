package hiiragi283.ragium.common.block.generator

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.energy.HTMachineEnergyData.Empty
import hiiragi283.ragium.api.extension.insertOrDrop
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.internal.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper

class HTStirlingGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.STIRLING_GENERATOR, pos, state, HTMachineType.STIRLING_GENERATOR) {
    private val itemInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val itemOutput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val fluidInput: HTMachineFluidTank = RagiumAPI.getInstance().createTank(this::setChanged, Tags.Fluids.WATER)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.of(
        listOf(itemInput.createSlot(0), itemOutput.createSlot(0)),
        listOf(fluidInput),
    )

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        fluidInput.updateCapacity(this)
    }

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = Stirling.of(itemInput.getStackInSlot(0))

    override fun process(level: ServerLevel, pos: BlockPos) {
        val burnTime: Int = itemInput.getStackInSlot(0).getBurnTime(null)
        if (burnTime <= 0) throw HTMachineException.FindFuel(false)
        val requiredWater: Int = RagiumConfig.getStirlingWater(burnTime)
        if (fluidInput.drain(requiredWater, IFluidHandler.FluidAction.SIMULATE).amount < requiredWater) {
            throw HTMachineException.ExtractFluid(false)
        }
        if (!itemInput.consumeItem(0, 1, true)) {
            throw HTMachineException.ConsumeInput(false)
        }
        fluidInput.drain(requiredWater, IFluidHandler.FluidAction.EXECUTE)
        itemInput.getStackInSlot(0).shrink(1)
        itemOutput.insertOrDrop(
            level,
            pos,
            RagiumItems
                .getMaterialItem(HTTagPrefix.DUST, CommonMaterials.ASH)
                .toStack(RagiumConfig.getStirlingAsh(burnTime)),
        )
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun interactWithFluidStorage(player: Player): Boolean = fluidInput.interactWithFluidStorage(player, HTStorageIO.GENERIC)

    override val hasMenu: Boolean = false

    override fun getItemHandler(direction: Direction?): CombinedInvWrapper = CombinedInvWrapper(
        HTStorageIO.INPUT.wrapItemHandler(itemInput),
        HTStorageIO.OUTPUT.wrapItemHandler(itemOutput),
    )

    override fun getFluidHandler(direction: Direction?): IFluidHandler = HTStorageIO.INPUT.wrapFluidHandler(fluidInput)

    //    EnergyData    //

    private class Stirling private constructor(override val amount: Int) : HTMachineEnergyData {
        companion object {
            @JvmStatic
            fun of(stack: ItemStack): HTMachineEnergyData {
                val burnTime: Int = stack.getBurnTime(null)
                if (burnTime > 0) {
                    return Stirling(RagiumConfig.getStirlingEnergy(burnTime))
                }
                return Empty
            }
        }

        override fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Boolean {
            val fixedAmount: Int = amount * modifier
            if (fixedAmount <= 0) return false
            return storage.receiveEnergy(fixedAmount, simulate) > 0
        }
    }
}
