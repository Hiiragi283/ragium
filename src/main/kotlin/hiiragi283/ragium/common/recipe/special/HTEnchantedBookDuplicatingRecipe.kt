package hiiragi283.ragium.common.recipe.special

import com.google.common.primitives.Ints
import hiiragi283.core.util.HTExperienceHelper
import hiiragi283.ragium.api.recipe.HTDuplicatingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.EnchantmentHelper

data object HTEnchantedBookDuplicatingRecipe : HTDuplicatingRecipe {
    override fun testItem(stack: ItemStack): Boolean = stack.`is`(Items.ENCHANTED_BOOK)

    override fun getRequiredMatter(stack: ItemStack): Int = EnchantmentHelper
        .getEnchantmentsForCrafting(stack)
        .let(HTExperienceHelper::getTotalMaxCost)
        .let(HTExperienceHelper::fluidAmountFromExp)
        .let(Ints::saturatedCast)

    override val time: Int = 200

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BOOK_DUPLICATING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.DUPLICATING.get()
}
