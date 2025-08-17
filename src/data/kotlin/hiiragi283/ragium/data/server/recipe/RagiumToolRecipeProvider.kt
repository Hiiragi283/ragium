package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTStonecuttingRecipeBuilder
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.common.recipe.HTBlastChargeRecipe
import hiiragi283.ragium.common.recipe.HTEternalTicketRecipe
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.HTLootTicketHelper
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.material.RagiumTierType
import hiiragi283.ragium.util.variant.HTToolVariant
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootTable
import net.neoforged.neoforge.common.Tags

object RagiumToolRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        HTShapedRecipeBuilder(RagiumItems.DRILL)
            .pattern(
                " A ",
                "ABA",
                "ACA",
            ).define('A', HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', HTMaterialVariant.CIRCUIT, RagiumTierType.BASIC)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.POTION_BUNDLE)
            .pattern(
                " A ",
                "BBB",
            ).define('A', Tags.Items.STRINGS)
            .define('B', Items.GLASS_BOTTLE)
            .save(output)

        HTStonecuttingRecipeBuilder(RagiumItems.SLOT_COVER, 3)
            .addIngredient(Items.SMOOTH_STONE_SLAB)
            .save(output)

        HTShapelessRecipeBuilder(RagiumItems.TRADER_CATALOG)
            .addIngredient(Items.BOOK)
            .addIngredient(HTMaterialVariant.GEM, HTVanillaMaterialType.EMERALD)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.BLAST_CHARGE, 8)
            .hollow8()
            .define('A', Tags.Items.GUNPOWDERS)
            .define('B', HTMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ENDER_BUNDLE)
            .pattern(
                " A ",
                "ABA",
                "AAA",
            ).define('A', Tags.Items.LEATHERS)
            .define('B', HTMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ELDRITCH_EGG)
            .hollow4()
            .define('A', HTMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL)
            .define('B', Tags.Items.EGGS)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ETERNAL_COMPONENT)
            .cross8()
            .define('A', HTMaterialVariant.INGOT, RagiumMaterialType.IRIDESCENTIUM)
            .define('B', Items.CLOCK)
            .define('C', RagiumItems.getComponent(RagiumTierType.ULTIMATE))
            .save(output)

        ragiAlloy()
        azureAndDeepSteel()

        forgeHammers()

        tickets()
        lootTickets()
    }

    @JvmStatic
    private fun ragiAlloy() {
        // Basic
        HTShapedRecipeBuilder(RagiumItems.RAGI_MAGNET)
            .pattern(
                "A A",
                "B B",
                " C ",
            ).define('A', HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('C', HTMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .save(output)

        // Advanced
        createComponentUpgrade(
            RagiumTierType.ADVANCED,
            RagiumItems.ADVANCED_RAGI_MAGNET,
            RagiumItems.RAGI_MAGNET,
        ).save(output)

        // Elite
        HTShapedRecipeBuilder(RagiumItems.RAGI_LANTERN)
            .hollow4()
            .define('A', HTMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .define('B', Items.LANTERN)
            .save(output)
    }

    @JvmStatic
    private fun azureAndDeepSteel() {
        addUpgrades(RagiumItems.ARMORS)
        addUpgrades(RagiumItems.TOOLS)

        addTemplate(
            RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE,
            RagiumMaterialType.AZURE_STEEL,
        )
        addTemplate(
            RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE,
            RagiumMaterialType.DEEP_STEEL,
        )
    }

    @JvmStatic
    private fun <V : HTVariantKey> addUpgrades(table: HTTable<V, HTMaterialType, out ItemLike>) {
        table.forEach { (variant: V, material: HTMaterialType, item: ItemLike) ->
            val base: HTVanillaMaterialType = when (material) {
                RagiumMaterialType.AZURE_STEEL -> HTVanillaMaterialType.IRON
                RagiumMaterialType.DEEP_STEEL -> HTVanillaMaterialType.DIAMOND
                else -> return@forEach
            }
            val id: ResourceLocation = when (variant) {
                HTToolVariant.HAMMER -> RagiumAPI::id
                else -> ::vanillaId
            }("${base.serializedName}_${variant.serializedName}")
            when (material) {
                RagiumMaterialType.AZURE_STEEL -> ::addAzureSmithing
                RagiumMaterialType.DEEP_STEEL -> ::addDeepSmithing
                else -> return@forEach
            }(
                item,
                BuiltInRegistries.ITEM.get(id),
            )
        }
    }

    @JvmStatic
    private fun forgeHammers() {
        fun hammer(material: HTMaterialType): ItemLike = RagiumItems.getForgeHammer(material)

        fun crafting(variant: HTMaterialVariant, material: HTMaterialType) {
            HTShapedRecipeBuilder(hammer(material), category = CraftingBookCategory.EQUIPMENT)
                .pattern(
                    " AA",
                    "BBA",
                    " AA",
                ).define('A', variant, material)
                .define('B', Tags.Items.RODS_WOODEN)
                .save(output)
        }

        crafting(HTMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
        crafting(HTMaterialVariant.GEM, HTVanillaMaterialType.DIAMOND)
        crafting(HTMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)

        createNetheriteUpgrade(hammer(HTVanillaMaterialType.NETHERITE), hammer(HTVanillaMaterialType.DIAMOND)).save(output)
        createComponentUpgrade(
            RagiumTierType.ELITE,
            hammer(RagiumMaterialType.RAGI_CRYSTAL),
            hammer(RagiumMaterialType.RAGI_ALLOY),
        ).save(output)
    }

    @JvmStatic
    private fun tickets() {
        addTicket(
            RagiumItems.RAGI_TICKET,
            HTMaterialVariant.GEM,
            RagiumMaterialType.RAGI_CRYSTAL,
            DyeColor.RED,
        )
        addTicket(
            RagiumItems.TELEPORT_TICKET,
            HTMaterialVariant.GEM,
            RagiumMaterialType.WARPED_CRYSTAL,
            DyeColor.CYAN,
        )

        save(
            RagiumAPI.id("shapeless/blast_charge"),
            HTBlastChargeRecipe(CraftingBookCategory.EQUIPMENT),
        )
        save(
            RagiumAPI.id("shapeless/eternal_ticket"),
            HTEternalTicketRecipe(CraftingBookCategory.MISC),
        )
    }

    @JvmStatic
    private fun lootTickets() {
        // End City
        addLootTicket(BuiltInLootTables.END_CITY_TREASURE) {
            define('A', Items.PURPUR_BLOCK)
            define('B', Items.SHULKER_SHELL)
        }
        // Simple Dungeon
        addLootTicket(BuiltInLootTables.SIMPLE_DUNGEON) {
            define('A', Tags.Items.COBBLESTONES_MOSSY)
            define('B', Items.ROTTEN_FLESH)
        }
        // Mineshaft
        addLootTicket(BuiltInLootTables.ABANDONED_MINESHAFT) {
            define('A', ItemTags.PLANKS)
            define('B', ItemTags.RAILS)
        }
        // Nether Fortress
        addLootTicket(BuiltInLootTables.NETHER_BRIDGE) {
            define('A', Items.NETHER_BRICKS)
            define('B', Tags.Items.CROPS_NETHER_WART)
        }
    }

    //    Extension    //

    @JvmStatic
    private fun addTemplate(template: ItemLike, material: HTMaterialType) {
        HTShapedRecipeBuilder(template)
            .pattern(
                "A A",
                "A A",
                " A ",
            ).define('A', HTMaterialVariant.INGOT, material)
            .save(output)

        HTShapelessRecipeBuilder(template, 2)
            .addIngredient(template)
            .addIngredient(HTMaterialVariant.INGOT, material)
            .addIngredient(HTMaterialVariant.INGOT, material)
            .saveSuffixed(output, "_duplicate")
    }

    @JvmStatic
    private fun addAzureSmithing(output: ItemLike, ingredient: ItemLike) {
        HTSmithingRecipeBuilder(output)
            .addIngredient(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(ingredient)
            .addIngredient(HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .save(this.output)
    }

    @JvmStatic
    private fun addDeepSmithing(output: ItemLike, ingredient: ItemLike) {
        HTSmithingRecipeBuilder(output)
            .addIngredient(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(ingredient)
            .addIngredient(HTMaterialVariant.INGOT, RagiumMaterialType.DEEP_STEEL)
            .save(this.output)
    }

    @JvmStatic
    private fun addTicket(
        ticket: ItemLike,
        variant: HTMaterialVariant,
        material: HTMaterialType,
        dye: DyeColor,
    ) {
        HTShapedRecipeBuilder(ticket)
            .cross8()
            .define('A', variant, material)
            .define('B', dye.tag)
            .define('C', Items.PAPER)
            .save(output)
    }

    @JvmStatic
    private fun addLootTicket(lootTableKey: ResourceKey<LootTable>, builderAction: HTShapedRecipeBuilder.() -> Unit) {
        HTShapedRecipeBuilder(HTLootTicketHelper.getLootTicket(lootTableKey))
            .cross8()
            .apply(builderAction)
            .define('C', RagiumItems.RAGI_TICKET)
            .saveSuffixed(output, lootTableKey.location().path.removePrefix("chests"))
    }
}
