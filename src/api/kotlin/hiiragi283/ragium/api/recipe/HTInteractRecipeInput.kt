package hiiragi283.ragium.api.recipe

import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

data class HTInteractRecipeInput(val pos: BlockPos, val item: ItemStack) : RecipeInput {
    override fun getItem(index: Int): ItemStack = item

    override fun size(): Int = 1
}
