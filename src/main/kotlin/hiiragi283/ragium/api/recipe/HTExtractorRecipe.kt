package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

class HTExtractorRecipe(
    private val group: String,
    val input: SizedIngredient,
    val itemOutput: Optional<ItemStack>,
    val fluidOutput: Optional<FluidStack>,
) : Recipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTExtractorRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(HTExtractorRecipe::group),
                        SizedIngredient.FLAT_CODEC.fieldOf("input").forGetter(HTExtractorRecipe::input),
                        ItemStack.STRICT_CODEC.optionalFieldOf("item_output").forGetter(HTExtractorRecipe::itemOutput),
                        FluidStack.CODEC.optionalFieldOf("fluid_output").forGetter(HTExtractorRecipe::fluidOutput),
                    ).apply(instance, ::HTExtractorRecipe)
            }.validate { recipe: HTExtractorRecipe ->
                if (recipe.itemOutput.isEmpty && recipe.fluidOutput.isEmpty) {
                    return@validate DataResult.error { "Either item or fluid output required!" }
                }
                DataResult.success(recipe)
            }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTExtractorRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTExtractorRecipe::group,
            SizedIngredient.STREAM_CODEC,
            HTExtractorRecipe::input,
            ByteBufCodecs.optional(ItemStack.STREAM_CODEC),
            HTExtractorRecipe::itemOutput,
            ByteBufCodecs.optional(FluidStack.STREAM_CODEC),
            HTExtractorRecipe::fluidOutput,
            ::HTExtractorRecipe,
        )
    }

    override fun matches(input: SingleRecipeInput, level: Level): Boolean = this.input.test(input.item())

    override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = itemOutput.orElse(ItemStack.EMPTY).copy()

    override fun getIngredients(): NonNullList<Ingredient> = NonNullList.withSize(1, input.ingredient())

    override fun getGroup(): String = group

    override fun getToastSymbol(): ItemStack = super.getToastSymbol()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXTRACTOR.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTOR.get()
}
