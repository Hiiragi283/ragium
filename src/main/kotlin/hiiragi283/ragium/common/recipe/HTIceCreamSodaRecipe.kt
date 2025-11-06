package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.recipe.input.ImmutableRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.Tags

class HTIceCreamSodaRecipe(category: CraftingBookCategory) : CustomRecipe(category) {
    override fun matches(input: CraftingInput, level: Level): Boolean {
        var isIceCream = false
        var isCherry = false
        var isPotion = false
        var isDye = false
        for (stack: ImmutableItemStack? in ImmutableRecipeInput(input)) {
            if (stack == null) continue
            if (stack.isOf(RagiumItems.ICE_CREAM) && !isIceCream) {
                isIceCream = true
            } else if (stack.isOf(CommonMaterialPrefixes.FOOD.itemTagKey(FoodMaterialKeys.RAGI_CHERRY)) && !isCherry) {
                isCherry = true
            } else if (stack.has(DataComponents.POTION_CONTENTS) && !isPotion) {
                isPotion = true
            } else if (stack.isOf(Tags.Items.DYES_GREEN) && !isDye) {
                isDye = true
            }
        }
        return isIceCream && isCherry && isPotion && isDye
    }

    override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack {
        var potion: PotionContents = PotionContents.EMPTY
        for (stack: ImmutableItemStack? in ImmutableRecipeInput(input)) {
            if (stack == null) continue
            if (stack.has(DataComponents.POTION_CONTENTS)) {
                potion = stack.get(DataComponents.POTION_CONTENTS)!!
            }
        }
        return RagiumPlatform.INSTANCE.createSoda(potion)
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width >= 2 && height >= 2

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ICE_CREAM_SODA
}
