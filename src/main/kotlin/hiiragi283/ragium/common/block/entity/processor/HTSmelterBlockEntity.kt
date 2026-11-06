package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTItemToBlockEntity
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.item.ItemResource

class HTSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemToBlockEntity.SingleOutput<ItemStack, HTItemToItemRecipe>(RagiumBlockEntityTypes.SMELTER.get(), pos, state) {
    companion object {
        @JvmStatic
        private val FAKE_INPUT = SingleRecipeInput(ItemStack.EMPTY)
    }

    private lateinit var typeSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        createInputSlot(builder, listener)
        typeSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.catalyst(listener))
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
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        widgetHolder += HTItemWidget.Container(
            typeSlot,
            1,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.NONE,
        )
        widgetHolder.track(typeSlot)
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

    //    HTItemToBlockEntity    //

    override fun getLookup(): HTRecipeLookup<HTItemToItemRecipe> = HTRecipeLookup { context: HTRecipeLookup.Context ->
        val resourceIn: ItemResource = typeSlot.resource
        when {
            resourceIn.`is`(Items.BLAST_FURNACE) -> RecipeType.SMELTING
            resourceIn.`is`(Items.SMOKER) -> RecipeType.BLASTING
            else -> RecipeType.SMOKING
        }.let(context::byType).map { it.map(::wrapRecipe) }
    }

    private fun wrapRecipe(recipe: AbstractCookingRecipe): HTItemToItemRecipe = object : HTItemToItemRecipe {
        override val ingredient = HTItemIngredient(recipe.input(), 1)

        override fun apply(input: ItemInstance): ItemStack = recipe.assemble(FAKE_INPUT)

        override fun getMatchingStack(input: ItemInstance): ItemInstance = ingredient.getMatchingStack(input)

        override fun getProgressData(input: ItemInstance): HTProgressData = HTProgressData.time(recipe.cookingTime())
    }

    override fun createOutputHandler(): HTOutputSlot<ItemStack> = HTOutputSlotHelper.forItem(outputSlot)

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.smelter
}
