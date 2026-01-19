package hiiragi283.ragium.common.block.entity.processing

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.result.HTFluidResultCreator
import hiiragi283.core.api.gui.element.HTItemSlotElement
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.api.times
import hiiragi283.core.api.toFraction
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.config.RagiumFluidConfigType
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.ComposterBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.crafting.SizedIngredient
import org.apache.commons.lang3.math.Fraction

class HTFermenterBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity(RagiumBlockEntityTypes.FERMENTER, pos, state) {
    companion object {
        @JvmStatic
        fun createRecipe(item: ItemLike, chance: Float): HTMeltingRecipe? {
            if (chance <= 0f) return null
            val amount: Int = (HTConst.DEFAULT_FLUID_AMOUNT * chance.toFraction()).toInt()
            return HTMeltingRecipe(
                HTItemIngredient(SizedIngredient.of(item, 1)),
                HTFluidResultCreator.create(RagiumFluids.CRUDE_BIO, amount),
                20 * 5,
                Fraction.ZERO,
            )
        }
    }

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

    override fun setupMainTab(root: UIElement) {
        RagiumModularUIHelper.melter(root, HTItemSlotElement(inputSlot), createFluidSlot(0))
        super.setupMainTab(root)
    }

    //    Processing    //

    override fun createRecipeComponent(): HTRecipeComponent<*, *> = object : HTRecipeComponent<SingleRecipeInput, HTMeltingRecipe>() {
        private val inputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(inputSlot) }
        private val outputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }

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
            playSound(SoundEvents.COMPOSTER_FILL_SUCCESS)
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput =
            SingleRecipeInput(inputHandler.getItemStack())

        override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): HTMeltingRecipe? {
            val stack: ItemStack = input.item()
            val chance: Float = ComposterBlock.getValue(stack)
            return createRecipe(stack.item, chance)
        }

        override fun getMaxProgress(recipe: HTMeltingRecipe): Int = recipe.time

        override fun getProgress(level: ServerLevel, pos: BlockPos): Int = 1

        override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTMeltingRecipe): Boolean =
            outputHandler.canInsert(recipe.getResultFluid(level.registryAccess()))
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.device.fermenter
}
