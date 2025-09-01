package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTDrumVariant
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

abstract class HTDrumBlockEntity(variant: HTDrumVariant, pos: BlockPos, state: BlockState) :
    HTBlockEntity(variant.blockEntityHolder, pos, state),
    HTFluidInteractable {
    private val capacity: Int = variant.capacity
    private lateinit var tank: HTFluidStackTank

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        tank = HTFluidStackTank.create(listener, capacity)
        return HTSimpleFluidTankHolder.generic(null, tank)
    }

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
            .getOrDefault(RagiumAPI.getInstance().getFluidComponent(), SimpleFluidContent.EMPTY)
            .copy()
            .let(tank::setStack)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumAPI.getInstance().getFluidComponent(), SimpleFluidContent.copyOf(tank.getStack()))
    }

    override fun reloadUpgrades() {
        super.reloadUpgrades()
        tank
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = true

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = interactWith(player, hand, tank)

    //    Impl    //

    class Small(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumVariant.SMALL, pos, state)

    class Medium(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumVariant.MEDIUM, pos, state)

    class Large(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumVariant.LARGE, pos, state)

    class Huge(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumVariant.HUGE, pos, state)
}
