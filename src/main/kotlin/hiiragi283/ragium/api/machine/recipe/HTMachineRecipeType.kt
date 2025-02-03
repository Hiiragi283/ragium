package hiiragi283.ragium.api.machine.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.extension.identifyFunction
import hiiragi283.ragium.api.extension.toDataResult
import hiiragi283.ragium.api.extension.toRegistryStream
import hiiragi283.ragium.api.machine.HTMachineKey
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTMachineRecipeType(val machine: HTMachineKey) :
    RecipeType<HTMachineRecipe>,
    RecipeSerializer<HTMachineRecipe> {
    companion object {
        @JvmField
        val CODEC: Codec<HTMachineRecipeType> = Codec
            .lazyInitialized(BuiltInRegistries.RECIPE_TYPE::byNameCodec)
            .comapFlatMap(
                { recipeType: RecipeType<*> ->
                    (recipeType as? HTMachineRecipeType).toDataResult { "Recipe type: $recipeType not extends HTMachineRecipeType!" }
                },
                identifyFunction(),
            )

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMachineRecipeType> = CODEC.toRegistryStream()
    }

    override fun codec(): MapCodec<HTMachineRecipe> = HTMachineRecipe.CODEC

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, HTMachineRecipe> = HTMachineRecipe.STREAM_CODEC
}
