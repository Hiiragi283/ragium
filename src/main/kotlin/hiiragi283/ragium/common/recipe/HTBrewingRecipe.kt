package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.HTBrewingRecipeData
import hiiragi283.ragium.api.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class HTBrewingRecipe(val left: TagKey<Item>, val ingredient: HTItemIngredient, val contents: PotionContents) :
    HTCombineRecipe,
    HTRecipe.Fake {
    companion object {
        @JvmField
        val FLUID_INGREDIENT: HTFluidIngredient = RagiumPlatform.INSTANCE.fluidCreator().water(1000)

        @JvmStatic
        fun createBaseRecipe(recipeData: HTBrewingRecipeData): HTBrewingRecipe =
            HTBrewingRecipe(Tags.Items.CROPS_NETHER_WART, recipeData.getIngredient(), recipeData.getBasePotion())

        @JvmStatic
        private fun createDropIngredient(recipeData: HTBrewingRecipeData): HTItemIngredient = HTItemIngredient(
            DataComponentIngredient.of(
                false,
                DataComponents.POTION_CONTENTS,
                recipeData.getBasePotion(),
                RagiumItems.POTION_DROP,
            ),
            1,
        )

        @JvmStatic
        fun createLongRecipe(recipeData: HTBrewingRecipeData): HTBrewingRecipe? {
            val long: PotionContents = recipeData.getLongPotion().takeUnless { it.allEffects.none() } ?: return null
            return HTBrewingRecipe(Tags.Items.DUSTS_REDSTONE, createDropIngredient(recipeData), long)
        }

        @JvmStatic
        fun createStrongRecipe(recipeData: HTBrewingRecipeData): HTBrewingRecipe? {
            val strong: PotionContents = recipeData.getStrongPotion().takeUnless { it.allEffects.none() } ?: return null
            return HTBrewingRecipe(Tags.Items.DUSTS_GLOWSTONE, createDropIngredient(recipeData), strong)
        }
    }

    val potionDrop: ItemStack = HTPotionHelper.createPotion(RagiumItems.POTION_DROP, contents)

    override fun getLeftRequiredCount(): Int = 1

    override fun getRightRequiredCount(): Int = ingredient.getRequiredAmount()

    override fun getRequiredAmount(input: HTRecipeInput): Int = FLUID_INGREDIENT.getRequiredAmount()

    override fun test(left: ImmutableItemStack, right: ImmutableItemStack, fluid: ImmutableFluidStack): Boolean {
        val bool1: Boolean = left.isOf(this.left)
        val bool2: Boolean = ingredient.test(right)
        val bool3: Boolean = FLUID_INGREDIENT.test(fluid)
        return bool1 && bool2 && bool3
    }

    override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = potionDrop.toImmutable()
}
