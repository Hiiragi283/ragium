package hiiragi283.ragium.api.registry

import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredItem

interface HTItemSet {
    val itemHolders: List<DeferredItem<*>>

    fun getItems(): List<Item> = itemHolders.map(ItemLike::asItem)

    fun init(eventBus: IEventBus)

    fun addRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider)

    fun addItemModels(provider: ItemModelProvider)

    fun addTranslationEn(name: String, provider: LanguageProvider)

    fun addTranslationJp(name: String, provider: LanguageProvider)
}
