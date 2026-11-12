package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTFluidSeparatingRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.input.HTFluidRecipeInput
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class RTFluidReactingRecipe(
    val ingredients: Nel<HTFluidIngredient>,
    val catalyst: HTItemCatalyst,
    val result: HTFluidSeparatingRecipe.Result,
    override val progressData: HTProgressData,
) : HTRecipeFactories.CatalysedSingleInput<RTFluidReactingRecipe.Input, List<FluidInstance>, HTFluidSeparatingRecipe.Output>,
    HTProgressRecipe.Holder,
    HTSerializableRecipe<RTFluidReactingRecipe.Input> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTFluidReactingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTFluidIngredient.CODEC
                    .compactNelFieldOf(HTConstants.INGREDIENT, HTConstants.INGREDIENTS, 2)
                    .forGetter(RTFluidReactingRecipe::ingredients),
                HTItemCatalyst.CODEC.forGetter(RTFluidReactingRecipe::catalyst),
                HTFluidSeparatingRecipe.Result.CODEC.fieldOf(HTConstants.RESULTS).forGetter(RTFluidReactingRecipe::result),
                HTProgressRecipe.Holder.progressCodec(),
            ).apply(instance, ::RTFluidReactingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTFluidReactingRecipe> = StreamCodec.composite(
            HTFluidIngredient.STREAM_CODEC.nelOf(),
            RTFluidReactingRecipe::ingredients,
            HTItemCatalyst.STREAM_CODEC,
            RTFluidReactingRecipe::catalyst,
            HTFluidSeparatingRecipe.Result.STREAM_CODEC,
            RTFluidReactingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTFluidReactingRecipe::progressData,
            ::RTFluidReactingRecipe,
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTFluidReactingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(input: List<FluidInstance>, catalyst: ItemInstance): Boolean =
        HTIngredientHelper.match(ingredients, input) && this.catalyst.test(catalyst)

    override fun apply(input: List<FluidInstance>, catalyst: ItemInstance): HTFluidSeparatingRecipe.Output = result.create()

    override fun getMatchingStack(input: List<FluidInstance>): List<FluidInstance> = HTIngredientHelper.getMatchingStacks(ingredients, input)

    override fun produce(input: Input): HTFluidSeparatingRecipe.Output = apply(input.fluids, input.catalyst)

    override fun matches(input: Input): Boolean = test(input.fluids, input.catalyst)

    override fun getProgressData(first: List<FluidInstance>, second: ItemInstance): HTProgressData = progressData

    override fun getProgressData(input: Input): HTProgressData = getProgressData(input.fluids, input.catalyst)

    override fun getSerializer(): RecipeSerializer<RTFluidReactingRecipe> = RagiumRecipeSerializers.FLUID_REACTING

    override fun getType(): RecipeType<RTFluidReactingRecipe> = RagiumRecipeTypes.FLUID_REACTING

    //    Input    //

    @JvmRecord
    data class Input(val fluids: List<FluidStack>, val catalyst: ItemStack) : HTFluidRecipeInput {
        override val fluidSize: Int get() = fluids.size

        override fun getFluid(index: Int): FluidStack = fluids[index]

        override fun asFluidList(): List<FluidStack> = this.fluids

        override fun getItem(index: Int): ItemStack = error("No item for index: $index")

        override fun size(): Int = 0
    }
}
