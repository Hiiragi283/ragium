package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.extension.toOptional
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

/**
 * 複数のアイテムによるインプットを受け付けるレシピ
 */
abstract class HTMultiItemRecipe(
    group: String,
    val itemInputs: List<HTItemIngredient>,
    val fluidInput: Optional<SizedFluidIngredient>,
    val itemOutput: HTItemOutput,
) : HTMachineRecipeBase(group) {
    final override fun matches(input: HTMachineRecipeInput): Boolean {
        val bool1: Boolean = this.itemInputs[0].test(input, 0)
        val bool2: Boolean = this.itemInputs.getOrNull(1)?.test(input, 1) != false
        val bool3: Boolean = this.itemInputs.getOrNull(2)?.test(input, 2) != false
        val bool4: Boolean = this.fluidInput.map { it.test(input.getFluid(0)) }.orElse(true)
        return bool1 && bool2 && bool3 && bool4
    }

    //    Serializer    //

    class Serializer<T : HTMultiItemRecipe>(
        private val factory: (String, List<HTItemIngredient>, Optional<SizedFluidIngredient>, HTItemOutput) -> T,
    ) : RecipeSerializer<T> {
        private val codec: MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTItemIngredient.CODEC
                        .listOf(1, 3)
                        .fieldOf("item_inputs")
                        .forGetter(HTMultiItemRecipe::itemInputs),
                    SizedFluidIngredient.FLAT_CODEC
                        .optionalFieldOf("fluid_input")
                        .forGetter(HTMultiItemRecipe::fluidInput),
                    HTRecipeCodecs.ITEM_OUTPUT.forGetter(HTMultiItemRecipe::itemOutput),
                ).apply(instance, factory)
        }

        private val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTMultiItemRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC.toList(),
            HTMultiItemRecipe::itemInputs,
            SizedFluidIngredient.STREAM_CODEC.toOptional(),
            HTMultiItemRecipe::fluidInput,
            HTItemOutput.STREAM_CODEC,
            HTMultiItemRecipe::itemOutput,
            factory,
        )

        override fun codec(): MapCodec<T> = codec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
    }
}
