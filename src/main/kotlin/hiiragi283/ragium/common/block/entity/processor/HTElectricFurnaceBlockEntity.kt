package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractSmelterBlockEntity
import hiiragi283.ragium.common.recipe.HTVanillaCookingRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTElectricFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTAbstractSmelterBlockEntity(RagiumBlocks.ELECTRIC_FURNACE, pos, state) {
    override fun getMatchedRecipe(input: HTRecipeInput, level: ServerLevel): HTVanillaCookingRecipe? {
        val singleInput: SingleRecipeInput = input.toSingleItem() ?: return null
        return getRecipeCache().getFirstRecipe(singleInput, level)?.let(::HTVanillaCookingRecipe)
    }
}
