package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toOptional
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTUniversalRecipe
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.Optional

class HTSolidifyingRecipe(val ingredient: SizedFluidIngredient, val catalyst: Optional<SizedIngredient>, val output: HTItemOutput) :
    HTUniversalRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTSolidifyingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedFluidIngredient.FLAT_CODEC
                        .fieldOf(RagiumConst.FLUID_INPUT)
                        .forGetter(HTSolidifyingRecipe::ingredient),
                    SizedIngredient.FLAT_CODEC
                        .optionalFieldOf(RagiumConst.CATALYST)
                        .forGetter(HTSolidifyingRecipe::catalyst),
                    HTItemOutput.CODEC
                        .fieldOf(RagiumConst.ITEM_OUTPUT)
                        .forGetter(HTSolidifyingRecipe::output),
                ).apply(instance, ::HTSolidifyingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSolidifyingRecipe> = StreamCodec.composite(
            SizedFluidIngredient.STREAM_CODEC,
            HTSolidifyingRecipe::ingredient,
            SizedIngredient.STREAM_CODEC.toOptional(),
            HTSolidifyingRecipe::catalyst,
            HTItemOutput.STREAM_CODEC,
            HTSolidifyingRecipe::output,
            ::HTSolidifyingRecipe,
        )
    }

    override fun matches(input: HTUniversalRecipeInput, level: Level): Boolean {
        val bool1: Boolean = ingredient.test(input.getFluid(0))
        val catalystStack: ItemStack = input.getItem(0)
        val bool2: Boolean = when {
            catalyst.isEmpty -> catalystStack.isEmpty
            else -> catalyst.get().test(catalystStack)
        }
        return bool1 && bool2
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SOLIDIFYING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SOLIDIFYING.get()
}
