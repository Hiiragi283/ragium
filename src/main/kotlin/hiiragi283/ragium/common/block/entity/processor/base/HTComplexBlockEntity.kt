package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.ragium.api.block.attribute.getFluidAttribute
import hiiragi283.ragium.api.recipe.HTFluidRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.HTProcessorBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTComplexBlockEntity<INPUT : RecipeInput, RECIPE : HTFluidRecipe<INPUT>> : HTProcessorBlockEntity.Cached<INPUT, RECIPE> {
    constructor(
        recipeCache: HTRecipeCache<INPUT, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : super(recipeCache, blockHolder, pos, state)

    constructor(
        finder: HTRecipeFinder<INPUT, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : super(finder, blockHolder, pos, state)

    lateinit var outputTank: HTBasicFluidTank
        private set

    final override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // input
        initInputTanks(builder, listener)
        // output
        outputTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidTank.output(listener, blockHolder.getFluidAttribute().getOutputTank()),
        )
    }

    protected open fun initInputTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {}

    lateinit var outputSlot: HTBasicItemSlot
        protected set

    final override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0

    final override fun canProgressRecipe(level: ServerLevel, input: INPUT, recipe: RECIPE): Boolean {
        val bool1: Boolean = HTStackSlotHelper.canInsertStack(outputSlot, input, level, recipe::assembleItem)
        val bool2: Boolean = HTStackSlotHelper.canInsertStack(outputTank, input, level, recipe::assembleFluid)
        return bool1 && bool2
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: INPUT,
        recipe: RECIPE,
    ) {
        // 実際にアウトプットに搬出する
        val access: RegistryAccess = level.registryAccess()
        outputSlot.insert(recipe.assembleItem(input, access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        outputTank.insert(recipe.assembleFluid(input, access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
    }
}
