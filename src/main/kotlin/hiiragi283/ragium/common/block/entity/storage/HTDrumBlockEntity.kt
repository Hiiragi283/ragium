package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTFluidItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTFluidItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.util.HTItemHelper
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

abstract class HTDrumBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(blockHolder, pos, state),
    HTFluidInteractable {
    constructor(tier: HTDrumTier, pos: BlockPos, state: BlockState) : this(tier.getBlock(), pos, state)

    private lateinit var tank: HTFluidTank.Mutable

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        tank = builder.addSlot(
            HTAccessConfig.BOTH,
            HTVariableFluidStackTank.create(listener) {
                HTItemHelper.processStorageCapacity(
                    level?.random,
                    this,
                    this.blockHolder.getAttributeTier<HTDrumTier>().getDefaultCapacity(),
                )
            },
        )
        return builder.build()
    }

    private lateinit var fillSlot: HTFluidItemSlot
    private lateinit var outputSlot: HTItemSlot.Mutable

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder? {
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
            .getOrDefault(RagiumDataComponents.FLUID_CONTENT, ImmutableFluidStack.EMPTY)
            .copy()
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

    final override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = null

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = interactWith(player, hand, tank)

    //    Impl    //

    class Small(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumTier.SMALL, pos, state)

    class Medium(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumTier.MEDIUM, pos, state)

    class Large(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumTier.LARGE, pos, state)

    class Huge(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumTier.HUGE, pos, state)
}
