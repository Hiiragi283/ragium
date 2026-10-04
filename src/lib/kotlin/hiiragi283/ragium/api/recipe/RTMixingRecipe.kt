package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToFluidRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTFluidRecipeInput
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.nelOrElement
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
data class RTMixingRecipe(
    val itemIngredients: Nel<HTItemIngredient>,
    val fluidIngredient: HTFluidIngredient,
    val result: HTItemOrFluidResult,
    override val progressData: HTProgressData
) : HTRecipePredicates.DoubleItemAndFluid,
    HTRecipeFactories.DoubleItemAndFluid<HTItemAndFluidStack>,
    HTProgressRecipe.Simple<HTFluidRecipeInput>,
    HTSerializableRecipe<HTFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTMixingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTItemIngredient.CODEC
                    .nelOrElement(2)
                    .fieldOf(HTConstants.ITEM_INGREDIENT)
                    .forGetter(RTMixingRecipe::itemIngredients),
                HTFluidIngredient.CODEC
                    .fieldOf(HTConstants.FLUID_INGREDIENT)
                    .forGetter(RTMixingRecipe::fluidIngredient),
                HTItemOrFluidResult.CODEC.forGetter(RTMixingRecipe::result),
                HTProgressData.CODEC.forGetter(RTMixingRecipe::progressData)
            ).apply(instance, ::RTMixingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTMixingRecipe> = StreamCodec.composite(
            HTItemIngredient.STREAM_CODEC.nelOf(),
            RTMixingRecipe::itemIngredients,
            HTFluidIngredient.STREAM_CODEC,
            RTMixingRecipe::fluidIngredient,
            HTItemOrFluidResult.STREAM_CODEC,
            RTMixingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTMixingRecipe::progressData,
            ::RTMixingRecipe
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTMixingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    fun toItemAndFluidToFluid(): HTItemAndFluidToFluidRecipe? = when {
        itemIngredients.size > 1 -> null

        !result.content.isRight() -> null

        else -> object : HTItemAndFluidToFluidRecipe {
            override fun test(first: ItemInstance, second: FluidInstance): Boolean =
                itemIngredients.head.test(first) && fluidIngredient.test(second)

            override fun apply(first: ItemInstance, second: FluidInstance): FluidStack = result.create().fluid

            override fun getMatchingStack(
                first: ItemInstance,
                second: FluidInstance
            ): Pair<ItemInstance, FluidInstance> =
                itemIngredients.head.getMatchingStack(first) to fluidIngredient.getMatchingStack(second)

            override fun getProgressData(input: HTItemAndFluidRecipeInput): HTProgressData = progressData
        }
    }

    fun toItemAndFluidToItem(): HTItemAndFluidToItemRecipe? = when {
        itemIngredients.size > 1 -> null

        !result.content.isLeft() -> null

        else -> object : HTItemAndFluidToItemRecipe {
            override fun test(first: ItemInstance, second: FluidInstance): Boolean =
                itemIngredients.head.test(first) && fluidIngredient.test(second)

            override fun apply(first: ItemInstance, second: FluidInstance): ItemStack = result.create().item

            override fun getMatchingStack(
                first: ItemInstance,
                second: FluidInstance
            ): Pair<ItemInstance, FluidInstance> =
                itemIngredients.head.getMatchingStack(first) to fluidIngredient.getMatchingStack(second)

            override fun getProgressData(input: HTItemAndFluidRecipeInput): HTProgressData = progressData
        }
    }

    override fun test(first: ItemInstance, second: ItemInstance, third: FluidInstance): Boolean = when {
        !fluidIngredient.test(third) -> false

        itemIngredients.head.test(first) ->
            itemIngredients.getOrNull(1)?.test(second) ?: HTIngredientHelper.isEmpty(second)

        itemIngredients.head.test(second) ->
            itemIngredients.getOrNull(1)?.test(first) ?: HTIngredientHelper.isEmpty(first)

        else -> false
    }

    override fun apply(first: ItemInstance, second: ItemInstance, third: FluidInstance): HTItemAndFluidStack =
        result.create()

    override fun getMatchingStack(
        first: ItemInstance,
        second: ItemInstance,
        third: FluidInstance
    ): Triple<ItemInstance, ItemInstance, FluidInstance> {
        val fluid: FluidInstance = fluidIngredient.getMatchingStack(third)
        return when {
            itemIngredients.head.test(first) -> Triple(
                itemIngredients.head.getMatchingStack(first),
                itemIngredients.getOrNull(1)?.getMatchingStack(second) ?: ItemStack.EMPTY,
                fluid
            )

            itemIngredients.head.test(second) -> Triple(
                itemIngredients.head.getMatchingStack(second),
                itemIngredients.getOrNull(1)?.getMatchingStack(first) ?: ItemStack.EMPTY,
                fluid
            )

            else -> Triple(ItemStack.EMPTY, ItemStack.EMPTY, fluid)
        }
    }

    override fun getSerializer(): RecipeSerializer<RTMixingRecipe> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<RTMixingRecipe> = RagiumRecipeTypes.MIXING
}
