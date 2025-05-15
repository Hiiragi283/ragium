package hiiragi283.ragium.common.recipe.custom

import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SmithingRecipe
import net.minecraft.world.item.crafting.SmithingRecipeInput
import net.minecraft.world.level.Level

object HTEternalTicketRecipe : SmithingRecipe {
    override fun isTemplateIngredient(stack: ItemStack): Boolean = stack.`is`(RagiumItems.ETERNAL_TICKET)

    override fun isBaseIngredient(stack: ItemStack): Boolean = stack.isDamageableItem

    override fun isAdditionIngredient(stack: ItemStack): Boolean = stack.isEmpty

    override fun matches(input: SmithingRecipeInput, level: Level): Boolean =
        isTemplateIngredient(input.template) && isBaseIngredient(input.base) && isAdditionIngredient(input.addition)

    override fun assemble(input: SmithingRecipeInput, registries: HolderLookup.Provider): ItemStack {
        val base: ItemStack = input.base.copy()
        base.set(DataComponents.UNBREAKABLE, Unbreakable(true))
        return base
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = createItemStack(Items.IRON_PICKAXE) {
        set(DataComponents.UNBREAKABLE, Unbreakable(true))
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ETERNAL_TICKET.get()
}
