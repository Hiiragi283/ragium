package hiiragi283.ragium.client.emi.recipe

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.core.api.item.createEnchantedBook
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder

class HTEnchantingEmiRecipe(holder: RecipeHolder<HTEnchantingRecipe>) :
    HTHolderModularEmiRecipe<HTEnchantingRecipe>(
        { recipe: HTEnchantingRecipe, root: UIElement ->
            RagiumModularUIHelper.enchanting(
                root,
                inputSlot(recipe.expIngredient),
                ItemSlot().setItem(ItemStack(Items.BOOK)),
                inputSlot(recipe.ingredient),
                ItemSlot().setItem(createEnchantedBook(recipe.enchantments)),
            )
            root.addChild(RagiumModularUIHelper.fakeProgress(recipe.time))
        },
        RagiumEmiRecipeCategories.ENCHANTING,
        holder,
    )
