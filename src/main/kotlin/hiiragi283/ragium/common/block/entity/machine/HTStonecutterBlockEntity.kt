package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.mixin.SingleItemRecipeAccessor
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTStonecutterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.AUTO_CHISEL, pos, state) {
    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var catalystSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))

        catalystSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.input(listener))

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        widgetHolder += HTEnergySlotWidget(battery, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4))
        // slots
        widgetHolder += HTItemSlotWidget.container(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder += HTItemSlotWidget.container(
            catalystSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.NONE,
        )

        widgetHolder += HTItemSlotWidget.container(
            outputSlot,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
    }

    //    Serialize    //

    private val cache: HTRecipeCache<HTDoubleRecipeInput, WrappedRecipe> = object :
        HTRecipeCache<HTDoubleRecipeInput, WrappedRecipe>,
        HTValueSerializable.Empty {
        private var lastRecipe: HTRecipeHolder<WrappedRecipe>? = null

        override fun getFirstRecipe(input: HTDoubleRecipeInput, level: Level): WrappedRecipe? {
            var holder: HTRecipeHolder<WrappedRecipe>? = null
            if (lastRecipe != null) {
                if (lastRecipe!!.recipe.test(input)) {
                    holder = lastRecipe
                } else {
                    lastRecipe = null
                    return null
                }
            } else {
                holder = level.recipeManager
                    .getAllRecipesFor(RecipeType.STONECUTTING)
                    .map(HTRecipeHolder.Companion::from)
                    .map { holder: HTRecipeHolder<StonecutterRecipe> -> holder.mapRecipe { WrappedRecipe(it as SingleItemRecipeAccessor) } }
                    .firstOrNull { (_, recipe: WrappedRecipe) -> recipe.test(input) }
            }
            lastRecipe = holder
            return holder?.recipe
        }
    }

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        cache.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        cache.deserialize(input)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTDoubleRecipeInput, WrappedRecipe>() {
        private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val catalystHandler: HTItemInputHandler by lazy { HTItemInputHandler(catalystSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun createInput(level: ServerLevel, pos: BlockPos): HTDoubleRecipeInput? = createInput(inputHandler, catalystHandler)

        override fun findRecipe(level: ServerLevel, pos: BlockPos, input: HTDoubleRecipeInput): WrappedRecipe? =
            cache.getFirstRecipe(input, level)

        override fun canComplete(level: ServerLevel, pos: BlockPos, recipe: HTHandledRecipe<HTDoubleRecipeInput, WrappedRecipe>): Boolean =
            recipe.assemble(true).let(outputHandler::canInsert)

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTHandledRecipe<HTDoubleRecipeInput, WrappedRecipe>) {
            // output
            recipe.assemble(false).let(outputHandler::insert)
            // input
            inputHandler.consume(1)
            // sound
            playSound(SoundEvents.UI_STONECUTTER_TAKE_RESULT)
        }
    }

    override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    private class WrappedRecipe(private val accessor: SingleItemRecipeAccessor) : HTProcessingRecipe<HTDoubleRecipeInput> {
        override val time: Int = 5

        override fun test(input: HTDoubleRecipeInput): Boolean {
            val (first: ItemStack, second: ItemStack) = input
            return accessor.ingredient.test(first) && ItemStack.isSameItemSameComponents(accessor.result, second)
        }

        override fun assemble(input: HTDoubleRecipeInput, preview: Boolean): ItemStack = accessor.result.copy()
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.autoChisel
}
