package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.HTItemStackSlot
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.block.state.BlockState

class HTEngraverBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity<StonecutterRecipe>(
        RecipeType.STONECUTTING,
        HTMachineVariant.ENGRAVER,
        pos,
        state,
    ) {
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
        RagiumMenuTypes.ENGRAVER.openMenu(player, title, this, ::writeExtraContainerData)

    override fun createSound(random: RandomSource, pos: BlockPos): SoundInstance =
        createSound(SoundEvents.UI_STONECUTTER_TAKE_RESULT, random, pos)

    //    Ticking    //

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: StonecutterRecipe): Boolean {
        var remainder: ItemStack = recipe.assemble(input, level.registryAccess())
        for (slot: HTItemSlot in outputSlots) {
            remainder = slot.insertItem(remainder, true, HTStorageAccess.INTERNAl)
            if (remainder.isEmpty) break
        }
        return remainder.isEmpty
    }

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): StonecutterRecipe? {
        val allRecipes: List<RecipeHolder<StonecutterRecipe>> = level.recipeManager.getAllRecipesFor(RecipeType.STONECUTTING)
        if (allRecipes.isEmpty()) return null
        // 指定されたアイテムと同じものを出力するレシピだけを選ぶ
        var matchedRecipe: StonecutterRecipe? = null
        for (holder: RecipeHolder<StonecutterRecipe> in allRecipes) {
            val recipe: StonecutterRecipe = holder.value
            if (!recipe.matches(input, level)) continue
            val result: ItemStack = recipe.assemble(input, level.registryAccess())
            if (ItemStack.isSameItemSameComponents(catalystSlot.getStack(), result)) {
                matchedRecipe = recipe
                break
            }
        }
        return matchedRecipe
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: StonecutterRecipe,
    ) {
        // 実際にアウトプットに搬出する
        var remainder: ItemStack = recipe.assemble(input, level.registryAccess())
        for (slot: HTItemSlot in outputSlots) {
            remainder = slot.insertItem(remainder, false, HTStorageAccess.INTERNAl)
            if (remainder.isEmpty) break
        }
        // インプットを減らす
        inputSlot.shrinkStack(1, false)
    }
}
