package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.data.HTFluidFuelData
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.HTItemStackSlot
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTGeneratorVariant
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.registries.datamaps.DataMapType

abstract class HTFuelGeneratorBlockEntity(variant: HTGeneratorVariant, pos: BlockPos, state: BlockState) :
    HTGeneratorBlockEntity(
        variant,
        pos,
        state,
    ),
    HTFluidInteractable {
    protected lateinit var fuelSlot: HTItemSlot
        private set

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // fuel
        fuelSlot = HTItemStackSlot.atManualOut(
            this,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(1),
            filter = { stack: ItemStack -> getFuelValue(stack) > 0 },
        )
        return HTSimpleItemSlotHolder(this, listOf(fuelSlot), listOf())
    }

    protected lateinit var tank: HTFluidStackTank
        private set

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder? {
        tank = HTFluidStackTank.of(listener, 8000, filter = { stack: FluidStack -> getRequiredAmount(stack) > 0 })
        return HTSimpleFluidTankHolder(this, listOf(tank), listOf())
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.FUEL_GENERATOR.openMenu(player, title, this, ::writeExtraContainerData)

    //    Ticking    //

    override fun onUpdateServer(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): Boolean {
        // スロット内のアイテムを液体に変換する
        convertToFuel()
        // 燃料を消費して発電する
        val required: Int = getRequiredAmount(tank.getStack())
        if (required <= 0) return false
        if (tank.extract(required, true, HTStorageAccess.INTERNAl).isEmpty) return false
        return if (network.receiveEnergy(energyUsage, true) == energyUsage) {
            tank.extract(required, false, HTStorageAccess.INTERNAl)
            network.receiveEnergy(energyUsage, false)
            true
        } else {
            false
        }
    }

    /**
     * @see [mekanism.generators.common.slot.FluidFuelInventorySlot.fillOrBurn]
     */
    protected fun convertToFuel() {
        val slot: HTItemSlot = fuelSlot
        if (!slot.isEmpty) {
            val stack: ItemStack = slot.getStack()
            val remainAmount: Int = tank.getNeeded()
            if (remainAmount > 0) {
                val fuelValue: Int = getFuelValue(stack)
                if (fuelValue in 1..remainAmount) {
                    val hasContainer: Boolean = stack.hasCraftingRemainingItem()
                    if (hasContainer && stack.count > 1) return
                    tank.insert(getFuelStack(fuelValue), false, HTStorageAccess.INTERNAl)
                    if (hasContainer) {
                        slot.setStack(stack.craftingRemainingItem)
                    } else {
                        slot.shrinkStack(1, false)
                    }
                }
            }
        }
    }

    protected abstract fun getFuelValue(stack: ItemStack): Int

    protected abstract fun getFuelStack(value: Int): FluidStack

    protected abstract fun getRequiredAmount(stack: FluidStack): Int

    protected fun getRequiredAmount(stack: FluidStack, dataMap: DataMapType<Fluid, HTFluidFuelData>): Int =
        stack.fluidHolder.getData(dataMap)?.amount ?: 0

    //    HTFluidInteractable    //

    final override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult =
        interactWith(player, hand, tank)
}
