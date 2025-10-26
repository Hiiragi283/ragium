package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.castRecipe
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.getItemStack
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, SingleItemRecipe>(
        RagiumBlocks.CUTTING_MACHINE,
        pos,
        state,
    ) {
    private lateinit var inputSlot: HTItemSlot.Mutable
    private lateinit var catalystSlot: HTItemSlot
    private lateinit var outputSlots: List<HTItemSlot>

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        // input
        inputSlot = builder.addSlot(
            HTAccessConfig.INPUT_ONLY,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0)),
        )
        // catalyst
        catalystSlot = builder.addSlot(
            HTAccessConfig.NONE,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)),
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

    //    Ticking    //

    private val recipeCache = SingleItemCache()

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = inputSlot.toRecipeInput()

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): SingleItemRecipe? =
        recipeCache.getFirstRecipe(input, level)

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: SingleItemRecipe): Boolean = HTStackSlotHelper
        .insertStacks(
            outputSlots,
            recipe.assemble(input, level.registryAccess()).toImmutable(),
            HTStorageAction.SIMULATE,
        ).isEmpty()

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: SingleItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        HTStackSlotHelper.insertStacks(
            outputSlots,
            recipe.assemble(input, level.registryAccess()).toImmutable(),
            HTStorageAction.EXECUTE,
        )
        // インプットを減らす
        inputSlot.shrinkStack(1, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1f, 1f)
    }

    private inner class SingleItemCache : HTRecipeCache<SingleRecipeInput, SingleItemRecipe> {
        override fun getFirstHolder(input: SingleRecipeInput, level: Level): RecipeHolder<SingleItemRecipe>? {
            // 指定されたアイテムと同じものを出力するレシピだけを選ぶ
            var matchedHolder: RecipeHolder<SingleItemRecipe>? = null
            for (holder: RecipeHolder<out SingleItemRecipe> in RagiumRecipeTypes.CUTTING.getAllHolders(level.recipeManager)) {
                val recipe: SingleItemRecipe = holder.value
                if (!recipe.matches(input, level)) continue
                val result: ItemStack = recipe.assemble(input, level.registryAccess())
                if (ItemStack.isSameItemSameComponents(catalystSlot.getItemStack(), result)) {
                    matchedHolder = holder.castRecipe()
                    break
                }
            }
            return matchedHolder
        }
    }
}
