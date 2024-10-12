package hiiragi283.ragium.client.integration.patchouli

import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World
import vazkii.patchouli.client.book.BookContentsBuilder
import vazkii.patchouli.client.book.BookEntry
import vazkii.patchouli.client.book.page.abstr.PageSimpleProcessingRecipe

class HTCustomCraftingPage : PageSimpleProcessingRecipe<Recipe<*>>(object : RecipeType<Recipe<*>> {}) {
    companion object {
        private val registry: MutableMap<Identifier, DummyRecipe> = mutableMapOf()

        @JvmStatic
        fun register(
            id: Identifier,
            input: ItemConvertible,
            icon: ItemStack,
            output: ItemStack,
        ) {
            registry[id] = DummyRecipe(Ingredient.ofItems(input), icon, output)
        }

        @JvmStatic
        fun register(
            id: Identifier,
            input: TagKey<Item>,
            icon: ItemStack,
            output: ItemStack,
        ) {
            registry[id] = DummyRecipe(Ingredient.fromTag(input), icon, output)
        }
    }

    @Transient
    private var loadedId: Identifier? = null

    override fun loadRecipe(
        level: World?,
        builder: BookContentsBuilder,
        entry: BookEntry,
        res: Identifier?,
    ): Recipe<*>? {
        if (level == null || res == null) return null
        if (loadedId == res) return null
        registry[res]
        val recipe: DummyRecipe = registry[res] ?: return null
        entry.addRelevantStack(builder, recipe.getResult(level.registryManager), pageNum)
        loadedId = res
        return recipe
    }

    //    Recipe    //

    private class DummyRecipe(private val input: Ingredient, private val icon: ItemStack, private val output: ItemStack) :
        Recipe<RecipeInput> {
        override fun matches(input: RecipeInput, world: World): Boolean = false

        override fun craft(input: RecipeInput, lookup: RegistryWrapper.WrapperLookup): ItemStack = output

        override fun fits(width: Int, height: Int): Boolean = true

        override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup?): ItemStack = output

        override fun getIngredients(): DefaultedList<Ingredient> = DefaultedList.ofSize(1, input)

        override fun createIcon(): ItemStack = icon

        override fun getSerializer(): RecipeSerializer<*>? = null

        override fun getType(): RecipeType<*>? = null
    }
}
