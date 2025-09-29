package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTSingleItemInputBlockEntity<RECIPE : Recipe<SingleRecipeInput>> : HTProcessorBlockEntity.Cached<SingleRecipeInput, RECIPE> {
    /*constructor(
        recipeCache: HTRecipeCache<SingleRecipeInput, RECIPE>,
        variant: HTMachineVariant,
        pos: BlockPos,
        state: BlockState,
    ) : super(recipeCache, variant, pos, state)*/

    constructor(
        recipeType: RecipeType<RECIPE>,
        variant: HTMachineVariant,
        pos: BlockPos,
        state: BlockState,
    ) : super(recipeType, variant, pos, state)

    protected lateinit var inputSlot: HTItemSlot

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inputSlot.getStack())

    abstract class ToItem<RECIPE : Recipe<SingleRecipeInput>> : HTSingleItemInputBlockEntity<RECIPE> {
        /*constructor(
            recipeCache: HTRecipeCache<SingleRecipeInput, RECIPE>,
            variant: HTMachineVariant,
            pos: BlockPos,
            state: BlockState,
        ) : super(recipeCache, variant, pos, state)*/

        constructor(
            recipeType: RecipeType<RECIPE>,
            variant: HTMachineVariant,
            pos: BlockPos,
            state: BlockState,
        ) : super(recipeType, variant, pos, state)

        protected lateinit var outputSlot: HTItemSlot
            private set

        final override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
            // input
            inputSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
            // output
            outputSlot = HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
            return HTSimpleItemSlotHolder(this, listOf(inputSlot), listOf(outputSlot))
        }

        final override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: RECIPE): Boolean =
            outputSlot.insertItem(recipe.assemble(input, level.registryAccess()), true, HTStorageAccess.INTERNAl).isEmpty

        override fun completeRecipe(
            level: ServerLevel,
            pos: BlockPos,
            state: BlockState,
            input: SingleRecipeInput,
            recipe: RECIPE,
        ) {
            // 実際にアウトプットに搬出する
            outputSlot.insertItem(recipe.assemble(input, level.registryAccess()), false, HTStorageAccess.INTERNAl)
        }
    }
}
