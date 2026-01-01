package hiiragi283.ragium.common.block.entity.processing

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.component.HTProcessingRecipeComponent
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.config.RagiumFluidConfigType
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.RecipeBased(RagiumBlockEntityTypes.MELTER, pos, state) {
    lateinit var outputTank: HTBasicFluidTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // output
        outputTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidTank.output(listener, getTankCapacity(RagiumFluidConfigType.FIRST_OUTPUT)),
        )
    }

    lateinit var inputSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.create(listener))
    }

    private val inputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(inputSlot) }
    private val outputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }

    //    Processing    //

    override fun createRecipeComponent(): HTProcessingRecipeComponent.Cached<HTMeltingRecipe> =
        object : HTProcessingRecipeComponent.Cached<HTMeltingRecipe>(RagiumRecipeTypes.MELTING, this) {
            override fun insertOutput(
                level: ServerLevel,
                pos: BlockPos,
                input: HTRecipeInput,
                recipe: HTMeltingRecipe,
            ) {
                outputHandler.insert(recipe.getResultFluid(level.registryAccess()))
            }

            override fun extractInput(
                level: ServerLevel,
                pos: BlockPos,
                input: HTRecipeInput,
                recipe: HTMeltingRecipe,
            ) {
                inputHandler.consume(recipe.ingredient.getRequiredAmount())
            }

            override fun applyEffect() {
                playSound(SoundEvents.BUCKET_EMPTY_LAVA)
            }

            override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTRecipeInput? =
                HTRecipeInput.create(null) { items += inputHandler.getItemStack() }

            override fun canProgressRecipe(level: ServerLevel, input: HTRecipeInput, recipe: HTMeltingRecipe): Boolean =
                outputHandler.canInsert(recipe.getResultFluid(level.registryAccess()))
        }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.melter
}
