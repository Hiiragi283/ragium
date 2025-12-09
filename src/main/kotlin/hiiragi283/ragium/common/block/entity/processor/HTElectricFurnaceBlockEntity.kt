package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractSmelterBlockEntity
import hiiragi283.ragium.common.recipe.HTVanillaCookingRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTElectricFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTAbstractSmelterBlockEntity(RagiumBlocks.ELECTRIC_FURNACE, pos, state) {
    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): HTVanillaCookingRecipe? =
        getRecipeCache().getFirstRecipe(input, level)?.let(::HTVanillaCookingRecipe)
}
