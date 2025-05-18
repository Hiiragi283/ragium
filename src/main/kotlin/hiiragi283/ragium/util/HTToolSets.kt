package hiiragi283.ragium.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.item.HTForgeHammerItem
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.registry.HTItemSet
import hiiragi283.ragium.api.tag.RagiumItemTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredItem

class HTToolSets(material: Tier, name: String, private val tagKey: TagKey<Item>) : HTItemSet {
    private val itemRegister = HTItemRegister(RagiumAPI.MOD_ID)

    val axeItem: DeferredItem<AxeItem> = itemRegister.registerItem(
        "${name}_axe",
        { AxeItem(material, it) },
        Item.Properties().attributes(DiggerItem.createAttributes(material, 6f, -3.1f)),
    )

    val hoeItem: DeferredItem<HoeItem> = itemRegister.registerItem(
        "${name}_hoe",
        { HoeItem(material, it) },
        Item.Properties().attributes(DiggerItem.createAttributes(material, -2f, -1f)),
    )

    val pickaxeItem: DeferredItem<PickaxeItem> = itemRegister.registerItem(
        "${name}_pickaxe",
        { PickaxeItem(material, it) },
        Item.Properties().attributes(DiggerItem.createAttributes(material, 1f, -2.8f)),
    )

    val shovelItem: DeferredItem<ShovelItem> = itemRegister.registerItem(
        "${name}_shovel",
        { ShovelItem(material, it) },
        Item.Properties().attributes(DiggerItem.createAttributes(material, 1.5f, -3f)),
    )

    val swordItem: DeferredItem<SwordItem> = itemRegister.registerItem(
        "${name}_sword",
        { SwordItem(material, it) },
        Item.Properties().attributes(DiggerItem.createAttributes(material, 3f, -2.4f)),
    )

    val hammerItem: DeferredItem<HTForgeHammerItem> = itemRegister.registerItem(
        "${name}_hammer",
        { HTForgeHammerItem(material, it) },
    )

    //    HTItemSet    //

    override val itemHolders: List<DeferredItem<*>> = itemRegister.entries

    override fun init(eventBus: IEventBus) {
        itemRegister.register(eventBus)
    }

    override fun appendItemTags(builder: HTTagBuilder.ItemTag) {
        builder.add(ItemTags.AXES, axeItem)
        builder.add(ItemTags.HOES, hoeItem)
        builder.add(ItemTags.PICKAXES, pickaxeItem)
        builder.add(ItemTags.SHOVELS, shovelItem)
        builder.add(ItemTags.SWORDS, swordItem)
        builder.add(RagiumItemTags.TOOLS_FORGE_HAMMER, hammerItem)
    }

    override fun addRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Axe
        HTShapedRecipeBuilder(axeItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "A ",
                "AB",
                "BB",
            ).define('A', Tags.Items.RODS_WOODEN)
            .define('B', tagKey)
            .save(output)
        // Hoe
        HTShapedRecipeBuilder(hoeItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "A ",
                "A ",
                "BB",
            ).define('A', Tags.Items.RODS_WOODEN)
            .define('B', tagKey)
            .save(output)
        // Pickaxe
        HTShapedRecipeBuilder(pickaxeItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                " A ",
                " A ",
                "BBB",
            ).define('A', Tags.Items.RODS_WOODEN)
            .define('B', tagKey)
            .save(output)
        // Shovel
        HTShapedRecipeBuilder(shovelItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "A",
                "A",
                "B",
            ).define('A', Tags.Items.RODS_WOODEN)
            .define('B', tagKey)
            .save(output)
        // Sword
        HTShapedRecipeBuilder(swordItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "A",
                "B",
                "B",
            ).define('A', Tags.Items.RODS_WOODEN)
            .define('B', tagKey)
            .save(output)
        // Forge Hammer
        HTShapedRecipeBuilder(hammerItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(" AA")
            .pattern("BBA")
            .pattern(" AA")
            .define('A', tagKey)
            .define('B', Tags.Items.RODS_WOODEN)
            .save(output)
    }

    override fun addItemModels(provider: ItemModelProvider) {
        getItems().forEach(provider::handheldItem)
    }

    override fun addTranslationEn(name: String, provider: LanguageProvider) {
        provider.addItem(axeItem, "$name Axe")
        provider.addItem(hoeItem, "$name Hoe")
        provider.addItem(pickaxeItem, "$name Pickaxe")
        provider.addItem(shovelItem, "$name Shovel")
        provider.addItem(swordItem, "$name Sword")
        provider.addItem(hammerItem, "$name Forge Hammer")
    }

    override fun addTranslationJp(name: String, provider: LanguageProvider) {
        provider.addItem(axeItem, "${name}の斧")
        provider.addItem(hoeItem, "${name}のクワ")
        provider.addItem(pickaxeItem, "${name}のツルハシ")
        provider.addItem(shovelItem, "${name}のシャベル")
        provider.addItem(swordItem, "${name}の剣")
        provider.addItem(hammerItem, "${name}の鍛造ハンマー")
    }
}
