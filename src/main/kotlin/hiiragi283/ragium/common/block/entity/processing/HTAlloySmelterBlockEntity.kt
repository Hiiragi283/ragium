package hiiragi283.ragium.common.block.entity.processing

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.input.HTListItemRecipeInput
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.component.HTProcessingRecipeComponent
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
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

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.ALLOY_SMELTER, pos, state) {
    lateinit var topInputSlot: HTBasicItemSlot
        private set
    lateinit var leftInputSlot: HTBasicItemSlot
        private set
    lateinit var rightInputSlot: HTBasicItemSlot
        private set
    lateinit var outputSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        topInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(listener, filter = { resource: HTItemResourceType ->
                resource != leftInputSlot.getResource() &&
                    resource != rightInputSlot.getResource()
            }),
        )
        leftInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(listener, filter = { resource: HTItemResourceType ->
                resource != rightInputSlot.getResource() &&
                    resource != topInputSlot.getResource()
            }),
        )
        rightInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(listener, filter = { resource: HTItemResourceType ->
                resource != topInputSlot.getResource() &&
                    resource != leftInputSlot.getResource()
            }),
        )

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    private val inputHandlers: List<HTSlotInputHandler<HTItemResourceType>> by lazy {
        listOf(topInputSlot, leftInputSlot, rightInputSlot).map(::HTSlotInputHandler)
    }
    private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

    //    Processing    //

    override fun createRecipeComponent(): HTProcessingRecipeComponent.Cached<HTListItemRecipeInput, HTAlloyingRecipe> =
        object : HTProcessingRecipeComponent.Cached<HTListItemRecipeInput, HTAlloyingRecipe>(
            RagiumRecipeTypes.ALLOYING,
            this,
        ) {
            override fun insertOutput(
                level: ServerLevel,
                pos: BlockPos,
                input: HTListItemRecipeInput,
                recipe: HTAlloyingRecipe,
            ) {
                outputHandler.insert(recipe.getResultItem(level.registryAccess()))
            }

            override fun extractInput(
                level: ServerLevel,
                pos: BlockPos,
                input: HTListItemRecipeInput,
                recipe: HTAlloyingRecipe,
            ) {
                inputHandlers[0].consume(recipe.firstIngredient)
                inputHandlers[1].consume(recipe.secondIngredient)
                inputHandlers[2].consume(recipe.thirdIngredient)
            }

            override fun applyEffect() {
                playSound(SoundEvents.FIRE_EXTINGUISH)
            }

            override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTListItemRecipeInput =
                HTListItemRecipeInput(inputHandlers.map { it.getItemStack() })

            override fun canProgressRecipe(level: ServerLevel, input: HTListItemRecipeInput, recipe: HTAlloyingRecipe): Boolean =
                outputHandler.canInsert(recipe.getResultItem(level.registryAccess()))
        }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.alloySmelter
}
