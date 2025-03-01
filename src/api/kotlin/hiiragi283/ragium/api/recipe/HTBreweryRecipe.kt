package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.recipe.base.*
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.alchemy.Potion
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTBreweryRecipe(group: String, itemInputs: List<HTItemIngredient>, val potion: Holder<Potion>) :
    HTMultiItemRecipe(
        group,
        itemInputs,
        Optional.of(SizedFluidIngredient.of(Tags.Fluids.WATER, 1000)),
        HTItemOutput.of(createPotionStack(potion, 3)),
    ) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTBreweryRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTItemIngredient.CODEC
                        .listOf(1, 3)
                        .fieldOf("item_inputs")
                        .forGetter(HTBreweryRecipe::itemInputs),
                    Potion.CODEC
                        .fieldOf("effect")
                        .forGetter(HTBreweryRecipe::potion),
                ).apply(instance, ::HTBreweryRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTBreweryRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTBreweryRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC.toList(),
            HTBreweryRecipe::itemInputs,
            ByteBufCodecs.holderRegistry(Registries.POTION),
            HTBreweryRecipe::potion,
            ::HTBreweryRecipe,
        )

        @JvmField
        val WATER_INGREDIENT: SizedFluidIngredient = SizedFluidIngredient.of(Tags.Fluids.WATER, FluidType.BUCKET_VOLUME)
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.BREWERY
}
