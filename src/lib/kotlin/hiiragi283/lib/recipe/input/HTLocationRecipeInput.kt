package hiiragi283.lib.recipe.input

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.state.pattern.BlockInWorld

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmRecord
data class HTLocationRecipeInput(val blockInWorld: BlockInWorld) : RecipeInput {
    constructor(level: ServerLevel, pos: BlockPos) : this(BlockInWorld(level, pos, false))

    override fun getItem(index: Int): ItemStack = error("No item for index: $index")

    override fun size(): Int = 0
}
