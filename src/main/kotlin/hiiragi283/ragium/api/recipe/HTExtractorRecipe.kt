package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.base.HTFluidOutputRecipe
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeCodecs
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

class HTExtractorRecipe(
    group: String,
    val input: SizedIngredient,
    itemOutput: Optional<ItemStack>,
    fluidOutput: Optional<FluidStack>,
) : HTFluidOutputRecipe(group, itemOutput, fluidOutput) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTExtractorRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTRecipeCodecs.group(),
                        HTRecipeCodecs.ITEM_INPUT.forGetter(HTExtractorRecipe::input),
                        ItemStack.CODEC.optionalFieldOf("item_output").forGetter(HTExtractorRecipe::itemOutput),
                        FluidStack.CODEC.optionalFieldOf("fluid_output").forGetter(HTExtractorRecipe::fluidOutput),
                    ).apply(instance, ::HTExtractorRecipe)
            }.validate(HTFluidOutputRecipe::validate)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTExtractorRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTExtractorRecipe::getGroup,
            SizedIngredient.STREAM_CODEC,
            HTExtractorRecipe::input,
            ByteBufCodecs.optional(ItemStack.STREAM_CODEC),
            HTExtractorRecipe::itemOutput,
            ByteBufCodecs.optional(FluidStack.STREAM_CODEC),
            HTExtractorRecipe::fluidOutput,
            ::HTExtractorRecipe,
        )
    }

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean = this.input.test(input.getItem(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXTRACTOR.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTOR.get()
}
