package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTListItemOrFluidIngredient
import hiiragi283.lib.recipe.input.HTFluidRecipeInput
import hiiragi283.lib.recipe.input.asList
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class RTReactingRecipe(
    val ingredients: HTListItemOrFluidIngredient,
    val catalyst: HTItemCatalyst,
    val result: HTItemOrFluidResult,
    override val progressData: HTProgressData,
) : HTRecipePredicates.TripleInput<RTReactingRecipe.Input, List<ItemInstance>, List<FluidInstance>, ItemInstance>,
    HTRecipeFactories.TripleInput<RTReactingRecipe.Input, List<ItemInstance>, List<FluidInstance>, ItemInstance, HTItemAndFluidStack>,
    HTProgressRecipe.Holder,
    HTSerializableRecipe<RTReactingRecipe.Input> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTReactingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTListItemOrFluidIngredient.CODEC.fieldOf(HTConstants.INGREDIENTS).forGetter(RTReactingRecipe::ingredients),
                HTItemCatalyst.CODEC.forGetter(RTReactingRecipe::catalyst),
                HTItemOrFluidResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(RTReactingRecipe::result),
                HTProgressRecipe.Holder.progressCodec(),
            ).apply(instance, ::RTReactingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTReactingRecipe> = StreamCodec.composite(
            HTListItemOrFluidIngredient.STREAM_CODEC,
            RTReactingRecipe::ingredients,
            HTItemCatalyst.STREAM_CODEC,
            RTReactingRecipe::catalyst,
            HTItemOrFluidResult.STREAM_CODEC,
            RTReactingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTReactingRecipe::progressData,
            ::RTReactingRecipe,
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTReactingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(first: List<ItemInstance>, second: List<FluidInstance>, third: ItemInstance): Boolean =
        ingredients.test(first, second) && catalyst.test(third)

    override fun matches(input: Input): Boolean = test(input.asList(), input.asFluidList(), input.catalyst)

    override fun apply(first: List<ItemInstance>, second: List<FluidInstance>, third: ItemInstance): HTItemAndFluidStack = result.create()

    override fun produce(input: Input): HTItemAndFluidStack = apply(input.asList(), input.asFluidList(), input.catalyst)

    override fun getMatchingStack(
        first: List<ItemInstance>,
        second: List<FluidInstance>,
        third: ItemInstance,
    ): Triple<List<ItemInstance>, List<FluidInstance>, ItemInstance> = Triple(
        ingredients.getMatchingItems(first),
        ingredients.getMatchingFluids(second),
        catalyst.getMatchingStack(third),
    )

    override fun getProgressData(first: List<ItemInstance>, second: List<FluidInstance>, third: ItemInstance): HTProgressData = progressData

    override fun getProgressData(input: Input): HTProgressData = progressData

    override fun getSerializer(): RecipeSerializer<RTReactingRecipe> = RagiumRecipeSerializers.REACTING

    override fun getType(): RecipeType<RTReactingRecipe> = RagiumRecipeTypes.REACTING

    //    Input    //

    @JvmRecord
    data class Input(val items: List<ItemStack>, val fluids: List<FluidStack>, val catalyst: ItemStack) : HTFluidRecipeInput {
        override val fluidSize: Int get() = fluids.size

        override fun getFluid(index: Int): FluidStack = fluids[index]

        override fun getItem(index: Int): ItemStack = items[index]

        override fun size(): Int = items.size
    }
}
