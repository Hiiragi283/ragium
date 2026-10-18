package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.createOrEmpty
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.serialization.network.nelOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

interface RTCentrifugingRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<RTCentrifugingRecipe.Output>,
    HTProgressRecipe<SingleRecipeInput> {

    @JvmRecord
    data class Output(val items: List<ItemStack>, val fluid: FluidStack) {
        constructor(item: ItemStack, fluid: FluidStack) : this(listOf(item), fluid)
    }

    @JvmRecord
    data class Result(val items: Nel<HTItemResult>, val fluid: Optional<HTFluidResult>) {
        companion object {
            @JvmField
            val CODEC: Codec<Result> = HTCodecs.record { instance ->
                instance.group(
                    HTItemResult.CODEC
                        .compactNelFieldOf(HTConstants.ITEM, HTConstants.ITEMS, 2)
                        .forGetter(Result::items),
                    HTFluidResult.CODEC.optionalFieldOf(HTConstants.FLUID).forGetter(Result::fluid)
                ).apply(instance, ::Result)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Result> = StreamCodec.composite(
                HTItemResult.STREAM_CODEC.nelOf(),
                Result::items,
                HTStreamCodecs.optional(HTFluidResult.STREAM_CODEC),
                Result::fluid,
                ::Result
            )
        }

        fun create(): Output = Output(items.map(HTItemResult::create), fluid.createOrEmpty())
    }

    @JvmRecord
    data class Basic(val ingredient: HTItemIngredient, val result: Result, override val progressData: HTProgressData) :
        RTCentrifugingRecipe,
        HTProgressRecipe.Simple<SingleRecipeInput>,
        HTSerializableRecipe<SingleRecipeInput> {
        companion object {
            @JvmField
            val CODEC: MapCodec<Basic> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(Basic::ingredient),
                    Result.CODEC.fieldOf(HTConstants.RESULT).forGetter(Basic::result),
                    HTProgressData.CODEC.forGetter(Basic::progressData)
                ).apply(instance, ::Basic)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Basic> = StreamCodec.composite(
                HTItemIngredient.STREAM_CODEC,
                Basic::ingredient,
                Result.STREAM_CODEC,
                Basic::result,
                HTProgressData.STREAM_CODEC,
                Basic::progressData,
                ::Basic
            )

            @JvmField
            val SERIALIZER: RecipeSerializer<Basic> = RecipeSerializer(CODEC, STREAM_CODEC)
        }

        override fun test(input: ItemInstance): Boolean = ingredient.test(input)

        override fun apply(input: ItemInstance): Output = result.create()

        override fun getMatchingStack(input: ItemInstance): ItemInstance = ingredient.getMatchingStack(input)

        override fun getSerializer(): RecipeSerializer<Basic> = RagiumRecipeSerializers.CENTRIFUGING

        override fun getType(): RecipeType<Basic> = RagiumRecipeTypes.CENTRIFUGING
    }
}
