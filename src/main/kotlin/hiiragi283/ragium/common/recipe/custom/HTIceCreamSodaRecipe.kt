package hiiragi283.ragium.common.recipe.custom

import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.DyeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import java.util.*

class HTIceCreamSodaRecipe(category: CraftingBookCategory) : CustomRecipe(category) {
    override fun matches(input: CraftingInput, level: Level): Boolean {
        var isIceCream = false
        var isCherry = false
        var isPotion = false
        var isDye = false
        for (index: Int in (0 until input.size())) {
            val stackIn: ItemStack = input.getItem(index)
            if (stackIn.isEmpty) continue
            if (stackIn.`is`(RagiumItems.ICE_CREAM) && !isIceCream) {
                isIceCream = true
            } else if (stackIn.`is`(RagiumItemTags.FOODS_CHERRY) && !isCherry) {
                isCherry = true
            } else if (stackIn.has(DataComponents.POTION_CONTENTS) && !isPotion) {
                isPotion = true
            } else if (stackIn.item is DyeItem && !isDye) {
                isDye = true
            }
        }
        return isIceCream && isCherry && isPotion && isDye
    }

    override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack {
        val result: ItemStack = RagiumItems.ICE_CREAM_SODA.toStack()
        var potionContents: PotionContents = PotionContents.EMPTY
        for (index: Int in (0 until input.size())) {
            val stackIn: ItemStack = input.getItem(index)
            val itemIn: Item = stackIn.item
            if (stackIn.isEmpty) continue
            if (stackIn.has(DataComponents.POTION_CONTENTS)) {
                val contentIn: PotionContents = stackIn.get(DataComponents.POTION_CONTENTS)!!
                if (potionContents == PotionContents.EMPTY) {
                    potionContents = contentIn
                }
            } else if (itemIn is DyeItem) {
                potionContents = PotionContents(
                    potionContents.potion,
                    Optional.of(itemIn.dyeColor.textureDiffuseColor),
                    potionContents.customEffects,
                )
            }
        }
        result.set(DataComponents.POTION_CONTENTS, potionContents)
        return result
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width >= 2 && height >= 2

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ICE_CREAM_SODA.get()
}
