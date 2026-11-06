package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTMemoryDiscData
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTDoubleItemToItemBlockEntity
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Predicate

class HTScannerBlockEntity(pos: BlockPos, state: BlockState) :
    HTDoubleItemToItemBlockEntity(
        RagiumBlockEntityTypes.SCANNER.get(),
        pos,
        state,
    ) {
    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var discSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(listener, filter = Predicate.not(RagiumItems.MEMORY_DISC::isOf)),
        )
        discSlot = builder.addSlot(
            HTSlotInfo.NONE,
            HTBasicItemSlot.catalyst(
                listener,
                filter = { RagiumItems.MEMORY_DISC.isOf(it) && !it.has(RagiumDataComponents.MEMORY_DISC_DATA) },
            ),
        )
        createOutputSlot(builder, listener)
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
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        widgetHolder += HTItemWidget.Container(
            discSlot,
            1,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.EXTRA_INPUT,
        )
        widgetHolder.track(discSlot)
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            2,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
    }

    //    HTDoubleItemToItemBlockEntity    //

    override fun getLookup(): HTRecipeLookup<HTDoubleItemToItemRecipe> = HTRecipeLookup {
        sequenceOf(HTRecipeHolder(RagiumAPI.id("scanning/fake"), FakeRecipe))
    }

    override fun createFirstInputHandler(): HTInputSlot<ItemInstance> = HTInputHandlerHelper.forItem(inputSlot)

    override fun createSecondInputHandler(): HTInputSlot<ItemInstance> = HTInputHandlerHelper.forItem(discSlot)

    override fun onComplete() {
        playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.scanner

    //    FakeRecipe    //

    data object FakeRecipe : HTDoubleItemToItemRecipe {
        override fun test(first: ItemInstance, second: ItemInstance): Boolean = !second.has(RagiumDataComponents.MEMORY_DISC_DATA)

        override fun getProgressData(first: ItemInstance, second: ItemInstance): HTProgressData {
            val modifier: Int = first.getOrDefault(DataComponents.RARITY, Rarity.COMMON).ordinal + 1
            return HTProgressData.time(20 * 30 * modifier)
        }

        override fun apply(first: ItemInstance, second: ItemInstance): ItemStack {
            val template: ItemStack = HTIngredientHelper.unwrap(first)
            val result: ItemStack = HTIngredientHelper.unwrap(second).copyWithCount(1)
            result[RagiumDataComponents.MEMORY_DISC_DATA] = HTMemoryDiscData.from(template)
            return result
        }

        override fun getMatchingStack(first: ItemInstance, second: ItemInstance): Pair<ItemInstance, ItemInstance> =
            HTIngredientHelper.copyWithCount(first, 1) to HTIngredientHelper.copyWithCount(second, 1)
    }
}
