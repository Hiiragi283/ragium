package hiiragi283.ragium.common.registry

import com.google.common.collect.HashBasedTable
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.google.common.collect.Table
import hiiragi283.ragium.common.data.HTLangType
import hiiragi283.ragium.common.init.RagiumToolMaterials
import hiiragi283.ragium.common.item.HTBaseBlockItem
import hiiragi283.ragium.common.item.HTBaseItem
import hiiragi283.ragium.common.util.forEach
import hiiragi283.ragium.common.util.itemSettings
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.Block
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Model
import net.minecraft.data.client.Models
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.awt.Color
import kotlin.jvm.optionals.getOrNull

class HTItemRegister(private val modId: String) : Iterable<Item> {
    private val itemCache: MutableMap<Identifier, Item> = mutableMapOf()

    private val colorCache: MutableMap<Item, Color> = mutableMapOf()
    private val groupCache: Multimap<RegistryKey<ItemGroup>, Item> = HashMultimap.create()
    private val langCache: Table<String, HTLangType, String> = HashBasedTable.create()
    private val modelCache: MutableMap<Item, (ItemModelGenerator) -> Unit> = mutableMapOf()
    private val tagCache: MutableMap<Item, List<TagKey<Item>>> = mutableMapOf()

    fun registerSimple(name: String, settings: Item.Settings = itemSettings(), action: Builder<Item>.() -> Unit): Item =
        register(name, HTBaseItem(settings), action)

    private fun registerBlockItem(
        name: String,
        block: Block,
        settings: Item.Settings = itemSettings(),
        action: Builder<HTBaseBlockItem>.() -> Unit = {},
    ): Item = register(name, HTBaseBlockItem(block, settings)) {
        action()
        setCustomModel()
    }

    fun registerBlockItem(block: Block, settings: Item.Settings = itemSettings(), action: Builder<HTBaseBlockItem>.() -> Unit = {}) {
        val name: String = Registries.BLOCK
            .getKey(block)
            .map(RegistryKey<Block>::getValue)
            .map(Identifier::getPath)
            .getOrNull() ?: return
        registerBlockItem(name, block, settings, action)
    }

    private fun <T : ToolItem> registerTool(
        name: String,
        material: ToolMaterial,
        builder: (ToolMaterial, Item.Settings) -> T,
        attributeComponent: AttributeModifiersComponent,
        settings: Item.Settings = itemSettings(),
        action: Builder<T>.() -> Unit = {},
    ): T = register(
        name,
        builder(material, settings.attributeModifiers(attributeComponent)),
        action,
    )

    fun registerSword(
        name: String,
        material: ToolMaterial,
        settings: Item.Settings = itemSettings(),
        action: Builder<SwordItem>.() -> Unit = {},
    ): Item = registerTool(
        name,
        material,
        ::SwordItem,
        RagiumToolMaterials.createAttributeComponent(material, 3.0, -2.0),
        settings,
        action,
    )

    fun registerShovel(
        name: String,
        material: ToolMaterial,
        settings: Item.Settings = itemSettings(),
        action: Builder<ShovelItem>.() -> Unit = {},
    ): Item = registerTool(
        name,
        material,
        ::ShovelItem,
        RagiumToolMaterials.createAttributeComponent(material, -2.0, -3.0),
        settings,
        action,
    )

    fun registerPickaxe(
        name: String,
        material: ToolMaterial,
        settings: Item.Settings = itemSettings(),
        action: Builder<PickaxeItem>.() -> Unit = {},
    ): Item = registerTool(
        name,
        material,
        ::PickaxeItem,
        RagiumToolMaterials.createAttributeComponent(material, -2.0, -2.8),
        settings,
        action,
    )

    fun registerAxe(
        name: String,
        material: ToolMaterial,
        settings: Item.Settings = itemSettings(),
        action: Builder<AxeItem>.() -> Unit = {},
    ): Item = registerTool(
        name,
        material,
        ::AxeItem,
        RagiumToolMaterials.createAttributeComponent(material, 3.0, -2.9),
        settings,
        action,
    )

    fun registerHoe(
        name: String,
        material: ToolMaterial,
        settings: Item.Settings = itemSettings(),
        action: Builder<HoeItem>.() -> Unit = {},
    ): Item = registerTool(
        name,
        material,
        ::HoeItem,
        RagiumToolMaterials.createAttributeComponent(material, -4.0, 0.0),
        settings,
        action,
    )

    fun <T : Item> register(name: String, item: T, action: Builder<T>.() -> Unit = {}): T {
        val id: Identifier = Identifier.of(modId, name)
        check(id !in itemCache)
        Registry.register(Registries.ITEM, id, item)
        Builder(name, item, this)
            .generateSimpleModel()
            .apply(action)
        itemCache[id] = item
        return item
    }

    //    Init    //

    fun registerItemGroups() {
        groupCache.keys().forEach { key: RegistryKey<ItemGroup> ->
            ItemGroupEvents.modifyEntriesEvent(key).register { entries: FabricItemGroupEntries ->
                groupCache.get(key).forEach(entries::add)
            }
        }
    }

    //    Client Init    //

    fun registerColors(action: (Item, Color) -> Unit) {
        colorCache.forEach(action)
    }

    //    Data Gen    //

    fun generateLang(type: HTLangType, builder: TranslationBuilder) {
        langCache.forEach { key: String, type1: HTLangType, value: String ->
            if (type == type1) builder.add(key, value)
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
        //    Color    //

        fun setColor(color: Color): Builder<T> = apply {
            register.colorCache[item] = color
        }

        //    ItemGroup    //

        fun setItemGroup(key: RegistryKey<ItemGroup>): Builder<T> = apply {
            register.groupCache.put(key, item)
        }

        //    Translation    //

        fun putTranslation(type: HTLangType, value: String): Builder<T> = apply {
            register.langCache.put(item.translationKey, type, value)
        }

        fun putEnglish(value: String): Builder<T> = putTranslation(HTLangType.EN_US, value)

        fun putJapanese(value: String): Builder<T> = putTranslation(HTLangType.JA_JP, value)

        fun putTooltip(type: HTLangType, value: String): Builder<T> = apply {
            register.langCache.put("${item.translationKey}.tooltip", type, value)
        }

        fun putEnglishTips(value: String): Builder<T> = putTooltip(HTLangType.EN_US, value)

        fun putJapaneseTips(value: String): Builder<T> = putTooltip(HTLangType.JA_JP, value)

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
