package hiiragi283.ragium.common.recipe.crafting

import hiiragi283.ragium.api.recipe.HTCustomRecipe
import hiiragi283.ragium.api.recipe.input.ImmutableRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class HTGravitationalUpgradeRecipe(category: CraftingBookCategory) : HTCustomRecipe(category) {
    override fun matches(input: ImmutableRecipeInput, level: Level): Boolean {
        var chestplate = 0
        var component = 0
        for (stack: ImmutableItemStack? in input) {
            if (stack == null) continue
            if (stack.isOf(ItemTags.CHEST_ARMOR)) {
                chestplate++
            } else if (stack.isOf(RagiumItems.GRAVITATIONAL_UNIT)) {
                component++
            }
        }
        return chestplate == 1 && component == 1
    }

    override fun assemble(input: ImmutableRecipeInput, registries: HolderLookup.Provider): ItemStack {
        var item: ItemStack = ItemStack.EMPTY
        for (stack: ImmutableItemStack? in input) {
            if (stack == null) continue
            if (stack.isOf(ItemTags.CHEST_ARMOR)) {
                item = stack.unwrap()
                break
            }
        }
        if (!item.isEmpty) {
            item.set(RagiumDataComponents.ANTI_GRAVITY, true)
        }
        return item
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.GRAVITATIONAL_UPGRADE
}
