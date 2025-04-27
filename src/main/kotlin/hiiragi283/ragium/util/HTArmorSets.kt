package hiiragi283.ragium.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.material.HTMaterial
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.registry.HTItemSet
import hiiragi283.ragium.api.tag.RagiumItemTags
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredItem

class HTArmorSets(material: Holder<ArmorMaterial>, val key: HTMaterial) : HTItemSet {
    private val itemRegister = HTItemRegister(RagiumAPI.MOD_ID)

    private fun register(material: Holder<ArmorMaterial>, type: ArmorItem.Type): DeferredItem<ArmorItem> = itemRegister.registerItem(
        "${key.materialName}_${type.serializedName}",
        { properties: Item.Properties -> ArmorItem(material, type, properties) },
        Item.Properties().durability(type.getDurability(20)),
    )

    val helmetItem: DeferredItem<ArmorItem> = register(material, ArmorItem.Type.HELMET)
    val chestPlateItem: DeferredItem<ArmorItem> = register(material, ArmorItem.Type.CHESTPLATE)
    val leggingsItem: DeferredItem<ArmorItem> = register(material, ArmorItem.Type.LEGGINGS)
    val bootsItem: DeferredItem<ArmorItem> = register(material, ArmorItem.Type.BOOTS)

    //    HTItemSet    //

    override val itemHolders: List<DeferredItem<*>> = itemRegister.entries

    override fun init(eventBus: IEventBus) {
        itemRegister.register(eventBus)
    }

    override fun appendItemTags(builder: HTTagBuilder.ItemTag) {
        builder.add(ItemTags.HEAD_ARMOR_ENCHANTABLE, helmetItem)
        builder.add(ItemTags.CHEST_ARMOR_ENCHANTABLE, chestPlateItem)
        builder.add(ItemTags.LEG_ARMOR_ENCHANTABLE, leggingsItem)
        builder.add(ItemTags.FOOT_ARMOR_ENCHANTABLE, bootsItem)
    }

    override fun addRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Helmet
        HTShapedRecipeBuilder(helmetItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "AAA",
                "ABA",
            ).define('A', HTTagPrefixes.INGOT, key)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Chestplate
        HTShapedRecipeBuilder(chestPlateItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "ABA",
                "AAA",
                "AAA",
            ).define('A', HTTagPrefixes.INGOT, key)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Leggings
        HTShapedRecipeBuilder(leggingsItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "AAA",
                "ABA",
                "A A",
            ).define('A', HTTagPrefixes.INGOT, key)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Boots
        HTShapedRecipeBuilder(bootsItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "A A",
                "ABA",
            ).define('A', HTTagPrefixes.INGOT, key)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
    }

    override fun addItemModels(provider: ItemModelProvider) {
        getItems().forEach(provider::basicItem)
    }

    override fun addTranslationEn(name: String, provider: LanguageProvider) {
        provider.addItem(helmetItem, "$name Helmet")
        provider.addItem(chestPlateItem, "$name Chestplate")
        provider.addItem(leggingsItem, "$name Leggings")
        provider.addItem(bootsItem, "$name Boots")
    }

    override fun addTranslationJp(name: String, provider: LanguageProvider) {
        provider.addItem(helmetItem, "${name}のヘルメット")
        provider.addItem(chestPlateItem, "${name}のチェストプレート")
        provider.addItem(leggingsItem, "${name}のレギンス")
        provider.addItem(bootsItem, "${name}のブーツ")
    }
}
