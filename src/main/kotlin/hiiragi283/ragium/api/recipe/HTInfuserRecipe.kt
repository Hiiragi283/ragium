package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTInfuserRecipe(
    group: String,
    val itemInput: HTItemIngredient,
    val fluidInput: SizedFluidIngredient,
    itemOutputs: List<HTItemOutput>,
    fluidOutputs: List<HTFluidOutput>,
) : HTFluidOutputRecipe(group, itemOutputs, fluidOutputs) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTInfuserRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTRecipeCodecs.group(),
                        HTRecipeCodecs.ITEM_INPUT.forGetter(HTInfuserRecipe::itemInput),
                        HTRecipeCodecs.FLUID_INPUT.forGetter(HTInfuserRecipe::fluidInput),
                        HTRecipeCodecs.itemOutputs(0, 1),
                        HTRecipeCodecs.fluidOutputs(0, 1),
                    ).apply(instance, ::HTInfuserRecipe)
            }.validate(HTFluidOutputRecipe::validate)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTInfuserRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTInfuserRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTInfuserRecipe::itemInput,
            SizedFluidIngredient.STREAM_CODEC,
            HTInfuserRecipe::fluidInput,
            HTItemOutput.STREAM_CODEC.toList(),
            HTInfuserRecipe::itemOutputs,
            HTFluidOutput.STREAM_CODEC.toList(),
            HTInfuserRecipe::fluidOutputs,
            ::HTInfuserRecipe,
        )
    }

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean =
        itemInput.test(input, 0) && fluidInput.test(input.getFluid(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.INFUSER.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.INFUSER.get()
}
