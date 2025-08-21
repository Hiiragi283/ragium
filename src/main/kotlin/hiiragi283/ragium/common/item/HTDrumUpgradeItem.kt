package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.getCapability
import hiiragi283.ragium.api.registry.HTDeferredBlockHolder
import hiiragi283.ragium.util.variant.HTDrumVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LevelEvent
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import java.util.function.Supplier

/**
 * @see [me.desht.pneumaticcraft.common.item.AbstractChestUpgradeKitItem]
 */
abstract class HTDrumUpgradeItem(
    private val filter: List<HTDeferredBlockHolder<*, *>>,
    private val newDrum: Supplier<out Block>,
    properties: Properties,
) : Item(properties) {
    constructor(
        filter: List<HTDrumVariant>,
        newDrum: HTDrumVariant,
        properties: Properties,
    ) : this(filter.map(HTDrumVariant::blockHolder), newDrum.blockHolder, properties)

    override fun useOn(context: UseOnContext): InteractionResult {
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        val state: BlockState = level.getBlockState(pos)
        if (filter.any(state::`is`)) {
            if (!level.isClientSide) {
                var fluid: FluidStack = FluidStack.EMPTY
                level.getCapability(Capabilities.FluidHandler.BLOCK, pos)?.let { handler: IFluidHandler ->
                    fluid = handler.getFluidInTank(0).copy()
                }
                val newState: BlockState = newDrum.get().defaultBlockState()
                level.setBlockAndUpdate(pos, newState)
                val player: Player? = context.player
                if (player is ServerPlayer) {
                    level.playSound(null, pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS)
                    level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state))
                }

                level
                    .getCapability(Capabilities.FluidHandler.BLOCK, pos)
                    ?.fill(fluid, IFluidHandler.FluidAction.EXECUTE)
                val drop = ItemStack(state.block)
                player?.let { dropStackAt(it, drop) } ?: dropStackAt(level, pos, drop)
                context.itemInHand.shrink(1)
            }
            return InteractionResult.sidedSuccess(level.isClientSide)
        }
        return InteractionResult.FAIL
    }

    //    Impl    //

    class Medium(properties: Properties) :
        HTDrumUpgradeItem(
            listOf(HTDrumVariant.SMALL),
            HTDrumVariant.MEDIUM,
            properties,
        )

    class Large(properties: Properties) :
        HTDrumUpgradeItem(
            listOf(HTDrumVariant.SMALL, HTDrumVariant.MEDIUM),
            HTDrumVariant.LARGE,
            properties,
        )

    class Huge(properties: Properties) :
        HTDrumUpgradeItem(
            listOf(HTDrumVariant.SMALL, HTDrumVariant.MEDIUM, HTDrumVariant.LARGE),
            HTDrumVariant.HUGE,
            properties,
        )
}
