package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.modifyEnchantment
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeCodecs
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTEnchanterRecipe(
    group: String,
    val firstInput: SizedIngredient,
    val secondInput: SizedIngredient,
    val enchantment: Holder<Enchantment>,
) : HTMachineRecipeBase(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTEnchanterRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    SizedIngredient.FLAT_CODEC.fieldOf("first_item_input").forGetter(HTEnchanterRecipe::firstInput),
                    SizedIngredient.FLAT_CODEC.fieldOf("second_item_input").forGetter(HTEnchanterRecipe::secondInput),
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
            SizedIngredient.STREAM_CODEC,
            HTEnchanterRecipe::firstInput,
            SizedIngredient.STREAM_CODEC,
            HTEnchanterRecipe::secondInput,
            ByteBufCodecs.holderRegistry(Registries.ENCHANTMENT),
            HTEnchanterRecipe::enchantment,
            ::HTEnchanterRecipe,
        )
    }

    override fun getItemOutput(): ItemStack =
        EnchantedBookItem.createForEnchantment(EnchantmentInstance(enchantment, enchantment.value().maxLevel))

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean {
        if (input.getItem(0).getEnchantmentLevel(enchantment) > 0) {
            return false
        }
        return firstInput.test(input.getItem(1)) && secondInput.test(input.getItem(2))
    }

    override fun assemble(input: HTMachineRecipeInput, registries: HolderLookup.Provider): ItemStack =
        input.getItem(0).copyWithCount(1).modifyEnchantment { mutable: ItemEnchantments.Mutable ->
            mutable.set(enchantment, enchantment.value().maxLevel)
            mutable.toImmutable()
        }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ENCHANTER.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ENCHANTER.get()
}
