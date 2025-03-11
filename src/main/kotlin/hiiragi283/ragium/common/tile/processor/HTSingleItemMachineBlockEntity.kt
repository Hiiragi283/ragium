package hiiragi283.ragium.common.tile.processor

import hiiragi283.ragium.api.recipe.base.HTMachineRecipeCache
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeContext
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

abstract class HTSingleItemMachineBlockEntity(
    type: Supplier<out BlockEntityType<*>>,
    pos: BlockPos,
    state: BlockState,
    protected val recipeType: HTRecipeType<out HTSingleItemRecipe>,
    baseTickRate: Int = 200,
) : HTSimpleMachineBlockEntity(type, pos, state, baseTickRate) {
    private val recipeCache: HTMachineRecipeCache<out HTSingleItemRecipe> = HTMachineRecipeCache(recipeType)

    final override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val context: HTMachineRecipeContext = HTMachineRecipeContext.Companion
            .builder()
            .addInput(0, inputSlot)
            .addCatalyst(catalystSlot)
            .addOutput(0, outputSlot)
            .build()
        recipeCache.processFirstRecipe(context, level)
    }
}
