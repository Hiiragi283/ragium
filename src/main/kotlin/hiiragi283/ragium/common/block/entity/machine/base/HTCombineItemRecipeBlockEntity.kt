package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.block.entity.HTSoundPlayerBlockEntity
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
import hiiragi283.ragium.common.recipe.base.HTCombineItemRecipe
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

abstract class HTCombineItemRecipeBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    protected lateinit var inputSlots: List<HTBasicItemSlot>
        private set
    protected lateinit var outputSlot: HTBasicItemSlot
        private set

    final override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlots = List(3) { HTBasicItemSlot.input(listener) }

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    //    Processing    //

    inner class CombineRecipeComponent<RECIPE : HTCombineItemRecipe>(
        finder: HTRecipeFinder<HTShapelessRecipeInput, RECIPE>,
        private val soundAction: (HTSoundPlayerBlockEntity) -> Unit,
    ) : HTEnergizedRecipeComponent.Cached<HTShapelessRecipeInput, RECIPE>(finder, this) {
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun insertOutput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTShapelessRecipeInput,
            recipe: RECIPE,
        ) {
            outputHandler.insert(recipe.assemble(input, level.registryAccess()))
        }

        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTShapelessRecipeInput,
            recipe: RECIPE,
        ) {
            HTShapelessRecipeHelper.shapelessConsume(recipe.ingredients, inputSlots)
        }

        override fun applyEffect() {
            soundAction(this@HTCombineItemRecipeBlockEntity)
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTShapelessRecipeInput? {
            val map: Map<HTItemResourceType, Int> = HTShapelessRecipeHelper.createMap(inputSlots)
            if (map.isEmpty()) return null
            return HTShapelessRecipeInput(map)
        }

        override fun canProgressRecipe(level: ServerLevel, input: HTShapelessRecipeInput, recipe: RECIPE): Boolean =
            outputHandler.canInsert(recipe.assemble(input, level.registryAccess()))
    }
}
