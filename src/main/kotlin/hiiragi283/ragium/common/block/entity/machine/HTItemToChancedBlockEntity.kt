package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.api.recipe.HTViewRecipeInput
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.recipe.base.HTItemToChancedRecipe
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemToChancedBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTChancedBlockEntity(type, pos, state) {
    private lateinit var inputSlot: HTBasicItemSlot

    override fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    //    Processing    //

    inner class RecipeComponent<RECIPE : HTItemToChancedRecipe>(
        finder: HTRecipeFinder<HTViewRecipeInput, RECIPE>,
        private val sound: SoundEvent,
    ) : HTChancedBlockEntity.RecipeComponent<RECIPE>(finder, this) {
        private val inputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(inputSlot) }

        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTViewRecipeInput,
            recipe: RECIPE,
        ) {
            inputHandler.consume(recipe.ingredient.getRequiredAmount())
        }

        override fun applyEffect() {
            playSound(sound)
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTViewRecipeInput? = HTViewRecipeInput.create {
            items +=
                inputHandler
        }
    }
}
