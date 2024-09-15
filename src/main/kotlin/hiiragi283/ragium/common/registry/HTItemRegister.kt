package hiiragi283.ragium.common.registry

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import hiiragi283.ragium.common.data.HTLangType
import hiiragi283.ragium.common.util.forEach
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Model
import net.minecraft.data.client.Models
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class HTItemRegister(private val modId: String) : Iterable<Item> {
    private val itemCache: MutableMap<Identifier, Item> = mutableMapOf()

    private val langCache: Table<Item, HTLangType, String> = HashBasedTable.create()
    private val modelCache: MutableMap<Item, (ItemModelGenerator) -> Unit> = mutableMapOf()
    private val tagCache: MutableMap<Item, List<TagKey<Item>>> = mutableMapOf()

    fun registerSimple(name: String, settings: Item.Settings = Item.Settings(), action: Builder<Item>.() -> Unit): Item =
        register(name, Item(settings), action)

    fun registerBlockItem(
        name: String,
        block: Block,
        settings: Item.Settings = Item.Settings(),
        action: Builder<BlockItem>.() -> Unit = {},
    ): BlockItem = register(name, BlockItem(block, settings)) {
        action()
        setCustomModel()
    }

    fun <T : Item> register(name: String, item: T, action: Builder<T>.() -> Unit): T {
        val id: Identifier = Identifier.of(modId, name)
        check(id !in itemCache)
        Registry.register(Registries.ITEM, id, item)
        Builder(name, item, this)
            .generateSimpleModel()
            .apply(action)
        itemCache[id] = item
        return item
    }

    //    Data Gen    //

    fun generateLang(type: HTLangType, builder: TranslationBuilder) {
        langCache.forEach { item: Item, type1: HTLangType, value: String ->
            if (type == type1) builder.add(item, value)
        }
    }

    fun generateModel(generator: ItemModelGenerator) {
        modelCache.values.forEach { it(generator) }
    }

    fun generateTag(builderMapper: (TagKey<Item>) -> FabricTagProvider<Item>.FabricTagBuilder) {
        tagCache.forEach { (item: Item, tagKeys: List<TagKey<Item>>) ->
            tagKeys.map(builderMapper).forEach { it.add(item) }
        }
    }

    //    Iterable    //

    override fun iterator(): Iterator<Item> = itemCache.values.iterator()

    //    Builder    //

    class Builder<T : Item> internal constructor(val name: String, val item: T, val register: HTItemRegister) {
        //    Lang    //

        fun putLang(type: HTLangType, value: String): Builder<T> = apply {
            register.langCache.put(item, type, value)
        }

        fun putEnglishLang(value: String): Builder<T> = putLang(HTLangType.EN_US, value)

        fun putJapaneseLang(value: String): Builder<T> = putLang(HTLangType.JA_JP, value)

        //    Model    //

        fun generateModel(action: (ItemModelGenerator) -> Unit): Builder<T> = apply {
            register.modelCache[item] = action
        }

        fun generateModel(model: Model): Builder<T> = generateModel { it.register(item, model) }

        fun generateSimpleModel(): Builder<T> = generateModel(Models.GENERATED)

        fun setCustomModel(): Builder<T> = apply {
            register.modelCache.remove(item)
        }

        //    Recipe    //

        /*fun registerRecipes(action: (RecipeExporter) -> Unit): Builder<T> = apply {
            register.recipeCache[item] = action
        }*/

        //    Tag    //

        fun registerTags(tagKeys: List<TagKey<Item>>): Builder<T> = apply {
            register.tagCache[item] = tagKeys
        }

        fun registerTags(vararg tagKeys: TagKey<Item>): Builder<T> = registerTags(tagKeys.asList())
    }
}
