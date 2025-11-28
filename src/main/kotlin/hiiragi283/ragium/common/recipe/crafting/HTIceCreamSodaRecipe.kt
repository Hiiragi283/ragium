package hiiragi283.ragium.common.recipe.crafting

import hiiragi283.ragium.api.recipe.HTCustomRecipe
import hiiragi283.ragium.api.recipe.input.ImmutableRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.util.HTPotionHelper
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.Tags

class HTIceCreamSodaRecipe(category: CraftingBookCategory) : HTCustomRecipe(category) {
    override fun matches(input: ImmutableRecipeInput, level: Level): Boolean {
        var iceCreams = 0
        var cherries = 0
        var potions = 0
        var dyes = 0
        for (stack: ImmutableItemStack? in input) {
            if (stack == null) continue
            if (stack.isOf(RagiumItems.ICE_CREAM)) {
                iceCreams++
            } else if (stack.isOf(CommonMaterialPrefixes.FOOD.itemTagKey(FoodMaterialKeys.RAGI_CHERRY))) {
                cherries++
            } else if (stack.has(DataComponents.POTION_CONTENTS)) {
                potions++
            } else if (stack.isOf(Tags.Items.DYES_GREEN)) {
                dyes++
            }
        }
        return iceCreams == 1 && cherries == 1 && potions == 1 && dyes == 1
    }

    override fun assemble(input: ImmutableRecipeInput, registries: HolderLookup.Provider): ItemStack {
        var potion: PotionContents = PotionContents.EMPTY
        for (stack: ImmutableItemStack? in input) {
            if (stack == null) continue
            if (stack.has(DataComponents.POTION_CONTENTS)) {
                potion = stack.get(DataComponents.POTION_CONTENTS)!!
            }
        }
        return HTPotionHelper.createPotion(RagiumItems.ICE_CREAM_SODA, potion)
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 4

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ICE_CREAM_SODA
}
