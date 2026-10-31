package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTFluidRecipeInput
import hiiragi283.lib.recipe.input.asList
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.HTRecipeResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.serialization.network.nelOf
import hiiragi283.lib.util.Ior
import hiiragi283.lib.util.getLeftList
import hiiragi283.lib.util.getRightList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class RTReactingRecipe(
    val ingredients: Ingredients,
    val catalyst: HTItemCatalyst,
    val result: Result,
    override val progressData: HTProgressData,
) : HTRecipePredicates.TripleInput<RTReactingRecipe.Input, List<ItemInstance>, List<FluidInstance>, ItemInstance>,
    HTRecipeFactories.TripleInput<RTReactingRecipe.Input, List<ItemInstance>, List<FluidInstance>, ItemInstance, RTReactingRecipe.Output>,
    HTProgressRecipe.Holder,
    HTSerializableRecipe<RTReactingRecipe.Input> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTReactingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                Ingredients.CODEC.fieldOf(HTConstants.INGREDIENTS).forGetter(RTReactingRecipe::ingredients),
                HTItemCatalyst.CODEC.forGetter(RTReactingRecipe::catalyst),
                Result.CODEC.fieldOf(HTConstants.RESULTS).forGetter(RTReactingRecipe::result),
                HTProgressRecipe.Holder.progressCodec(),
            ).apply(instance, ::RTReactingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTReactingRecipe> = StreamCodec.composite(
            Ingredients.STREAM_CODEC,
            RTReactingRecipe::ingredients,
            HTItemCatalyst.STREAM_CODEC,
            RTReactingRecipe::catalyst,
            Result.STREAM_CODEC,
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

    override fun apply(first: List<ItemInstance>, second: List<FluidInstance>, third: ItemInstance): Output = result.create()

    override fun produce(input: Input): Output = apply(input.asList(), input.asFluidList(), input.catalyst)

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

    //    Output    //

    @JvmRecord
    data class Output(val items: List<ItemStack>, val fluids: List<FluidStack>)

    //    Ingredient    //

    @JvmRecord
    data class Ingredients(val contents: Ior<Nel<HTItemIngredient>, Nel<HTFluidIngredient>>) {
        companion object {
            @JvmField
            val CODEC: MapCodec<Ingredients> = HTCodecs.ior(
                HTItemIngredient.CODEC.compactNelFieldOf(HTConstants.ITEM, HTConstants.ITEMS),
                HTFluidIngredient.CODEC.compactNelFieldOf(HTConstants.FLUID, HTConstants.FLUIDS),
            ).xmap(::Ingredients, Ingredients::contents)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Ingredients> = HTStreamCodecs.ior(
                HTItemIngredient.STREAM_CODEC.nelOf(),
                HTFluidIngredient.STREAM_CODEC.nelOf(),
            ).map(::Ingredients, Ingredients::contents)
        }

        fun test(items: List<ItemInstance>, fluids: List<FluidInstance>): Boolean = contents.fold(
            { HTIngredientHelper.match(it, items) },
            { HTIngredientHelper.match(it, fluids) },
            { items1: Nel<HTItemIngredient>, fluids1: Nel<HTFluidIngredient> ->
                HTIngredientHelper.match(items1, items) && HTIngredientHelper.match(fluids1, fluids)
            },
        )

        fun getMatchingItems(items: List<ItemInstance>): List<ItemInstance> =
            contents.getLeftList().let { HTIngredientHelper.getMatchingStacks(it, items) }

        fun getMatchingFluids(fluids: List<FluidInstance>): List<FluidInstance> =
            contents.getRightList().let { HTIngredientHelper.getMatchingStacks(it, fluids) }

        fun itemDisplays(): List<SlotDisplay> = contents.getLeftList().map(HTItemIngredient::display)

        fun fluidDisplays(): List<SlotDisplay> = contents.getRightList().map(HTFluidIngredient::display)
    }

    //    Result    //

    @JvmRecord
    data class Result(val contents: Ior<Nel<HTItemResult>, Nel<HTFluidResult>>) : HTRecipeResult<Output> {
        companion object {
            @JvmField
            val CODEC: MapCodec<Result> = HTCodecs.ior(
                HTItemResult.CODEC.compactNelFieldOf(HTConstants.ITEM, HTConstants.ITEMS),
                HTFluidResult.CODEC.compactNelFieldOf(HTConstants.FLUID, HTConstants.FLUIDS),
            ).xmap(::Result, Result::contents)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Result> =
                HTStreamCodecs.ior(HTItemResult.STREAM_CODEC.nelOf(), HTFluidResult.STREAM_CODEC.nelOf()).map(::Result, Result::contents)
        }

        override fun getId(): Identifier? = contents.getLeftList().firstOrNull()?.getId() ?: contents.getRightList().firstOrNull()?.getId()

        override fun create(): Output = Output(contents.getLeftList().map(HTItemResult::create), contents.getRightList().map(HTFluidResult::create))

        fun itemDisplays(): List<SlotDisplay> = contents.getLeftList().map(HTItemResult::display)

        fun fluidDisplays(): List<SlotDisplay> = contents.getRightList().map(HTFluidResult::display)
    }
}
