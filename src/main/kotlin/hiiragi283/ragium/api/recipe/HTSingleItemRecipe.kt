package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
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

abstract class HTSingleItemRecipe(private val group: String, val input: SizedIngredient, val output: ItemStack) : HTMachineRecipeBase {
    override fun matches(input: HTRecipeInput, level: Level): Boolean = this.input.test(input.getItem(0))

    final override fun assemble(input: HTRecipeInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    final override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = output.copy()

    final override fun getGroup(): String = group

    //    Serializer    //

    class Serializer<T : HTSingleItemRecipe>(private val factory: (String, SizedIngredient, ItemStack) -> T) : RecipeSerializer<T> {
        private val codec: MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(HTSingleItemRecipe::getGroup),
                    SizedIngredient.FLAT_CODEC.fieldOf("input").forGetter(HTSingleItemRecipe::input),
                    ItemStack.STRICT_CODEC.fieldOf("output").forGetter(HTSingleItemRecipe::output),
                ).apply(instance, factory)
        }

        private val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTSingleItemRecipe::getGroup,
            SizedIngredient.STREAM_CODEC,
            HTSingleItemRecipe::input,
            ItemStack.STREAM_CODEC,
            HTSingleItemRecipe::output,
            factory,
        )

        override fun codec(): MapCodec<T> = codec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
    }
}
