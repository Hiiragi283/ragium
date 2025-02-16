package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.base.*
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTSolidifierRecipe(
    group: String,
    val input: SizedFluidIngredient,
    val catalyst: Optional<Ingredient>,
    output: HTItemOutput,
) : HTMachineRecipeBase(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTSolidifierRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTRecipeCodecs.FLUID_INPUT.fieldOf("fluid_input").forGetter(HTSolidifierRecipe::input),
                    HTRecipeCodecs.CATALYST.forGetter(HTSolidifierRecipe::catalyst),
                    HTRecipeCodecs.itemOutput(),
                ).apply(instance, ::HTSolidifierRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSolidifierRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTSolidifierRecipe::getGroup,
            SizedFluidIngredient.STREAM_CODEC,
            HTSolidifierRecipe::input,
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC),
            HTSolidifierRecipe::catalyst,
            HTItemOutput.STREAM_CODEC,
            { it.itemOutputs[0] },
            ::HTSolidifierRecipe,
        )
    }

    override val itemOutputs: List<HTItemOutput> = listOf(output)

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.SOLIDIFIER

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean {
        if (!this.input.test(input.getFluid(0))) return false
        val catalystItem: ItemStack = input.getItem(0)
        return catalyst.map { it.test(catalystItem) }.orElse(catalystItem.isEmpty)
    }
}
