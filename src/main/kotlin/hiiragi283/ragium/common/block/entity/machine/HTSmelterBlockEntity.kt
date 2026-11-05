package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.HTSingleRecipeHandler
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.lookup.HTSingleRecipeLookupHandler
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemResource

class HTSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.SMELTER.get(), pos, state),
    HTSingleRecipeLookupHandler.ForItem<HTItemToItemRecipe> {
    companion object {
        @JvmStatic
        private val FAKE_INPUT = SingleRecipeInput(ItemStack.EMPTY)
    }

    override val recipeCache: HTRecipeCacheHelper.SingleItem<HTItemToItemRecipe> = HTRecipeCacheHelper.SingleItem { context: HTRecipeLookup.Context ->
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

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    override fun createRecipeHandler(): HTRecipeHandler = HTSingleRecipeHandler.Basic(
        HTInputHandlerHelper.forItem(inputSlot),
        HTOutputSlotHelper.forItem(outputSlot),
        SingleProgressor(),
        ::findFirstRecipe,
        HTItemToItemRecipe::getMatchingStack,
    ) { playSound(SoundEvents.FIRE_EXTINGUISH) }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var typeSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
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

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.smelter
}
