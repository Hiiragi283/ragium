package hiiragi283.ragium.common.block.entity.processing

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.component.HTProcessingRecipeComponent
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
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
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTPyrolyzerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.RecipeBased(RagiumBlockEntityTypes.PYROLYZER, pos, state) {
    lateinit var outputTank: HTBasicFluidTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        outputTank =
            builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(listener, getTankCapacity(RagiumFluidConfigType.FIRST_OUTPUT)))
    }

    lateinit var inputSlot: HTBasicItemSlot
        private set
    lateinit var outputSlots: List<HTBasicItemSlot>
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        outputSlots = List(4) { builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener)) }
    }

    private val inputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(inputSlot) }
    private val itemOutputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.multiple(outputSlots) }
    private val fluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }

    //    Processing    //

    override fun createRecipeComponent(): HTProcessingRecipeComponent.Cached<HTPyrolyzingRecipe> =
        object : HTProcessingRecipeComponent.Cached<HTPyrolyzingRecipe>(RagiumRecipeTypes.PYROLYZING, this) {
            override fun insertOutput(
                level: ServerLevel,
                pos: BlockPos,
                input: HTRecipeInput,
                recipe: HTPyrolyzingRecipe,
            ) {
                val access: RegistryAccess = level.registryAccess()
                itemOutputHandler.insert(recipe.getResultItem(access))
                fluidOutputHandler.insert(recipe.getResultFluid(access))
            }

            override fun extractInput(
                level: ServerLevel,
                pos: BlockPos,
                input: HTRecipeInput,
                recipe: HTPyrolyzingRecipe,
            ) {
                inputHandler.consume(recipe.ingredient.getRequiredAmount())
            }

            override fun applyEffect() {
                playSound(SoundEvents.BLAZE_AMBIENT, volume = 0.5f)
            }

            override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTRecipeInput? =
                HTRecipeInput.create(null) { items += inputHandler.getItemStack() }

            override fun canProgressRecipe(level: ServerLevel, input: HTRecipeInput, recipe: HTPyrolyzingRecipe): Boolean {
                val access: RegistryAccess = level.registryAccess()
                val bool1: Boolean = itemOutputHandler.canInsert(recipe.getResultItem(access))
                val bool2: Boolean = fluidOutputHandler.canInsert(recipe.getResultFluid(access))
                return bool1 && bool2
            }
        }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.pyrolyzer
}
