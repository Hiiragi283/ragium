package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class HTCentrifugingRecipe(private val group: String, val ingredient: Ingredient, private val results: List<ItemStack>) :
    Recipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCentrifugingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(HTCentrifugingRecipe::getGroup),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(HTCentrifugingRecipe::ingredient),
                    ItemStack.STRICT_CODEC
                        .listOf(1, 4)
                        .fieldOf("results")
                        .forGetter(HTCentrifugingRecipe::results),
                ).apply(instance, ::HTCentrifugingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTCentrifugingRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTCentrifugingRecipe::getGroup,
            Ingredient.CONTENTS_STREAM_CODEC,
            HTCentrifugingRecipe::ingredient,
            ItemStack.STREAM_CODEC.toList(),
            HTCentrifugingRecipe::results,
            ::HTCentrifugingRecipe,
        )
    }

    fun getResultItem(index: Int): ItemStack = results.getOrNull(index)?.copy() ?: ItemStack.EMPTY

    override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())

    override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = results[0]

    override fun getIngredients(): NonNullList<Ingredient> = NonNullList.create<Ingredient>().apply { add(ingredient) }

    override fun getGroup(): String = group

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipes.CENTRIFUGING.serializer

    override fun getType(): RecipeType<*> = RagiumRecipes.CENTRIFUGING
}
