package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.extension.toRegistryStream
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer

interface HTDefinitionRecipe<T : RecipeInput> : Recipe<T> {
    fun getDefinition(): DataResult<HTRecipeDefinition>

    //    Factory    //

    fun interface Factory<T : HTDefinitionRecipe<*>> {
        fun fromDefinition(definition: HTRecipeDefinition): DataResult<T>
    }

    //    Serializer    //

    class Serializer<T : HTDefinitionRecipe<*>>(val name: String, private val factory: Factory<T>) : RecipeSerializer<T> {
        private val codec: MapCodec<T> =
            HTRecipeDefinition.CODEC.flatXmap(factory::fromDefinition, HTDefinitionRecipe<*>::getDefinition)

        fun createRecipe(definition: HTRecipeDefinition): DataResult<T> = factory.fromDefinition(definition)

        override fun codec(): MapCodec<T> = codec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = codec.toRegistryStream()
    }
}
