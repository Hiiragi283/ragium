package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.ingredient.testOrEmpty
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
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

@JvmRecord
data class RTMixingRecipe(
    val itemIngredients: Nel<HTItemIngredient>,
    val fluidIngredient: HTFluidIngredient,
    val result: HTItemOrFluidResult,
    override val progressData: HTProgressData
) : HTRecipePredicates.DoubleItemAndFluid,
    HTRecipeFactories.DoubleItemAndFluid<HTItemAndFluidStack>,
    HTProgressRecipe.Simple<HTRecipeInput>,
    HTSerializableRecipe<HTRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTMixingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTCodecs.mapPair(
                    HTItemIngredient.CODEC.compactNelFieldOf(HTConstants.ITEM, HTConstants.ITEMS, 2),
                    HTFluidIngredient.CODEC.fieldOf(HTConstants.FLUID)
                ).fieldOf(HTConstants.INGREDIENT).forGetter { it.itemIngredients to it.fluidIngredient },
                HTItemOrFluidResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(RTMixingRecipe::result),
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

    constructor(
        ingredient: Pair<Nel<HTItemIngredient>, HTFluidIngredient>,
        result: HTItemOrFluidResult,
        progressData: HTProgressData
    ) : this(
        ingredient.first,
        ingredient.second,
        result,
        progressData
    )

    fun toItemAndFluidToFluid(): HTItemAndFluidToRecipe.BasicFluid? = when {
        itemIngredients.size > 1 -> null

        else -> this.result.content.getRight()?.let {
            HTItemAndFluidToRecipe.BasicFluid(
                this.itemIngredients.head,
                this.fluidIngredient,
                it,
                this.progressData
            )
        }
    }

    fun toItemAndFluidToItem(): HTItemAndFluidToRecipe.BasicItem? = when {
        itemIngredients.size > 1 -> null

        else -> this.result.content.getLeft()?.let {
            HTItemAndFluidToRecipe.BasicItem(
                this.itemIngredients.head,
                this.fluidIngredient,
                it,
                this.progressData
            )
        }
    }

    override fun test(first: ItemInstance, second: ItemInstance, third: FluidInstance): Boolean = when {
        !fluidIngredient.test(third) -> false
        else -> itemIngredients.head.test(first) && itemIngredients.getOrNull(1).testOrEmpty(second)
    }

    override fun apply(first: ItemInstance, second: ItemInstance, third: FluidInstance): HTItemAndFluidStack =
        result.create()

    override fun getMatchingStack(
        first: ItemInstance,
        second: ItemInstance,
        third: FluidInstance
    ): Triple<ItemInstance, ItemInstance, FluidInstance> = Triple(
        itemIngredients.head.getMatchingStack(first),
        itemIngredients.getOrNull(1)?.getMatchingStack(second) ?: ItemStack.EMPTY,
        fluidIngredient.getMatchingStack(third)
    )

    override fun getSerializer(): RecipeSerializer<RTMixingRecipe> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<RTMixingRecipe> = RagiumRecipeTypes.MIXING
}
