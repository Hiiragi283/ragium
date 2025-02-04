package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.*

abstract class HTMultiItemRecipe(
    group: String,
    val firstInput: SizedIngredient,
    val secondInput: SizedIngredient,
    val thirdInput: Optional<SizedIngredient>,
    val output: ItemStack,
) : HTMachineRecipeBase(group) {
    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean {
        val bool1: Boolean = this.firstInput.test(input.getItem(0))
        val bool2: Boolean = this.secondInput.test(input.getItem(1))
        val bool3: Boolean = this.thirdInput.map { it.test(input.getItem(2)) }.orElse(true)
        return bool1 && bool2 && bool3
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = output.copy()

    //    Serializer    //

    class Serializer<T : HTMultiItemRecipe>(
        private val factory: (String, SizedIngredient, SizedIngredient, Optional<SizedIngredient>, ItemStack) -> T,
    ) : RecipeSerializer<T> {
        private val codec: MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.GROUP.forGetter(HTMultiItemRecipe::getGroup),
                    SizedIngredient.FLAT_CODEC.fieldOf("first_item_input").forGetter(HTMultiItemRecipe::firstInput),
                    SizedIngredient.FLAT_CODEC
                        .fieldOf("second_item_input")
                        .forGetter(HTMultiItemRecipe::secondInput),
                    SizedIngredient.FLAT_CODEC
                        .optionalFieldOf("third_item_input")
                        .forGetter(HTMultiItemRecipe::thirdInput),
                    HTRecipeCodecs.ITEM_OUTPUT.forGetter(HTMultiItemRecipe::output),
                ).apply(instance, factory)
        }

        private val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTMultiItemRecipe::getGroup,
            SizedIngredient.STREAM_CODEC,
            HTMultiItemRecipe::firstInput,
            SizedIngredient.STREAM_CODEC,
            HTMultiItemRecipe::secondInput,
            ByteBufCodecs.optional(SizedIngredient.STREAM_CODEC),
            HTMultiItemRecipe::thirdInput,
            ItemStack.STREAM_CODEC,
            HTMultiItemRecipe::output,
            factory,
        )

        override fun codec(): MapCodec<T> = codec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
    }
}
