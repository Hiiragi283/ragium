package hiiragi283.ragium.data.server

import aztech.modern_industrialization.MI
import com.buuz135.industrial.utils.IndustrialTags
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.add
import hiiragi283.ragium.api.extension.addItem
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import mekanism.generators.common.registries.GeneratorsItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import java.util.concurrent.CompletableFuture

class RagiumItemTagProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper,
) : TagsProvider<Item>(output, Registries.ITEM, provider, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        materialTags()
        foodTags()
        toolTags()
        partTags()
    }

    //    Material    //

    private fun materialTags() {
        RagiumBlocks.ORES.forEach { (_, key: HTMaterialKey, ore: DeferredBlock<out Block>) ->
            val oreTagKey: TagKey<Item> = HTTagPrefix.ORE.createTag(key)

            tag(HTTagPrefix.ORE.commonTagKey)
                .addTag(oreTagKey)

            tag(oreTagKey)
                .addItem(HTBlockContent.of(ore))
        }

        RagiumBlocks.STORAGE_BLOCKS.forEach { (key: HTMaterialKey, storage: DeferredBlock<Block>) ->
            val storageTagKey: TagKey<Item> = HTTagPrefix.STORAGE_BLOCK.createTag(key)

            tag(HTTagPrefix.STORAGE_BLOCK.commonTagKey)
                .addTag(storageTagKey)

            tag(storageTagKey)
                .addItem(HTBlockContent.of(storage))
        }

        RagiumItems.materialItems.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, holder: DeferredItem<out Item>) ->
            val tagKey: TagKey<Item> = prefix.createTag(key)

            tag(prefix.commonTagKey)
                .addTag(tagKey)

            tag(tagKey)
                .add(holder)
        }

        addMaterialTag(HTTagPrefix.DUST, IntegrationMaterials.DARK_GEM, "evilcraft:dark_gem_crushed")
        addMaterialTag(HTTagPrefix.GEM, IntegrationMaterials.DARK_GEM, "evilcraft:dark_gem")
        addMaterialTag(HTTagPrefix.GEM, VanillaMaterials.NETHERITE_SCRAP, "netherite_scrap", false)
        addMaterialTag(HTTagPrefix.ORE, IntegrationMaterials.DARK_GEM, "evilcraft:dark_ore")
        addMaterialTag(HTTagPrefix.ORE, IntegrationMaterials.DARK_GEM, "evilcraft:dark_ore_deepslate")
        addMaterialTag(HTTagPrefix.STORAGE_BLOCK, IntegrationMaterials.DARK_GEM, "evilcraft:dark_block")
    }

    private fun addMaterialTag(
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        value: String,
        optional: Boolean = true,
    ) {
        tag(prefix.commonTagKey)
            .addTag(prefix.createTag(material))

        tag(prefix.createTag(material))
            .add(DeferredItem.createItem(ResourceLocation.parse(value)), optional)
    }

    //    Food    //

    private fun foodTags() {
        val foods: TagAppender<Item> = tag(Tags.Items.FOODS)
        RagiumItems.FOODS.forEach { foodItem: DeferredItem<Item> ->
            if (foodItem.get().components().has(DataComponents.FOOD)) {
                foods.add(foodItem)
            }
        }

        tag(RagiumItemTags.DOUGH).add(RagiumItems.DOUGH)
    }

    //    Tool    //

    private fun toolTags() {
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(RagiumItems.FORGE_HAMMER)

        tag(ItemTags.PICKAXES).add(RagiumItems.SILKY_PICKAXE)

        tag(
            itemTagKey(ResourceLocation.fromNamespaceAndPath("modern_industrialization", "forge_hammer_tools")),
        ).add(RagiumItems.FORGE_HAMMER)
    }

    //    Part    //

    private fun partTags() {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            tag(tier.getCircuitTag())
                .add(tier.getCircuit())
        }

        tag(RagiumItemTags.ALKALI_REAGENTS)
            .add(RagiumItems.ALKALI_REAGENT)

        tag(ItemTags.COALS)
            .add(RagiumItems.RESIDUAL_COKE)
        tag(RagiumItemTags.COAL_COKE)
            .add(RagiumItems.COKE)
            .addOptional(ResourceLocation.fromNamespaceAndPath(MI.ID, "coke"))

        tag(RagiumItemTags.PLASTICS)
            .add(RagiumItems.PLASTIC_PLATE)

        tag(RagiumItemTags.SOLAR_PANELS)
            .add(RagiumItems.SOLAR_PANEL)
            .add(GeneratorsItems.SOLAR_PANEL, true)

        // Industrial Foregoing
        tag(IndustrialTags.Items.MACHINE_FRAME_PITY)
            .addItem(RagiumBlocks.Casings.BASIC)
        tag(IndustrialTags.Items.MACHINE_FRAME_SIMPLE)
            .addItem(RagiumBlocks.Casings.ADVANCED)
        tag(IndustrialTags.Items.MACHINE_FRAME_ADVANCED)
            .addItem(RagiumBlocks.Casings.ELITE)
        tag(IndustrialTags.Items.MACHINE_FRAME_SUPREME)
            .addItem(RagiumBlocks.Casings.ULTIMATE)
    }
}
