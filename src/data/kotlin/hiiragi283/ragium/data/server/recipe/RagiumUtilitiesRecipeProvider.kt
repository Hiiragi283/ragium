package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTStonecuttingRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.crafting.HTPotionDropRecipe
import hiiragi283.ragium.common.item.HTDefaultLootTickets
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags

object RagiumUtilitiesRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Loot Ticket
        lootTickets()
        // Potion Drop -> Potion
        save(id("shapeless/potion_from_drop"), HTPotionDropRecipe(CraftingBookCategory.MISC))
        // Slot Cover
        HTStonecuttingRecipeBuilder
            .create(RagiumItems.SLOT_COVER, 3)
            .addIngredient(Items.SMOOTH_STONE_SLAB)
            .save(output)
        // Trader Catalog
        HTShapelessRecipeBuilder
            .create(RagiumItems.TRADER_CATALOG)
            .addIngredient(Items.BOOK)
            .addIngredient(HCMaterialPrefixes.GEM, HCMaterial.Gems.EMERALD)
            .setCategory(CraftingBookCategory.EQUIPMENT)
            .save(output)
    }

    @JvmStatic
    private fun lootTickets() {
        HTShapedRecipeBuilder
            .create(RagiumItems.LOOT_TICKET)
            .cross8()
            .define('A', HCMaterialPrefixes.GEM, RagiumMaterial.RAGI_CRYSTAL)
            .define('B', Tags.Items.DYES_RED)
            .define('C', Items.PAPER)
            .setCategory(CraftingBookCategory.EQUIPMENT)
            .save(output)

        // End City
        addLootTicket(HTDefaultLootTickets.END_CITY) {
            addIngredient(Items.PURPUR_BLOCK)
            addIngredient(Items.SHULKER_SHELL)
        }
        // Simple Dungeon
        addLootTicket(HTDefaultLootTickets.DUNGEON) {
            addIngredient(Tags.Items.COBBLESTONES_MOSSY)
            addIngredient(Items.ROTTEN_FLESH)
        }
        // Mineshaft
        addLootTicket(HTDefaultLootTickets.MINESHAFT) {
            addIngredient(ItemTags.PLANKS)
            addIngredient(ItemTags.RAILS)
        }
        // Nether Fortress
        addLootTicket(HTDefaultLootTickets.NETHER_FORTRESS) {
            addIngredient(Items.NETHER_BRICKS)
            addIngredient(Tags.Items.CROPS_NETHER_WART)
        }

        // Desert Pyramid
        addLootTicket(HTDefaultLootTickets.DESERT_PYRAMID) {
            addIngredient(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            addIngredient(HCMaterialPrefixes.INGOT, HCMaterial.Metals.GOLD)
        }
        // Jungle Temple
        addLootTicket(HTDefaultLootTickets.TEMPLE) {
            addIngredient(Tags.Items.COBBLESTONES_MOSSY)
            addIngredient(Items.VINE)
        }
        // Igloo Chest
        addLootTicket(HTDefaultLootTickets.IGLOO) {
            addIngredient(Items.SNOW_BLOCK)
            addIngredient(ItemTags.BEDS)
        }
        // Mansion
        addLootTicket(HTDefaultLootTickets.MANSION) {
            addIngredient(Items.DARK_OAK_PLANKS)
            addIngredient(HCMaterialPrefixes.GEM, HCMaterial.Gems.EMERALD)
        }

        // Buried Treasure
        addLootTicket(HTDefaultLootTickets.BURIED_TREASURE) {
            addIngredient(Tags.Items.SANDS_COLORLESS)
            addIngredient(Items.PUFFERFISH)
        }
        // Shipwreck
        addLootTicket(HTDefaultLootTickets.SHIPWRECK) {
            addIngredient(Tags.Items.CHESTS_WOODEN)
            addIngredient(Items.KELP)
        }
        // Bastion Remnant
        addLootTicket(HTDefaultLootTickets.BASTION_REMNANT) {
            addIngredient(Items.BLACKSTONE)
            addIngredient(HCMaterialPrefixes.INGOT, HCMaterial.Metals.GOLD)
        }
        // Ancient City
        addLootTicket(HTDefaultLootTickets.ANCIENT_CITY) {
            addIngredient(Items.DEEPSLATE_TILES)
            addIngredient(HCMaterialPrefixes.GEM, HCMaterial.Gems.ECHO)
        }
        // Ruined Portal
        addLootTicket(HTDefaultLootTickets.RUINED_PORTAL) {
            addIngredient(Tags.Items.OBSIDIANS_NORMAL)
            addIngredient(Tags.Items.CROPS_NETHER_WART)
        }
    }

    @JvmStatic
    private inline fun addLootTicket(lootTicket: HTDefaultLootTickets, builderAction: HTShapelessRecipeBuilder.() -> Unit) {
        val ticket: ImmutableItemStack = HTDefaultLootTickets.getLootTicket(lootTicket)
        HTShapelessRecipeBuilder(ticket)
            .addIngredient(RagiumItems.LOOT_TICKET)
            .apply(builderAction)
            .setCategory(CraftingBookCategory.EQUIPMENT)
            .saveSuffixed(output, "/${lootTicket.name.lowercase()}")
    }
}
