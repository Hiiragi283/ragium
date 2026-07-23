package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.recipe.HTSerializableRecipe
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import hiiragi283.ragium.support.recipe.base.HTBasicEnchantingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.Enchantment

class RTEnchantingRecipe(ingredient: HTItemIngredient, val holder: Holder<Enchantment>) :
    HTBasicEnchantingRecipe(ingredient),
    HTSerializableRecipe<HTEnchantingRecipe.Input> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTEnchantingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(RTEnchantingRecipe::ingredient),
                    HTCodecs.holder(Registries.ENCHANTMENT).fieldOf("enchantment").forGetter(RTEnchantingRecipe::holder),
                ).apply(instance, ::RTEnchantingRecipe)
        }
    }

    override val requiredExpAmount: Int get() {
        val enchantment: Enchantment = holder.value()
        return enchantment.getMaxCost(enchantment.maxLevel)
    }

    override fun getMatchingStacks(first: ItemStack, second: ItemStack, third: Int): Triple<ItemStack, ItemStack, Int> = Triple(
        ItemStack.EMPTY,
        ingredient.getMatchingStack(second),
        getRequiredExpAmount(first, second),
    )

    override fun applyEnchantment(stack: ItemStack): ItemStack {
        val result: ItemStack = stack.copyWithCount(1)
        result.enchant(holder, holder.value().maxLevel)
        return result
    }

    override fun testBase(stack: ItemStack): Boolean = stack.supportsEnchantment(holder)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.HOLDER_ENCHANTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ENCHANTING

    override fun isIncomplete(): Boolean = ingredient.isIncomplete()
}
