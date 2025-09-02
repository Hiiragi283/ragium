package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTSingleItemInputBlockEntity<RECIPE : Recipe<SingleRecipeInput>> : HTProcessorBlockEntity<SingleRecipeInput, RECIPE> {
    constructor(
        recipeCache: HTRecipeCache<SingleRecipeInput, RECIPE>,
        variant: HTMachineVariant,
        pos: BlockPos,
        state: BlockState,
    ) : super(recipeCache, variant, pos, state)

    constructor(
        recipeType: RecipeType<RECIPE>,
        variant: HTMachineVariant,
        pos: BlockPos,
        state: BlockState,
    ) : super(recipeType, variant, pos, state)

    protected lateinit var inputSlot: HTItemSlot

    //    Ticking    //

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inputSlot.getStack())
}
