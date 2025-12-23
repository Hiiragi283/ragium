package hiiragi283.ragium.common.crafting

import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.input.ImmutableRecipeInput
import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class HTPotionDropRecipe(category: CraftingBookCategory) : HTCustomRecipe(category) {
    override fun matches(input: ImmutableRecipeInput, level: Level): Boolean {
        var drops = 0
        var bottles = 0
        for (stack: ImmutableItemStack? in input) {
            if (stack == null) continue
            if (stack.isOf(RagiumItems.POTION_DROP)) {
                drops++
            } else if (stack.isOf(Items.GLASS_BOTTLE)) {
                bottles++
            }
        }
        return drops == 1 && bottles == 4
    }

    override fun assemble(input: ImmutableRecipeInput, registries: HolderLookup.Provider): ItemStack {
        var potion: PotionContents = PotionContents.EMPTY
        for (stack: ImmutableItemStack? in input) {
            if (stack == null) continue
            val contents: PotionContents = stack.get(DataComponents.POTION_CONTENTS) ?: continue
            if (!HTPotionHelper.isEmpty(contents)) {
                potion = contents
                break
            }
        }
        return when {
            HTPotionHelper.isEmpty(potion) -> ItemStack.EMPTY
            else -> HTPotionHelper.createPotion(Items.POTION, potion, 4)
        }
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 5

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.POTION_DROP
}
