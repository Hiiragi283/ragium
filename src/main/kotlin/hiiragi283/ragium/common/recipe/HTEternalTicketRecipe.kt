package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.extension.indices
import hiiragi283.ragium.setup.RagiumCustomRecipeSerializers
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class HTEternalTicketRecipe(category: CraftingBookCategory) : CustomRecipe(category) {
    override fun matches(input: CraftingInput, level: Level): Boolean {
        var isTicket = false
        var isEquipment = false
        for (index: Int in input.indices) {
            val stackIn: ItemStack = input.getItem(index)
            if (stackIn.isEmpty) continue
            if (stackIn.`is`(RagiumItems.ETERNAL_COMPONENT) && !isTicket) {
                isTicket = true
            } else if (stackIn.isDamageableItem && !isEquipment) {
                isEquipment = true
            } else {
                return false
            }
        }
        return isTicket && isEquipment
    }

    override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack {
        var equipment: ItemStack = ItemStack.EMPTY
        for (index: Int in input.indices) {
            val stackIn: ItemStack = input.getItem(index)
            if (stackIn.isEmpty) continue
            if (stackIn.isDamageableItem) {
                equipment = stackIn.copy()
            }
        }
        if (equipment.isEmpty) return ItemStack.EMPTY
        equipment.set(DataComponents.UNBREAKABLE, Unbreakable(true))
        return equipment
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> = RagiumCustomRecipeSerializers.ETERNAL_TICKET.get()
}
