package hiiragi283.ragium.client.integration.emi.recipe.processor

import com.mojang.datafixers.util.Either
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.RagiumEmiPlugin
import hiiragi283.ragium.client.integration.emi.recipe.base.HTMultiOutputEmiRecipe
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SingleItemRecipe

class HTCuttingEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<SingleItemRecipe>) :
    HTMultiOutputEmiRecipe<SingleItemRecipe>(category, holder) {
    init {
        val ingredient: Ingredient = recipe.ingredients[0]
        addInput(
            object : HTItemIngredient {
                override fun unwrap(): Either<Pair<TagKey<Item>, Int>, List<ImmutableItemStack>> =
                    Either.right(ingredient.items.mapNotNull(ItemStack::toImmutable))

                override fun test(stack: ImmutableItemStack): Boolean = ingredient.test(stack.unwrap())

                override fun testOnlyType(stack: ImmutableItemStack): Boolean = ingredient.test(stack.unwrap())

                override fun getRequiredAmount(stack: ImmutableItemStack): Int = if (test(stack)) 1 else 0

                override fun hasNoMatchingStacks(): Boolean = ingredient.hasNoItems()
            },
        )

        addOutputs(HTResultHelper.INSTANCE.item(recipe.getResultItem(RagiumEmiPlugin.registryAccess)))
    }

    override fun initInputSlots(widgets: WidgetHolder) {
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(output(0).copy().setAmount(1), getPosition(1), getPosition(2)).catalyst(true)
    }
}
