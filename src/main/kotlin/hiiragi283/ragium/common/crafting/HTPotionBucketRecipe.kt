package hiiragi283.ragium.common.crafting

import hiiragi283.core.api.item.alchemy.HTPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.common.crafting.HTCustomRecipe
import hiiragi283.core.common.crafting.ImmutableRecipeInput
import hiiragi283.core.util.HCPotionFluidHelper
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.Tags

class HTPotionBucketRecipe(category: CraftingBookCategory) : HTCustomRecipe(category) {
    override fun matches(input: ImmutableRecipeInput, level: Level): Boolean {
        var drops = 0
        var waterBuckets = 0
        for (stack: ItemStack in input) {
            if (stack.isEmpty) continue
            if (stack.`is`(RagiumItems.POTION_DROP)) {
                drops++
            } else if (stack.`is`(Tags.Items.BUCKETS_WATER)) {
                waterBuckets++
            }
        }
        return drops == 1 && waterBuckets == 1
    }

    override fun assemble(input: ImmutableRecipeInput, registries: HolderLookup.Provider): ItemStack {
        var contents: HTPotionContents? = null
        for (stack: ItemStack in input) {
            if (stack.isEmpty) continue
            contents = HTPotionHelper.getContents(stack) ?: continue
            break
        }
        return when {
            contents == null -> ItemStack.EMPTY
            else -> HCPotionFluidHelper.createBucket(contents)
        }
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 5

    override fun getRemainingItems(input: CraftingInput): NonNullList<ItemStack> {
        val list: NonNullList<ItemStack> = NonNullList.withSize(input.size(), ItemStack.EMPTY)
        for (i: Int in list.indices) {
            val itemIn: ItemStack = input.getItem(i)
            if (itemIn.hasCraftingRemainingItem() && !itemIn.`is`(Tags.Items.BUCKETS_WATER)) {
                list[i] = itemIn.craftingRemainingItem
            }
        }
        return list
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.POTION_BUCKET
}
