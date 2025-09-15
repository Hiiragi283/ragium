package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.indices
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializersImpl
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
        for (index: Int in input.indices) {
            val stackIn: ItemStack = input.getItem(index)
            if (stackIn.isEmpty) continue
            if (stackIn.`is`(RagiumItems.ICE_CREAM) && !isIceCream) {
                isIceCream = true
            } else if (stackIn.`is`(RagiumCommonTags.Items.FOODS_CHERRY) && !isCherry) {
                isCherry = true
            } else if (stackIn.has(DataComponents.POTION_CONTENTS) && !isPotion) {
                isPotion = true
            } else if (stackIn.`is`(Tags.Items.DYES_GREEN) && !isDye) {
                isDye = true
            }
        }
        return isIceCream && isCherry && isPotion && isDye
    }

    override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack {
        var potion: PotionContents = PotionContents.EMPTY
        for (index: Int in input.indices) {
            val stackIn: ItemStack = input.getItem(index)
            if (stackIn.isEmpty) continue
            if (stackIn.has(DataComponents.POTION_CONTENTS)) {
                potion = stackIn.get(DataComponents.POTION_CONTENTS)!!
            }
        }
        return RagiumAPI.INSTANCE.createSoda(potion)
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width >= 2 && height >= 2

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializersImpl.ICE_CREAM_SODA
}
