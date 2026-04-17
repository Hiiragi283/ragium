package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTAssemblerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.ASSEMBLER, pos, state) {
    private lateinit var leftInputSlot: HTBasicItemSlot
    private lateinit var rightInputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        leftInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(
                listener,
                filter = { resource: HTItemResourceType -> rightInputSlot.getResource() != resource },
            ),
        )
        rightInputSlot = builder.addSlot(
            HTSlotInfo.EXTRA_INPUT,
            HTBasicItemSlot.input(
                listener,
                filter = { resource: HTItemResourceType -> leftInputSlot.getResource() != resource },
            ),
        )

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    //    Serialize    //

    private lateinit var cache: HTRecipeCache<HTDoubleRecipeInput, HTAssemblingRecipe>

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        cache.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        cache.deserialize(input)
    }

    //    Processing    //

    private val leftInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(leftInputSlot) }
    private val rightInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(rightInputSlot) }
    private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

    override fun initRecipeCache() {
        cache = HTLookupRecipeCache.forRecipe(RagiumRecipeLookups.ASSEMBLING)
    }

    override fun createHandler(): HTRecipeHandler<*, *> = createHandler(
        { _, _ -> createInput(leftInputHandler, rightInputHandler) },
        cache,
        {
            canComplete = { level: ServerLevel, _, recipe: HTHandledRecipe<HTDoubleRecipeInput, HTAssemblingRecipe> ->
                recipe.assemble(level.registryAccess()).let(outputHandler::canInsert)
            }
            onComplete = { level: ServerLevel, _, recipe: HTHandledRecipe<HTDoubleRecipeInput, HTAssemblingRecipe> ->
                // output
                recipe.assemble(level.registryAccess()).let(outputHandler::insert)
                // input
                val recipe: HTAssemblingRecipe = recipe.recipe
                leftInputHandler.consume(recipe.itemIngredients[0])
                rightInputHandler.consume(recipe.itemIngredients[1])
                // sound
                playSound(SoundEvents.CRAFTER_CRAFT)
            }
        },
    )

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.assembler
}
