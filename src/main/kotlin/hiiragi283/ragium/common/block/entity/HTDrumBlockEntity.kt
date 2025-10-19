package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.getFluidStack
import hiiragi283.ragium.api.storage.fluid.setFluidStack
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTFluidItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTFluidItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.common.util.HTItemHelper
import hiiragi283.ragium.common.variant.HTDrumVariant
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.fluids.SimpleFluidContent

abstract class HTDrumBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(type, pos, state),
    HTFluidInteractable {
    constructor(variant: HTDrumVariant, pos: BlockPos, state: BlockState) : this(variant.blockEntityHolder, pos, state)

    private lateinit var tank: HTFluidTank.Mutable

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        tank = HTVariableFluidStackTank.create(listener) {
            HTItemHelper.processStorageCapacity(level?.random, this, getDefaultTankCapacity())
        }
        return HTSimpleFluidTankHolder.generic(null, tank)
    }

    private lateinit var fillSlot: HTFluidItemSlot
    private lateinit var outputSlot: HTItemSlot.Mutable

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        fillSlot = HTFluidItemStackSlot.fill(tank, listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        outputSlot = HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2))
        return HTSimpleItemSlotHolder(null, listOf(fillSlot), listOf(outputSlot))
    }

    protected abstract fun getDefaultTankCapacity(): Long

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult = RagiumMenuTypes.DRUM.openMenu(player, name, this, ::writeExtraContainerData)

    //    Save & Read    //

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        componentInput
            .getOrDefault(RagiumDataComponents.FLUID_CONTENT, SimpleFluidContent.EMPTY)
            .copy()
            .let(tank::setFluidStack)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumDataComponents.FLUID_CONTENT, SimpleFluidContent.copyOf(tank.getFluidStack()))
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        fillSlot.fillTank(outputSlot)
        return false
    }

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = interactWith(player, hand, tank)

    //    Impl    //

    class Small(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumVariant.SMALL, pos, state) {
        override fun getDefaultTankCapacity(): Long = RagiumConfig.COMMON.smallDrumCapacity.asLong
    }

    class Medium(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumVariant.MEDIUM, pos, state) {
        override fun getDefaultTankCapacity(): Long = RagiumConfig.COMMON.mediumDrumCapacity.asLong
    }

    class Large(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumVariant.LARGE, pos, state) {
        override fun getDefaultTankCapacity(): Long = RagiumConfig.COMMON.largeDrumCapacity.asLong
    }

    class Huge(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumVariant.HUGE, pos, state) {
        override fun getDefaultTankCapacity(): Long = RagiumConfig.COMMON.hugeDrumCapacity.asLong
    }
}
