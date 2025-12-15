package hiiragi283.ragium.common.crafting

import hiiragi283.ragium.api.recipe.HTCustomRecipe
import hiiragi283.ragium.api.recipe.input.ImmutableRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class HTEternalUpgradeRecipe(category: CraftingBookCategory) : HTCustomRecipe(category) {
    override fun matches(input: ImmutableRecipeInput, level: Level): Boolean {
        var damageable = 0
        var component = 0
        for (stack: ImmutableItemStack? in input) {
            if (stack == null) continue
            if (stack.unwrap().isDamageableItem) {
                damageable++
            } else if (stack.isOf(RagiumItems.ETERNAL_COMPONENT)) {
                component++
            }
        }
        return damageable == 1 && component == 1
    }

    override fun assemble(input: ImmutableRecipeInput, registries: HolderLookup.Provider): ItemStack {
        var item: ItemStack = ItemStack.EMPTY
        for (stack: ImmutableItemStack? in input) {
            if (stack == null) continue
            if (stack.unwrap().isDamageableItem) {
                item = stack.unwrap()
                break
            }
        }
        if (!item.isEmpty) {
            item.set(DataComponents.UNBREAKABLE, Unbreakable(true))
        }
        return item
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ETERNAL_UPGRADE
}
