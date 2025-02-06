package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
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
import java.util.*

class HTGrinderRecipe(
    group: String,
    val input: SizedIngredient,
    private val output: ItemStack,
    val secondOutput: Optional<HTChancedItemStack>,
) : HTMachineRecipeBase(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTGrinderRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTRecipeCodecs.ITEM_INPUT.forGetter(HTGrinderRecipe::input),
                    HTRecipeCodecs.ITEM_OUTPUT.forGetter(HTGrinderRecipe::output),
                    HTChancedItemStack.CODEC.optionalFieldOf("second_output").forGetter(HTGrinderRecipe::secondOutput),
                ).apply(instance, ::HTGrinderRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTGrinderRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTGrinderRecipe::getGroup,
            SizedIngredient.STREAM_CODEC,
            HTGrinderRecipe::input,
            ItemStack.STREAM_CODEC,
            HTGrinderRecipe::output,
            ByteBufCodecs.optional(HTChancedItemStack.STREAM_CODEC),
            HTGrinderRecipe::secondOutput,
            ::HTGrinderRecipe,
        )
    }

    override fun getItemOutput(): ItemStack = output.copy()

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean = this.input.test(input.getItem(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.GRINDER.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.GRINDER.get()
}
