package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.data.HTFluidFuelData
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.common.storage.fluid.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTFluidFuelItemStackSlot
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumMenuTypes
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
    protected lateinit var slot: HTFluidFuelItemStackSlot
        private set

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // fuel
        slot = HTFluidFuelItemStackSlot.create(
            tank,
            ::getFuelValue,
            ::getFuelStack,
            listener,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(1),
        )
        return HTSimpleItemSlotHolder(this, listOf(slot), listOf())
    }

    protected lateinit var tank: HTVariableFluidStackTank
        private set

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder? {
        tank = HTVariableFluidStackTank.input(
            listener,
            RagiumConfig.CONFIG.generatorInputTankCapacity,
            filter = { stack: FluidStack -> getRequiredAmount(stack) > 0 },
        )
        return HTSimpleFluidTankHolder.input(this, tank)
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
        slot.fillOrBurn()
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

    protected abstract fun getFuelValue(stack: ItemStack): Int

    protected abstract fun getFuelStack(value: Int): FluidStack

    protected abstract fun getRequiredAmount(stack: FluidStack): Int

    protected fun getRequiredAmount(stack: FluidStack, dataMap: DataMapType<Fluid, HTFluidFuelData>): Int =
        stack.fluidHolder.getData(dataMap)?.amount ?: 0

    //    HTFluidInteractable    //

    final override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult =
        interactWith(player, hand, tank)
}
