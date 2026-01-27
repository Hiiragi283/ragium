package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
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
    private lateinit var inputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        super.createItemSlots(builder, listener)
    }

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
