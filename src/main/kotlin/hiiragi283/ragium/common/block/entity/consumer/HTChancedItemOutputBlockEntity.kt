package hiiragi283.ragium.common.block.entity.consumer

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.HTRecipeFinder
import hiiragi283.ragium.api.recipe.manager.createCache
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTChancedItemOutputBlockEntity<INPUT : RecipeInput, RECIPE : HTChancedItemRecipe<INPUT>>(
    blockHolder: Holder<Block>,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntity<INPUT, RECIPE>(blockHolder, pos, state) {
    lateinit var inputTank: HTFluidStackTank
        private set

    final override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        // input
        inputTank = builder.addSlot(HTAccessConfig.INPUT_ONLY, createTank(listener))
        return builder.build()
    }

    protected abstract fun createTank(listener: HTContentListener): HTFluidStackTank

    lateinit var inputSlot: HTItemStackSlot
        private set
    lateinit var outputSlots: List<HTItemStackSlot>
        private set

    final override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        // input
        inputSlot = builder.addSlot(
            HTAccessConfig.INPUT_ONLY,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0)),
        )
        // outputs
        outputSlots = intArrayOf(5, 6).flatMap { x: Int ->
            doubleArrayOf(0.5, 1.5).map { y: Double ->
                builder.addSlot(
                    HTAccessConfig.OUTPUT_ONLY,
                    HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(x), HTSlotHelper.getSlotPosY(y)),
                )
            }
        }
        return builder.build()
    }

    override fun canProgressRecipe(level: ServerLevel, input: INPUT, recipe: RECIPE): Boolean {
        // アウトプットに搬出できるか判定する
        for (stackIn: ImmutableItemStack in recipe.getPreviewItems(input, level.registryAccess())) {
            if (HTStackSlotHelper.insertStacks(outputSlots, stackIn, HTStorageAction.SIMULATE) != null) {
                return false
            }
        }
        return true
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: INPUT,
        recipe: RECIPE,
    ) {
        // 実際にアウトプットに搬出する
        for ((result: HTItemResult, chance: Float) in recipe.getResultItems(input)) {
            if (chance > level.random.nextFloat()) {
                val stackIn: ImmutableItemStack = result.getStackOrNull(level.registryAccess()) ?: continue
                HTStackSlotHelper.insertStacks(outputSlots, stackIn, HTStorageAction.EXECUTE)
            }
        }
    }

    //    Cached    //

    abstract class Cached<INPUT : RecipeInput, RECIPE : HTChancedItemRecipe<INPUT>>(
        private val cache: HTRecipeCache<INPUT, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : HTChancedItemOutputBlockEntity<INPUT, RECIPE>(blockHolder, pos, state) {
        constructor(
            finder: HTRecipeFinder<INPUT, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : this(finder.createCache(), blockHolder, pos, state)

        final override fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = cache.getFirstRecipe(input, level)
    }
}
