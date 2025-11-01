package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.common.util.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

class HTFluidType(private val builder: Builder, properties: Properties) : FluidType(properties) {
    companion object {
        @JvmField
        val IS_ULTRA_WARM: (Level, BlockPos, FluidStack) -> Boolean = { level: Level, _, _ ->
            level.dimensionType().ultraWarm
        }

        @JvmStatic
        fun create(builderAction: Builder.() -> Unit): (Properties) -> HTFluidType = { prop: Properties ->
            Builder().apply(builderAction).build(prop)
        }

        @JvmStatic
        fun explosive(power: Float): (Properties) -> HTFluidType = create {
            canVaporize = IS_ULTRA_WARM
            interactLevel = { level: Level, pos: BlockPos ->
                level.explode(
                    null,
                    null,
                    null,
                    pos.center,
                    power,
                    true,
                    Level.ExplosionInteraction.BLOCK,
                )
            }
        }

        @JvmStatic
        fun solidify(result: HTItemResult): (Properties) -> HTFluidType = create {
            dropItem = result
        }
    }

    val dropItem: HTItemResult? = builder.dropItem

    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean =
        builder.canVaporize?.invoke(level, pos, stack) ?: super.isVaporizedOnPlacement(level, pos, stack)

    override fun onVaporize(
        player: Player?,
        level: Level,
        pos: BlockPos,
        stack: FluidStack,
    ) {
        super.onVaporize(player, level, pos, stack)
        builder.interactLevel?.invoke(level, pos)
        dropItem?.getStackOrNull(level.registryAccess())?.let { stack: ImmutableItemStack ->
            if (player != null) {
                HTItemDropHelper.giveStackTo(player, stack)
            } else {
                HTItemDropHelper.dropStackAt(level, pos, stack)
            }
        }
    }

    class Builder {
        var canVaporize: ((Level, BlockPos, FluidStack) -> Boolean)? = null
        var dropItem: HTItemResult? = null
        var interactLevel: ((Level, BlockPos) -> Unit)? = null

        fun build(properties: Properties): HTFluidType = HTFluidType(this, properties)
    }
}
