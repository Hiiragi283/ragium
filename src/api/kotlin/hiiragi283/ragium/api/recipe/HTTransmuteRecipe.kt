package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.ShapelessRecipe
import net.minecraft.world.level.Level

@Suppress("DEPRECATION")
class HTTransmuteRecipe(
    @Deprecated("Only internal usage") val internalRecipe: ShapelessRecipe,
) : CraftingRecipe {
    constructor(
        group: String,
        category: CraftingBookCategory,
        result: ItemStack,
        target: Ingredient,
        addition: Ingredient,
    ) : this(ShapelessRecipe(group, category, result, NonNullList.of(Ingredient.EMPTY, target, addition)))

    override fun category(): CraftingBookCategory = internalRecipe.category()

    override fun matches(input: CraftingInput, level: Level): Boolean = internalRecipe.matches(input, level)

    override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack {
        // targetとadditionのIngredientを取得
        val targetIngredient: Ingredient = ingredients.getOrNull(0) ?: return ItemStack.EMPTY
        val additionIngredient: Ingredient = ingredients.getOrNull(1) ?: return ItemStack.EMPTY
        // 各Ingredientに一致するItemStackを取得
        var target: ItemStack? = null
        var addition: ItemStack? = null
        for (index: Int in (0 until input.size())) {
            val stackIn: ItemStack = input.getItem(index)
            if (stackIn.isEmpty) continue
            if (targetIngredient.test(stackIn) && target == null) {
                target = stackIn
                continue
            }
            if (additionIngredient.test(stackIn) && addition == null) {
                addition = stackIn
                continue
            }
        }
        if (target == null || addition == null) return ItemStack.EMPTY
        // resultにtargetのデータを乗せる
        val result: ItemStack = getResultItem(registries)
        val stack: ItemStack = target.transmuteCopy(result.item, result.count)
        stack.applyComponents(result.componentsPatch)
        return stack
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 2

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = internalRecipe.getResultItem(registries)

    override fun getRemainingItems(input: CraftingInput): NonNullList<ItemStack> = internalRecipe.getRemainingItems(input)

    override fun getIngredients(): NonNullList<Ingredient> = internalRecipe.ingredients

    override fun getGroup(): String = internalRecipe.group

    override fun getSerializer(): RecipeSerializer<*> = RagiumAPI.Companion.getInstance().getTransmuteRecipeSerializer()
}
