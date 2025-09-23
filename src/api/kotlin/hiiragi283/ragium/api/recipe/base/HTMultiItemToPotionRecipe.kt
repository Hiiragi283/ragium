package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.api.recipe.HTMultiItemToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents

interface HTMultiItemToPotionRecipe : HTMultiItemToObjRecipe {
    val ingredients: List<HTItemIngredient>
    val potion: PotionContents

    override fun test(input: HTMultiItemRecipeInput): Boolean {
        fun List<HTItemIngredient>.test(index: Int): Boolean {
            val stackIn: ItemStack = input.getItem(index)
            return this.getOrNull(index)?.test(stackIn) ?: stackIn.isEmpty
        }

        val bool1: Boolean = ingredients.test(0)
        val bool2: Boolean = ingredients.test(1)
        val bool3: Boolean = ingredients.test(2)
        return bool1 && bool2 && bool3
    }

    override fun assemble(input: HTMultiItemRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) createItemStack(Items.POTION, DataComponents.POTION_CONTENTS, potion) else ItemStack.EMPTY

    override fun isIncomplete(): Boolean =
        ingredients.isEmpty() || ingredients.any(HTItemIngredient::hasNoMatchingStacks) || potion.allEffects.none()
}
