package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.listOf
import hiiragi283.ragium.api.extension.listOrElement
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTListedRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTAlloyingRecipe(val ingredients: List<SizedIngredient>, val outputs: List<HTItemOutput>) : Recipe<HTListedRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTAlloyingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedIngredient.FLAT_CODEC
                        .listOf(2, 2)
                        .fieldOf("inputs")
                        .forGetter(HTAlloyingRecipe::ingredients),
                    HTItemOutput.CODEC
                        .listOrElement()
                        .fieldOf("outputs")
                        .forGetter(HTAlloyingRecipe::outputs),
                ).apply(instance, ::HTAlloyingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTAlloyingRecipe> = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC.listOf(),
            HTAlloyingRecipe::ingredients,
            HTItemOutput.STREAM_CODEC.listOf(),
            HTAlloyingRecipe::outputs,
            ::HTAlloyingRecipe,
        )
    }

    override fun matches(input: HTListedRecipeInput, level: Level): Boolean {
        val bool1: Boolean = ingredients[0].test(input[0]) && ingredients[1].test(input[1])
        val bool2: Boolean = ingredients[0].test(input[1]) && ingredients[1].test(input[0])
        return bool1 && bool2
    }

    override fun assemble(input: HTListedRecipeInput, registries: HolderLookup.Provider): ItemStack = throw UnsupportedOperationException()

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = throw UnsupportedOperationException()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ALLOYING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ALLOYING.get()
}
