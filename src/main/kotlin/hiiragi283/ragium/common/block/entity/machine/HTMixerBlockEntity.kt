package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTViewRecipeInput
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.recipe.HTMixingRecipe
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

class HTMixerBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.MIXER, pos, state) {
    private lateinit var inputTanks: List<HTBasicFluidTank>
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTanks = listOf(
            builder.addSlot(
                HTSlotInfo.INPUT,
                HTVariableFluidTank.input(listener, getTankCapacity(RagiumFluidConfigType.FIRST_INPUT)),
            ),
            builder.addSlot(
                HTSlotInfo.INPUT,
                HTVariableFluidTank.input(listener, getTankCapacity(RagiumFluidConfigType.SECOND_INPUT)),
            ),
        )

        outputTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidTank.output(listener, getTankCapacity(RagiumFluidConfigType.FIRST_OUTPUT)),
        )
    }

    private lateinit var inputSlots: List<HTBasicItemSlot>
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlots = List(HTMixingRecipe.MAX_ITEM_INPUT) {
            builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        }

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun createRecipeComponent(): HTRecipeComponent<*, *> = RecipeComponent()

    private inner class RecipeComponent :
        HTEnergizedRecipeComponent.Cached<HTViewRecipeInput, HTMixingRecipe>(RagiumRecipeTypes.MIXING, this) {
        private val itemOutputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }
        private val fluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }

        override fun insertOutput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTViewRecipeInput,
            recipe: HTMixingRecipe,
        ) {
            val access: RegistryAccess = level.registryAccess()
            itemOutputHandler.insert(recipe.getResultItem(access))
            fluidOutputHandler.insert(recipe.getResultFluid(access))
        }

        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTViewRecipeInput,
            recipe: HTMixingRecipe,
        ) {
            val ingredients: Ior<List<HTItemIngredient>, List<HTFluidIngredient>> = recipe.ingredients
            ingredients.getLeft()?.let { HTShapelessRecipeHelper.shapelessConsume(it, inputSlots) }
            ingredients.getRight()?.let { HTShapelessRecipeHelper.shapelessConsume(it, inputTanks) }
        }

        override fun applyEffect() {
            playSound(SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_INSIDE)
        }

        override fun canProgressRecipe(level: ServerLevel, input: HTViewRecipeInput, recipe: HTMixingRecipe): Boolean {
            val access: RegistryAccess = level.registryAccess()
            val bool1: Boolean = itemOutputHandler.canInsert(recipe.getResultItem(access))
            val bool2: Boolean = fluidOutputHandler.canInsert(recipe.getResultFluid(access))
            return bool1 && bool2
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTViewRecipeInput? = HTViewRecipeInput.create {
            inputSlots.forEach(this::plusAssign)
            inputTanks.forEach(this::plusAssign)
        }
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.mixer
}
