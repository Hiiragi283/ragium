package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.api.extension.toCenterVec3
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.TriPredicate
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import java.util.function.BiConsumer

class HTFluidType(builder: Builder, properties: Properties) : FluidType(properties) {
    companion object {
        @JvmField
        val IS_ULTRA_WARM: TriPredicate<Level, BlockPos, FluidStack> = TriPredicate { level: Level, _, _ ->
            level.dimensionType().ultraWarm
        }

        @JvmStatic
        fun create(builderAction: Builder.() -> Unit): (Properties) -> HTFluidType = { prop: Properties ->
            Builder().apply(builderAction).build(prop)
        }

        @JvmStatic
        fun explosive(power: Float): (Properties) -> HTFluidType = create {
            canVaporize = IS_ULTRA_WARM
            interactLevel = BiConsumer { level: Level, pos: BlockPos ->
                level.explode(
                    null,
                    null,
                    null,
                    pos.toCenterVec3(),
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

    class Builder {
        var canVaporize: TriPredicate<Level, BlockPos, FluidStack>? = null
        var dropItem: HTItemResult? = null
        var interactLevel: BiConsumer<Level, BlockPos>? = null

        fun build(properties: Properties): HTFluidType = HTFluidType(this, properties)
    }
}
