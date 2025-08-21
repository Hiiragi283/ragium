package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.data.HTFluidFuelData
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
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
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.registries.datamaps.DataMapType

abstract class HTFuelGeneratorBlockEntity(variant: HTGeneratorVariant, pos: BlockPos, state: BlockState) :
    HTGeneratorBlockEntity(
        variant,
        pos,
        state,
    ),
    HTFluidInteractable {
    final override val inventory: HTItemHandler = object : HTItemStackHandler(1) {
        override fun onContentsChanged() {
            this@HTFuelGeneratorBlockEntity.onContentsChanged()
        }

        override val inputSlots: IntArray = intArrayOf(0)
        override val outputSlots: IntArray = intArrayOf()
    }
    protected val tank: HTFluidStackTank =
        object : HTFluidStackTank(variant.tankCapacity, this) {
            override fun isFluidValid(stack: FluidStack): Boolean = getRequiredAmount(stack) > 0
        }

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
        return if (tank.canDrain(required, true)) {
            tank.drain(required, false)
            handleEnergy(network) > 0
        } else {
            false
        }
    }

    /**
     * @see [mekanism.generators.common.slot.FluidFuelInventorySlot.fillOrBurn]
     */
    protected fun convertToFuel() {
        val stack: ItemStack = inventory.getStackInSlot(0)
        if (!stack.isEmpty) {
            val remainAmount: Int = tank.space
            if (remainAmount > 0) {
                val fuelValue: Int = getFuelValue(stack)
                if (fuelValue in 1..remainAmount) {
                    val hasContainer: Boolean = stack.hasCraftingRemainingItem()
                    if (hasContainer && stack.count > 1) return
                    tank.fill(getFuelStack(fuelValue), false)
                    if (hasContainer) {
                        inventory.setStackInSlot(0, stack.craftingRemainingItem)
                    } else {
                        stack.shrink(1)
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

    //    Slot    //

    final override fun addInputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1))
    }

    final override fun addOutputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {}
}
