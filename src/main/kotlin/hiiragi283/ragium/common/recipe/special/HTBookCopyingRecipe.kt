package hiiragi283.ragium.common.recipe.special

import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.ragium.api.recipe.HTItemAndItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.WrittenBookContent
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

data object HTBookCopyingRecipe : HTItemAndItemRecipe {
    override fun testFirstItem(stack: ItemStack): Boolean = stack.`is`(Items.WRITABLE_BOOK)

    override fun testSecondItem(stack: ItemStack): Boolean =
        stack.`is`(Items.WRITTEN_BOOK) && stack.has(DataComponents.WRITTEN_BOOK_CONTENT)

    override fun assemble(input: HTDoubleRecipeInput, registries: HolderLookup.Provider): ItemStack {
        val parentStack: ItemStack = input.second.copy()
        val writtenTexts: WrittenBookContent = parentStack.getOrDefault(DataComponents.WRITTEN_BOOK_CONTENT, WrittenBookContent.EMPTY)
        val copiedTexts: WrittenBookContent = writtenTexts.tryCraftCopy() ?: return input.first.copyWithCount(1)
        parentStack.set(DataComponents.WRITTEN_BOOK_CONTENT, copiedTexts)
        return parentStack
    }

    override fun getRequiredAmount(input: HTDoubleRecipeInput): Pair<Int, Int> = 1 to 0

    override val time: Int = 100

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BOOK_COPYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PRINTING.get()
}
