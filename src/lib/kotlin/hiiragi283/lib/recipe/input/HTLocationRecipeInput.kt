package hiiragi283.lib.recipe.input

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmRecord
data class HTLocationRecipeInput(val level: ServerLevel, val pos: BlockPos) : RecipeInput {
    override fun getItem(index: Int): ItemStack = error("No item for index: $index")

    override fun size(): Int = 0

    override fun isEmpty(): Boolean = false
}
