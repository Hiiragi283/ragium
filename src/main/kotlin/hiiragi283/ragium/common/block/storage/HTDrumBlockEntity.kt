package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.extension.getItemData
import hiiragi283.ragium.api.fluid.HTTieredFluidTank
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.SimpleFluidContent

class HTDrumBlockEntity(pos: BlockPos, state: BlockState, override val machineTier: HTMachineTier) :
    HTBlockEntity(RagiumBlockEntityTypes.DRUM, pos, state),
    HTBlockEntityHandlerProvider,
    HTMachineTierProvider {
    constructor(pos: BlockPos, state: BlockState) : this(
        pos,
        state,
        state.getItemData(RagiumAPI.DataMapTypes.MACHINE_TIER) ?: HTMachineTier.BASIC,
    )

    private val fluidTank = HTTieredFluidTank(machineTier, this::setChanged)

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        fluidTank.writeToNBT(registries, tag)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        fluidTank.readFromNBT(registries, tag)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        val content: SimpleFluidContent =
            componentInput.getOrDefault(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.EMPTY)
        fluidTank.fluid = content.copy()
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.copyOf(fluidTank.fluid))
    }

    override fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult = when {
        FluidUtil.interactWithFluidHandler(
            player,
            InteractionHand.MAIN_HAND,
            fluidTank,
        ) -> ItemInteractionResult.SUCCESS

        else -> super.onRightClickedWithItem(stack, state, level, pos, player, hand, hitResult)
    }

    //    HTBlockEntityHandlerProvider    //

    override fun getFluidHandler(direction: Direction?): HTTieredFluidTank = fluidTank
}
