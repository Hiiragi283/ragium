package hiiragi283.ragium.common.block.entity.processing

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
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

class HTSolidifierBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleCatalystBlockEntity(RagiumBlockEntityTypes.SOLIDIFIER, pos, state) {
    @DescSynced
    @Persisted(subPersisted = true)
    val inputTank: HTBasicFluidTank = HTVariableFluidTank.input(getTankCapacity(RagiumFluidConfigType.FIRST_INPUT))

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder) {
        builder.addSlot(HTSlotInfo.INPUT, inputTank)
    }

    override fun createInputSlot(): UIElement = createFluidSlot(0)

    //    Processing    //

    override fun createRecipeComponent(): HTRecipeComponent<*, *> =
        object : RecipeComponent<HTItemAndFluidRecipeInput, HTSolidifyingRecipe>(RagiumRecipeTypes.SOLIDIFYING) {
            private val inputHandler: HTSlotInputHandler<HTFluidResourceType> by lazy { HTSlotInputHandler(inputTank) }

            override fun extractInput(
                level: ServerLevel,
                pos: BlockPos,
                input: HTItemAndFluidRecipeInput,
                recipe: HTSolidifyingRecipe,
            ) {
                inputHandler.consume(recipe.ingredient)
            }

            override fun applyEffect() {
                playSound(SoundEvents.FIRE_EXTINGUISH)
            }

            override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTItemAndFluidRecipeInput =
                HTItemAndFluidRecipeInput(catalystHandler.getItemStack(), inputHandler.getFluidStack())
        }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.solidifier
}
