package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTEnchantmentRecipe(val ingredients: List<SizedIngredient>, val requiredExp: Int, val enchantment: Holder<Enchantment>) :
    Recipe<SmithingRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTEnchantmentRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedIngredient.FLAT_CODEC
                        .listOf()
                        .fieldOf("inputs")
                        .forGetter(HTEnchantmentRecipe::ingredients),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("exp", 0).forGetter(HTEnchantmentRecipe::requiredExp),
                    RegistryFixedCodec
                        .create(Registries.ENCHANTMENT)
                        .fieldOf("enchantment")
                        .forGetter(HTEnchantmentRecipe::enchantment),
                ).apply(instance, ::HTEnchantmentRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTEnchantmentRecipe> = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC.toList(),
            HTEnchantmentRecipe::ingredients,
            ByteBufCodecs.VAR_INT,
            HTEnchantmentRecipe::requiredExp,
            ByteBufCodecs.holderRegistry(Registries.ENCHANTMENT),
            HTEnchantmentRecipe::enchantment,
            ::HTEnchantmentRecipe,
        )
    }

    override fun matches(input: SmithingRecipeInput, level: Level): Boolean {
        TODO("Not yet implemented")
    }

    override fun assemble(input: SmithingRecipeInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack =
        EnchantedBookItem.createForEnchantment(EnchantmentInstance(enchantment, enchantment.value().maxLevel))

    override fun getIngredients(): NonNullList<Ingredient> {
        val list: NonNullList<Ingredient> = NonNullList.create()
        ingredients.map(SizedIngredient::ingredient).forEach(list::add)
        return list
    }

    override fun getToastSymbol(): ItemStack = ItemStack(Blocks.ENCHANTING_TABLE)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipes.ENCHANTMENT_SERIALIZER.get()

    override fun getType(): RecipeType<*> = RagiumRecipes.ENCHANTMENT_TYPE.get()
}
