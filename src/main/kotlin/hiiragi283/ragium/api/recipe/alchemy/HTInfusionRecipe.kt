package hiiragi283.ragium.api.recipe.alchemy

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.api.recipe.WeightedIngredient
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.world.World

class HTInfusionRecipe(override val inputs: List<WeightedIngredient>, override val result: HTRecipeResult) : HTAlchemyRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTInfusionRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    WeightedIngredient.CODEC
                        .listOf()
                        .fieldOf("inputs")
                        .forGetter(HTInfusionRecipe::inputs),
                    HTRecipeResult.CODEC
                        .fieldOf("output")
                        .forGetter(HTInfusionRecipe::result),
                ).apply(instance, ::HTInfusionRecipe)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTInfusionRecipe> = PacketCodec.tuple(
            WeightedIngredient.LIST_PACKET_CODEC,
            HTInfusionRecipe::inputs,
            HTRecipeResult.PACKET_CODEC,
            HTInfusionRecipe::result,
            ::HTInfusionRecipe,
        )
    }

    override fun matches(input: HTAlchemyRecipe.Input, world: World): Boolean = input.matches(
        getInput(0),
        getInput(1),
        getInput(2),
        getInput(3),
    )

    override fun getSerializer(): RecipeSerializer<*> = Serializer

    data object Serializer : RecipeSerializer<HTInfusionRecipe> {
        override fun codec(): MapCodec<HTInfusionRecipe> = CODEC

        override fun packetCodec(): PacketCodec<RegistryByteBuf, HTInfusionRecipe> = PACKET_CODEC
    }
}
