package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

abstract class HTItemToItemBlockEntity(
    recipeType: RecipeType<HTItemToItemRecipe>,
    variant: HTMachineVariant,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntity<SingleRecipeInput, HTItemToItemRecipe>(recipeType, variant, pos, state) {
    final override val inventory: HTItemHandler = HTItemStackHandler
        .Builder(2)
        .addInput(0)
        .addOutput(1)
        .build(this)

    //    Ticking    //

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput =
        SingleRecipeInput(inventory.getStackInSlot(0))

    // アウトプットに搬出できるか判定する
    final override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToItemRecipe): Boolean =
        insertToOutput(recipe.assemble(input, level.registryAccess()), true).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(recipe.assemble(input, level.registryAccess()), false)
        // インプットを減らす
        inventory.shrinkStack(0, recipe.ingredient, false)
    }

    //    Slot    //

    final override fun addInputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
    }

    final override fun addOutputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 1, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
    }
}
