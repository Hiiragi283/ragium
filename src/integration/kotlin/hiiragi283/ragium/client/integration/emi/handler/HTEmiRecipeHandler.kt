package hiiragi283.ragium.client.integration.emi.handler

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.inventory.HTContainerItemSlot
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.world.inventory.Slot

open class HTEmiRecipeHandler<BE : HTBlockEntity>(private val categories: List<EmiRecipeCategory>) :
    StandardRecipeHandler<HTBlockEntityContainerMenu<BE>> {
    constructor(vararg categories: EmiRecipeCategory) : this(listOf(*categories))

    override fun getInputSources(handler: HTBlockEntityContainerMenu<BE>): List<Slot> = handler.slots

    override fun getCraftingSlots(handler: HTBlockEntityContainerMenu<BE>): List<Slot> = handler.slots
        .filterIsInstance<HTContainerItemSlot>()
        .filter { slot: HTContainerItemSlot -> slot.slotType == HTContainerItemSlot.Type.INPUT }

    override fun getOutputSlot(handler: HTBlockEntityContainerMenu<BE>): Slot? = handler.slots
        .filterIsInstance<HTContainerItemSlot>()
        .firstOrNull { slot: HTContainerItemSlot -> slot.slotType == HTContainerItemSlot.Type.OUTPUT }

    final override fun supportsRecipe(recipe: EmiRecipe): Boolean = recipe.category in this.categories
}
