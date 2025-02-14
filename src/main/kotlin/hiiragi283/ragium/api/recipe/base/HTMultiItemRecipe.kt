package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import java.util.Optional

/**
 * 複数のアイテムによるインプットを受け付けるレシピ
 */
abstract class HTMultiItemRecipe(
    group: String,
    val firstInput: HTItemIngredient,
    val secondInput: HTItemIngredient,
    val thirdInput: Optional<HTItemIngredient>,
    itemResult: HTItemResult,
) : HTMachineRecipeBase(group) {
    override val itemResults: List<HTItemResult> = listOf(itemResult)

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean {
        val bool1: Boolean = this.firstInput.test(input, 0)
        val bool2: Boolean = this.secondInput.test(input, 1)
        val bool3: Boolean = this.thirdInput.map { it.test(input, 2) }.orElse(true)
        return bool1 && bool2 && bool3
    }

    //    Serializer    //

    class Serializer<T : HTMultiItemRecipe>(
        private val factory: (String, HTItemIngredient, HTItemIngredient, Optional<HTItemIngredient>, HTItemResult) -> T,
    ) : RecipeSerializer<T> {
        private val codec: MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTItemIngredient.CODEC.fieldOf("first_item_input").forGetter(HTMultiItemRecipe::firstInput),
                    HTItemIngredient.CODEC
                        .fieldOf("second_item_input")
                        .forGetter(HTMultiItemRecipe::secondInput),
                    HTItemIngredient.CODEC
                        .optionalFieldOf("third_item_input")
                        .forGetter(HTMultiItemRecipe::thirdInput),
                    HTRecipeCodecs.itemResult(),
                ).apply(instance, factory)
        }

        private val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTMultiItemRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTMultiItemRecipe::firstInput,
            HTItemIngredient.STREAM_CODEC,
            HTMultiItemRecipe::secondInput,
            ByteBufCodecs.optional(HTItemIngredient.STREAM_CODEC),
            HTMultiItemRecipe::thirdInput,
            HTItemResult.STREAM_CODEC,
            { it.itemResults[0] },
            factory,
        )

        override fun codec(): MapCodec<T> = codec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
    }
}
