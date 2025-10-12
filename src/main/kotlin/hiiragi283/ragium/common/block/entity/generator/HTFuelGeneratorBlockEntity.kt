package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.block.entity.HTBlockEntityFactory
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.fluid.ImmutableFluidStack
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.ImmutableItemStack
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTFluidFuelItemStackSlot
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid

abstract class HTFuelGeneratorBlockEntity(variant: HTGeneratorVariant, pos: BlockPos, state: BlockState) :
    HTGeneratorBlockEntity(
        variant,
        pos,
        state,
    ),
    HTFluidInteractable {
    companion object {
        @JvmStatic
        fun createSimple(
            itemValueGetter: (ImmutableItemStack) -> Int,
            fuelContent: HTFluidContent<*, *, *>,
            fluidAmountGetter: (RegistryAccess, Holder<Fluid>) -> Int,
            variant: HTGeneratorVariant,
        ): HTBlockEntityFactory<HTFuelGeneratorBlockEntity> = HTBlockEntityFactory {
            pos: BlockPos,
            state: BlockState,
            ->
            Simple(itemValueGetter, fuelContent, fluidAmountGetter, variant, pos, state)
        }
    }

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
            RagiumConfig.COMMON.generatorInputTankCapacity,
            filter = { stack: ImmutableFluidStack ->
                val access: RegistryAccess = level?.registryAccess() ?: return@input false
                getRequiredAmount(access, stack) > 0
            },
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
        network: HTEnergyBattery,
    ): Boolean {
        // スロット内のアイテムを液体に変換する
        slot.fillOrBurn()
        // 燃料を消費して発電する
        val required: Int = getRequiredAmount(level.registryAccess(), tank.getStack())
        if (required <= 0) return false
        if (tank.extract(required, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL).isEmpty()) return false
        val usage: Int = getModifiedEnergy(energyUsage)
        return if (network.insertEnergy(usage, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) > 0) {
            tank.extract(required, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
            network.insertEnergy(usage, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
            true
        } else {
            false
        }
    }

    protected abstract fun getFuelValue(stack: ImmutableItemStack): Int

    protected abstract fun getFuelStack(value: Int): ImmutableFluidStack

    protected abstract fun getRequiredAmount(access: RegistryAccess, stack: ImmutableFluidStack): Int

    //    HTFluidInteractable    //

    final override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult =
        interactWith(player, hand, tank)

    //    Simple    //

    private class Simple(
        private val itemValueGetter: (ImmutableItemStack) -> Int,
        private val fuelContent: HTFluidContent<*, *, *>,
        private val fluidAmountGetter: (RegistryAccess, Holder<Fluid>) -> Int,
        variant: HTGeneratorVariant,
        pos: BlockPos,
        state: BlockState,
    ) : HTFuelGeneratorBlockEntity(variant, pos, state) {
        override fun getFuelValue(stack: ImmutableItemStack): Int = itemValueGetter(stack)

        override fun getFuelStack(value: Int): ImmutableFluidStack = fuelContent.toStorageStack(value)

        override fun getRequiredAmount(access: RegistryAccess, stack: ImmutableFluidStack): Int = fluidAmountGetter(access, stack.holder())
    }
}
