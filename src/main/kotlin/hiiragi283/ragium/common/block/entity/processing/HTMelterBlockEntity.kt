package hiiragi283.ragium.common.block.entity.processing

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.gui.element.alineCenter
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.gui.slot.toSlot
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.util.HTModularUIHelper
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
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.MELTER, pos, state) {
    @DescSynced
    @Persisted(subPersisted = true)
    val outputTank: HTBasicFluidTank = HTVariableFluidTank.output(getTankCapacity(RagiumFluidConfigType.FIRST_OUTPUT))

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder) {
        builder.addSlot(HTSlotInfo.OUTPUT, outputTank)
    }

    @DescSynced
    @Persisted(subPersisted = true)
    val inputSlot: HTBasicItemSlot = HTBasicItemSlot.create()

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder) {
        builder.addSlot(HTSlotInfo.INPUT, inputSlot)
    }

    private val inputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(inputSlot) }
    private val outputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }

    override fun setupMainTab(root: UIElement) {
        root.addRowChild {
            alineCenter()
            addChild(inputSlot.toSlot())
            addChild(HTModularUIHelper.rightArrowIcon().layout { it.marginHorizontalPercent(10f) })
            addChild(createFluidSlot(0))
        }
        super.setupMainTab(root)
    }

    //    Processing    //

    override fun createRecipeComponent(): HTProcessingRecipeComponent.Cached<SingleRecipeInput, HTMeltingRecipe> =
        object : HTProcessingRecipeComponent.Cached<SingleRecipeInput, HTMeltingRecipe>(RagiumRecipeTypes.MELTING, this) {
            override fun insertOutput(
                level: ServerLevel,
                pos: BlockPos,
                input: SingleRecipeInput,
                recipe: HTMeltingRecipe,
            ) {
                outputHandler.insert(recipe.getResultFluid(level.registryAccess()))
            }

            override fun extractInput(
                level: ServerLevel,
                pos: BlockPos,
                input: SingleRecipeInput,
                recipe: HTMeltingRecipe,
            ) {
                inputHandler.consume(recipe.ingredient.getRequiredAmount())
            }

            override fun applyEffect() {
                playSound(SoundEvents.BUCKET_EMPTY_LAVA)
            }

            override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput =
                SingleRecipeInput(inputHandler.getItemStack())

            override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTMeltingRecipe): Boolean =
                outputHandler.canInsert(recipe.getResultFluid(level.registryAccess()))
        }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.melter
}
