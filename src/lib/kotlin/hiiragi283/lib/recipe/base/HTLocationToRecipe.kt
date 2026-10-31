package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.builder.HTLocationToFluidRecipeBuilder
import hiiragi283.lib.recipe.HTRecipeFactory
import hiiragi283.lib.recipe.ingredient.HTBiomeCondition
import hiiragi283.lib.recipe.input.HTLocationRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.level.ServerLevel
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiFunction

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
typealias HTLocationToFluidRecipe = HTLocationToRecipe<FluidStack>

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
interface HTLocationToRecipe<OUTPUT : Any> :
    HTRecipePredicates.DoubleInput<HTLocationRecipeInput, ServerLevel, BlockPos>,
    HTRecipeFactory<HTLocationRecipeInput, OUTPUT>,
    HTProgressRecipe.Double<HTLocationRecipeInput, ServerLevel, BlockPos>,
    BiFunction<ServerLevel, BlockPos, OUTPUT> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    open class BasicFluid(val condition: HTBiomeCondition, val result: HTFluidResult, override val progressData: HTProgressData) :
        HTLocationToRecipe<FluidStack>,
        HTProgressRecipe.Holder {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicFluid> codec(factory: HTLocationToFluidRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> =
                HTCodecs.recordMap { instance ->
                    instance.group(
                        HTBiomeCondition.CODEC.fieldOf("condition").forGetter(BasicFluid::condition),
                        HTFluidResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(BasicFluid::result),
                        HTProgressRecipe.Holder.progressCodec(),
                    ).apply(instance, factory::create)
                }

            @JvmStatic
            fun <RECIPE : BasicFluid> streamCodec(
                factory: HTLocationToFluidRecipeBuilder.Factory<RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTBiomeCondition.STREAM_CODEC,
                BasicFluid::condition,
                HTFluidResult.STREAM_CODEC,
                BasicFluid::result,
                HTProgressData.STREAM_CODEC,
                BasicFluid::progressData,
                factory::create,
            )
        }

        override fun matches(input: HTLocationRecipeInput): Boolean = test(input.level, input.pos)

        override fun test(first: ServerLevel, second: BlockPos): Boolean = condition.test(first, second)

        override fun produce(input: HTLocationRecipeInput): FluidStack = result.create()

        override fun apply(first: ServerLevel, second: BlockPos): FluidStack = result.create()

        override fun getProgressData(input: HTLocationRecipeInput): HTProgressData = progressData

        override fun getProgressData(first: ServerLevel, second: BlockPos): HTProgressData = progressData
    }
}
