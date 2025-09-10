package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, SingleItemRecipe>(
        HTMachineVariant.CUTTING_MACHINE,
        pos,
        state,
    ) {
    private lateinit var inputSlot: HTItemSlot
    private lateinit var catalystSlot: HTItemSlot
    private lateinit var outputSlots: List<HTItemSlot>

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // input
        inputSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        // catalyst
        catalystSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2))
        // outputs
        outputSlots = listOf(
            HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(0.5)),
            HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0.5)),
            HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(1.5)),
            HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1.5)),
        )
        return HTSimpleItemSlotHolder(this, listOf(inputSlot), outputSlots, catalystSlot)
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.CUTTING_MACHINE.openMenu(player, title, this, ::writeExtraContainerData)

    //    Ticking    //

    private val recipeCache = SingleItemCache()

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inputSlot.getStack())

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): SingleItemRecipe? =
        recipeCache.getFirstRecipe(input, level)

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: SingleItemRecipe): Boolean {
        var remainder: ItemStack = recipe.assemble(input, level.registryAccess())
        for (slot: HTItemSlot in outputSlots) {
            remainder = slot.insertItem(remainder, true, HTStorageAccess.INTERNAl)
            if (remainder.isEmpty) break
        }
        return remainder.isEmpty
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: SingleItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        var remainder: ItemStack = recipe.assemble(input, level.registryAccess())
        for (slot: HTItemSlot in outputSlots) {
            remainder = slot.insertItem(remainder, false, HTStorageAccess.INTERNAl)
            if (remainder.isEmpty) break
        }
        // インプットを減らす
        inputSlot.shrinkStack(1, false)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1f, 1f)
    }

    private inner class SingleItemCache : HTRecipeCache<SingleRecipeInput, SingleItemRecipe> {
        override fun getFirstHolder(input: SingleRecipeInput, level: Level): RecipeHolder<SingleItemRecipe>? =
            getFirstHolder(RagiumRecipeTypes.SAWMILL, input, level)
                ?: getFirstHolder(RagiumRecipeTypes.STONECUTTER, input, level)

        private fun getFirstHolder(
            recipeType: HTDeferredRecipeType<SingleRecipeInput, out SingleItemRecipe>,
            input: SingleRecipeInput,
            level: Level,
        ): RecipeHolder<SingleItemRecipe>? {
            // 指定されたアイテムと同じものを出力するレシピだけを選ぶ
            var matchedHolder: RecipeHolder<SingleItemRecipe>? = null
            for (holder: RecipeHolder<out SingleItemRecipe> in recipeType.getAllRecipes(level.recipeManager)) {
                val recipe: SingleItemRecipe = holder.value
                if (!recipe.matches(input, level)) continue
                val result: ItemStack = recipe.assemble(input, level.registryAccess())
                if (ItemStack.isSameItemSameComponents(catalystSlot.getStack(), result)) {
                    matchedHolder = RecipeHolder(holder.id, recipe)
                    break
                }
            }
            return matchedHolder
        }
    }
}
