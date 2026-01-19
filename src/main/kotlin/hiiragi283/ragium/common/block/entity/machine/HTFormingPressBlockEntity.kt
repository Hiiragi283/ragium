package hiiragi283.ragium.common.block.entity.machine

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.gui.element.HTItemSlotElement
import hiiragi283.core.api.recipe.input.HTListItemRecipeInput
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTFormingPressBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleCatalystBlockEntity(RagiumBlockEntityTypes.FORMING_PRESS, pos, state) {
    @DescSynced
    @Persisted(subPersisted = true)
    val inputSlot: HTBasicItemSlot = HTBasicItemSlot.input()

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder) {
        builder.addSlot(HTSlotInfo.INPUT, inputSlot)

        super.createItemSlots(builder)
    }

    override fun createInputSlot(): UIElement = HTItemSlotElement(inputSlot)

    //    Processing    //

    override fun createRecipeComponent(): HTRecipeComponent<*, *> =
        object : RecipeComponent<HTListItemRecipeInput, HTPressingRecipe>(RagiumRecipeTypes.PRESSING) {
            private val inputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(inputSlot) }

            override fun extractInput(
                level: ServerLevel,
                pos: BlockPos,
                input: HTListItemRecipeInput,
                recipe: HTPressingRecipe,
            ) {
                inputHandler.consume(recipe.ingredient)
            }

            override fun applyEffect() {
                playSound(SoundEvents.ANVIL_LAND)
            }

            override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTListItemRecipeInput =
                HTListItemRecipeInput(listOf(inputHandler.getItemStack(), catalystHandler.getItemStack()))
        }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.formingPress
}
