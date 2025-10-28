package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTFluidItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTFluidItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTDrumBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(blockHolder, pos, state),
    HTFluidInteractable {
    private lateinit var tier: HTDrumTier

    override fun initializeVariables() {
        tier = blockHolder.getAttributeTier()
    }

    private lateinit var tank: HTFluidTank.Mutable

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        tank = builder.addSlot(HTAccessConfig.BOTH, DrumTank(tier, listener))
        return builder.build()
    }

    private lateinit var fillSlot: HTFluidItemSlot
    private lateinit var outputSlot: HTItemSlot.Mutable

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        fillSlot = builder.addSlot(
            HTAccessConfig.INPUT_ONLY,
            HTFluidItemStackSlot.create(tank, listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0)),
        )
        outputSlot = builder.addSlot(
            HTAccessConfig.OUTPUT_ONLY,
            HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)),
        )
        return builder.build()
    }

    //    Save & Read    //

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        componentInput
            .get(RagiumDataComponents.FLUID_CONTENT)
            ?.copy()
            .let(tank::setStack)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumDataComponents.FLUID_CONTENT, tank.getStack())
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        HTFluidItemSlot.moveFluid(fillSlot.getFluidTank(), fillSlot, outputSlot)
        return false
    }

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = interactWith(player, hand, tank)

    //    DrumTank    //

    /**
     * @see mekanism.common.capabilities.fluid.FluidTankFluidTank
     */
    private class DrumTank(tier: HTDrumTier, listener: HTContentListener) :
        HTFluidStackTank(
            tier.getDefaultCapacity(),
            HTPredicates.alwaysTrueBi(),
            HTPredicates.alwaysTrueBi(),
            HTPredicates.alwaysTrue(),
            listener,
        ) {
        val isCreative: Boolean = tier == HTDrumTier.CREATIVE

        override fun insert(stack: ImmutableFluidStack, action: HTStorageAction, access: HTStorageAccess): ImmutableFluidStack? {
            var remainder: ImmutableFluidStack?
            if (isCreative && this.getStack() == null && action.execute && access != HTStorageAccess.EXTERNAL) {
                remainder = super.insert(stack, HTStorageAction.SIMULATE, access)
                if (remainder == null) {
                    setStackUnchecked(stack.copyWithAmount(getCapacity(stack)))
                }
            } else {
                remainder = super.insert(stack, action.combine(!isCreative), access)
            }
            return remainder
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): ImmutableFluidStack? =
            super.extract(amount, action.combine(!isCreative), access)

        override fun setStackSize(amount: Int, action: HTStorageAction): Int = super.setStackSize(amount, action.combine(!isCreative))
    }
}
