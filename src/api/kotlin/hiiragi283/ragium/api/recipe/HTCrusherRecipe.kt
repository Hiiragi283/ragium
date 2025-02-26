package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.recipe.base.*
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

class HTCrusherRecipe(group: String, val input: HTItemIngredient, val outputs: List<HTItemOutput>) : HTMachineRecipeBase(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCrusherRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTRecipeCodecs.ITEM_INPUT.forGetter(HTCrusherRecipe::input),
                    HTItemOutput.CODEC
                        .listOf(1, 3)
                        .fieldOf("item_outputs")
                        .forGetter(HTCrusherRecipe::outputs),
                ).apply(instance, ::HTCrusherRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTCrusherRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTMachineRecipeBase::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTCrusherRecipe::input,
            HTItemOutput.STREAM_CODEC.toList(),
            HTCrusherRecipe::outputs,
            ::HTCrusherRecipe,
        )
    }

    override fun matches(input: HTMachineRecipeInput): Boolean = this.input.test(input, 0)

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.CRUSHER
}
