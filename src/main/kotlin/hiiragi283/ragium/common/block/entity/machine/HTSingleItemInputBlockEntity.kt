package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTBlockEntityFactory
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.util.HTIngredientHelper
import hiiragi283.ragium.common.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTSingleItemInputBlockEntity<RECIPE : Recipe<SingleRecipeInput>> : HTProcessorBlockEntity.Cached<SingleRecipeInput, RECIPE> {
    companion object {
        @JvmStatic
        fun <RECIPE : HTSingleInputRecipe> createSimple(
            menuType: HTDeferredMenuType<*>,
            sound: SoundEvent,
            soundValues: Pair<Float, Float>,
            recipeType: HTDeferredRecipeType<SingleRecipeInput, RECIPE>,
            variant: HTMachineVariant,
        ): HTBlockEntityFactory<HTSingleItemInputBlockEntity<RECIPE>> = HTBlockEntityFactory { pos: BlockPos, state: BlockState ->
            Simple(menuType, sound, soundValues, recipeType.get(), variant, pos, state)
        }
    }

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

    protected lateinit var inputSlot: HTItemSlot.Mutable

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inputSlot.getStack())

    //    Simple    //

    private class Simple<RECIPE : HTSingleInputRecipe>(
        private val menuType: HTDeferredMenuType<*>,
        private val sound: SoundEvent,
        private val soundValues: Pair<Float, Float>,
        recipeType: RecipeType<RECIPE>,
        variant: HTMachineVariant,
        pos: BlockPos,
        state: BlockState,
    ) : HTSingleItemInputBlockEntity<RECIPE>(
            recipeType,
            variant,
            pos,
            state,
        ) {
        private lateinit var outputSlot: HTItemSlot

        override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
            // input
            inputSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
            // output
            outputSlot = HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
            return HTSimpleItemSlotHolder(this, listOf(inputSlot), listOf(outputSlot))
        }

        override fun openGui(player: Player, title: Component): InteractionResult =
            menuType.openMenu(player, title, this, this::writeExtraContainerData)

        override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: RECIPE): Boolean =
            outputSlot.insert(recipe.assemble(input, level.registryAccess()), HTStorageAction.SIMULATE, HTStorageAccess.INTERNAl).isEmpty

        override fun completeRecipe(
            level: ServerLevel,
            pos: BlockPos,
            state: BlockState,
            input: SingleRecipeInput,
            recipe: RECIPE,
        ) {
            // 実際にアウトプットに搬出する
            outputSlot.insert(recipe.assemble(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAl)
            // インプットを減らす
            HTIngredientHelper.shrinkStack(inputSlot, recipe::getRequiredCount, HTStorageAction.EXECUTE)
            // SEを鳴らす
            level.playSound(null, pos, sound, SoundSource.BLOCKS, soundValues.first, soundValues.second)
        }
    }
}
