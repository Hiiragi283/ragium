package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.data.HTFluidFuelData
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.HTItemStackSlot
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTGeneratorVariant
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
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

    protected val tank: HTFluidStackTank = HTFluidStackTank(variant.tankCapacity, this)
        .setValidator { stack: FluidStack -> getRequiredAmount(stack) > 0 }

    final override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConst.TANK, tank)
    }

    final override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConst.TANK, tank)
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
        val required: Int = getRequiredAmount(tank.fluid)
        if (required <= 0) return false
        return if (tank.canDrain(required, true) && network.receiveEnergy(energyUsage, true) == energyUsage) {
            tank.drain(required, false)
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
            val remainAmount: Int = tank.space
            if (remainAmount > 0) {
                val fuelValue: Int = getFuelValue(stack)
                if (fuelValue in 1..remainAmount) {
                    val hasContainer: Boolean = stack.hasCraftingRemainingItem()
                    if (hasContainer && stack.count > 1) return
                    tank.fill(getFuelStack(fuelValue), false)
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

    final override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler =
        HTFilteredFluidHandler(tank, HTFluidFilter.FILL_ONLY)

    //    HTFluidInteractable    //

    final override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult =
        interactWith(player, hand, tank)
}
