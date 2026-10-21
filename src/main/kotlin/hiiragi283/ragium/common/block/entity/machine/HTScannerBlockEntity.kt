package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.item.toTemplateOrNull
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.handler.HTDoubleRecipeHandler
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.data.HTMemoryDiscData
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction
import java.util.function.Predicate

class HTScannerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.SCANNER.get(), pos, state) {

    override fun createRecipeHandler(): HTRecipeHandler =
        object : HTDoubleRecipeHandler<ItemInstance, ItemInstance, ItemStack, FakeRecipe>() {
            private val inputSlot: HTInputSlot<ItemInstance> by lazy {
                HTInputHandlerHelper.forItem(this@HTScannerBlockEntity.inputSlot)
            }
            private val discSlot: HTInputSlot<ItemInstance> by lazy {
                HTInputHandlerHelper.forItem(this@HTScannerBlockEntity.discSlot)
            }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItem(this@HTScannerBlockEntity.outputSlot)
            }

            override fun createProgressor(): Progressor<ItemInstance, ItemInstance, FakeRecipe> = DoubleProgressor()

            override fun getFirstInput(): ItemInstance = inputSlot.getStoredInput()

            override fun getSecondInput(): ItemInstance = discSlot.getStoredInput()

            override fun findRecipe(first: ItemInstance, second: ItemInstance, level: ServerLevel): FakeRecipe? = when {
                second.has(RagiumDataComponents.MEMORY_DISC_DATA) -> null
                else -> FakeRecipe
            }

            override fun canComplete(
                recipe: FakeRecipe,
                first: ItemInstance,
                second: ItemInstance,
                output: ItemStack
            ): Boolean = useTransaction { transaction: Transaction ->
                val (target: ItemInstance, disc: ItemInstance) = recipe.getMatchingStack(first, second)
                when {
                    inputSlot.use(target, transaction).failed -> false
                    discSlot.use(disc, transaction).failed -> false
                    else -> outputSlot.take(output, transaction) == HTOutputSlot.TakeResult.FULL
                }
            }

            override fun onComplete(recipe: FakeRecipe, first: ItemInstance, second: ItemInstance, output: ItemStack) {
                useTransaction { transaction: Transaction ->
                    val (target: ItemInstance, disc: ItemInstance) = recipe.getMatchingStack(first, second)
                    inputSlot.use(target, transaction)
                    discSlot.use(disc, transaction)
                    outputSlot.take(output, transaction)
                    transaction.commit()
                }
                playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE)
            }
        }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var discSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(listener, filter = Predicate.not(RagiumItems.MEMORY_DISC::isOf))
        )
        discSlot = builder.addSlot(
            HTSlotInfo.NONE,
            HTBasicItemSlot.input(
                listener,
                filter = { RagiumItems.MEMORY_DISC.isOf(it) && !it.has(RagiumDataComponents.MEMORY_DISC_DATA) }
            )
        )
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1))
        // progress
        addProgressBar(widgetHolder)
        // inputs
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT
        )
        widgetHolder.track(inputSlot)
        widgetHolder += HTItemWidget.Container(
            discSlot,
            1,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.EXTRA_INPUT
        )
        widgetHolder.track(discSlot)
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            2,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT
        )
        widgetHolder.track(outputSlot)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.scanner

    //    FakeRecipe    //

    data object FakeRecipe : HTRecipeFactories.DoubleItem<ItemStack> {
        override fun getProgressData(first: ItemInstance, second: ItemInstance): HTProgressData {
            val modifier: Int = first.getOrDefault(DataComponents.RARITY, Rarity.COMMON).ordinal + 1
            return HTProgressData.time(20 * 30 * modifier)
        }

        override fun apply(first: ItemInstance, second: ItemInstance): ItemStack {
            val template: ItemStackTemplate = HTIngredientHelper.unwrap(first)
                .toTemplateOrNull()
                ?: return ItemStack.EMPTY
            val result: ItemStack = HTIngredientHelper.unwrap(second).copyWithCount(1)
            result[RagiumDataComponents.MEMORY_DISC_DATA] = HTMemoryDiscData.from(template)
            return result
        }

        override fun getMatchingStack(first: ItemInstance, second: ItemInstance): Pair<ItemInstance, ItemInstance> =
            HTIngredientHelper.copyWithCount(first, 1) to HTIngredientHelper.copyWithCount(second, 1)
    }
}
