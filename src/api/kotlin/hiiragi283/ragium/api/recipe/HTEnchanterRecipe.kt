package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.createEnchBook
import hiiragi283.ragium.api.extension.modifyEnchantment
import hiiragi283.ragium.api.recipe.base.*
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import java.util.*

class HTEnchanterRecipe(
    group: String,
    val firstInput: HTItemIngredient,
    val secondInput: Optional<HTItemIngredient>,
    val enchantment: Holder<Enchantment>,
) : HTMachineRecipeBase(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTEnchanterRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTItemIngredient.CODEC.fieldOf("first_item_input").forGetter(HTEnchanterRecipe::firstInput),
                    HTItemIngredient.CODEC
                        .optionalFieldOf("second_item_input")
                        .forGetter(HTEnchanterRecipe::secondInput),
                    RegistryFixedCodec
                        .create(Registries.ENCHANTMENT)
                        .fieldOf("enchantment_output")
                        .forGetter(HTEnchanterRecipe::enchantment),
                ).apply(instance, ::HTEnchanterRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTEnchanterRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTEnchanterRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTEnchanterRecipe::firstInput,
            ByteBufCodecs.optional(HTItemIngredient.STREAM_CODEC),
            HTEnchanterRecipe::secondInput,
            ByteBufCodecs.holderRegistry(Registries.ENCHANTMENT),
            HTEnchanterRecipe::enchantment,
            ::HTEnchanterRecipe,
        )
    }

    override val itemOutputs: List<HTItemOutput> = listOf(HTItemOutput.of(createEnchBook(enchantment)))

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean {
        if (input.getItem(0).getEnchantmentLevel(enchantment) > 0) {
            return false
        }
        val second: ItemStack = input.getItem(2)
        return firstInput.test(input, 1) && secondInput.map { it.test(second) }.orElse(second.isEmpty)
    }

    override fun assemble(input: HTMachineRecipeInput, registries: HolderLookup.Provider): ItemStack =
        input.getItem(0).copyWithCount(1).modifyEnchantment { mutable: ItemEnchantments.Mutable ->
            mutable.set(enchantment, enchantment.value().maxLevel)
            mutable.toImmutable()
        }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.ENCHANTER
}
