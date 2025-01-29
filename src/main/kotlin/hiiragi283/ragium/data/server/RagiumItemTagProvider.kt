package hiiragi283.ragium.data.server

import aztech.modern_industrialization.MI
import com.buuz135.industrial.utils.IndustrialTags
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.addElement
import hiiragi283.ragium.api.extension.addTag
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.integration.RagiumEvilIntegration
import mekanism.generators.common.registries.GeneratorsItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
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
        RagiumBlocks.Ores.entries.forEach { ore: RagiumBlocks.Ores ->
            getOrCreateRawBuilder(ore.tagPrefix.commonTagKey)
                .addTag(ore.prefixedTagKey)

            getOrCreateRawBuilder(ore.prefixedTagKey)
                .addElement(ore)
        }

        RagiumBlocks.StorageBlocks.entries.forEach { storage: RagiumBlocks.StorageBlocks ->
            getOrCreateRawBuilder(storage.tagPrefix.commonTagKey)
                .addTag(storage.prefixedTagKey)

            getOrCreateRawBuilder(storage.prefixedTagKey)
                .addElement(storage)
        }

        RagiumItems.materialItems.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, holder: DeferredItem<out Item>) ->
            val tagKey: TagKey<Item> = prefix.createTag(key)

            getOrCreateRawBuilder(prefix.commonTagKey)
                .addTag(tagKey)

            getOrCreateRawBuilder(tagKey)
                .addElement(holder)
        }

        addMaterialTag(HTTagPrefix.DUST, RagiumEvilIntegration.DARK_GEM, "evilcraft:dark_gem_crushed")

        addMaterialTag(HTTagPrefix.GEM, RagiumEvilIntegration.DARK_GEM, "evilcraft:dark_gem")

        addMaterialTag(HTTagPrefix.ORE, RagiumEvilIntegration.DARK_GEM, "evilcraft:dark_ore")
        addMaterialTag(HTTagPrefix.ORE, RagiumEvilIntegration.DARK_GEM, "evilcraft:dark_ore_deepslate")

        addMaterialTag(HTTagPrefix.STORAGE_BLOCK, RagiumEvilIntegration.DARK_GEM, "evilcraft:dark_block")
    }

    private fun addMaterialTag(prefix: HTTagPrefix, material: HTMaterialKey, value: String) {
        getOrCreateRawBuilder(prefix.commonTagKey)
            .addTag(prefix.createTag(material))

        getOrCreateRawBuilder(prefix.createTag(material))
            .addOptionalElement(ResourceLocation.parse(value))
    }

    //    Food    //

    private fun foodTags() {
        val foods: TagBuilder = getOrCreateRawBuilder(Tags.Items.FOODS)
        RagiumItems.FOODS.forEach { foodItem: DeferredItem<Item> ->
            if (foodItem.get().components().has(DataComponents.FOOD)) {
                foods.addElement(foodItem)
            }
        }

        getOrCreateRawBuilder(RagiumItemTags.DOUGH).addElement(RagiumItems.DOUGH)
    }

    //    Tool    //

    private fun toolTags() {
        getOrCreateRawBuilder(ItemTags.DURABILITY_ENCHANTABLE).addElement(RagiumItems.FORGE_HAMMER)

        getOrCreateRawBuilder(ItemTags.PICKAXES).addElement(RagiumItems.SILKY_PICKAXE)

        getOrCreateRawBuilder(
            itemTagKey(ResourceLocation.fromNamespaceAndPath("modern_industrialization", "forge_hammer_tools")),
        ).addElement(RagiumItems.FORGE_HAMMER)
    }

    //    Part    //

    private fun partTags() {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            getOrCreateRawBuilder(tier.getCircuitTag())
                .addElement(tier.getCircuit())
        }

        getOrCreateRawBuilder(ItemTags.COALS)
            .addElement(RagiumItems.RESIDUAL_COKE)
        getOrCreateRawBuilder(RagiumItemTags.COAL_COKE)
            .addElement(RagiumItems.COKE)
            .addOptionalElement(ResourceLocation.fromNamespaceAndPath(MI.ID, "coke"))

        getOrCreateRawBuilder(RagiumItemTags.PLASTICS)
            .addElement(RagiumItems.PLASTIC_PLATE)

        getOrCreateRawBuilder(RagiumItemTags.SOLAR_PANELS)
            .addElement(RagiumItems.SOLAR_PANEL)
            .addElement(GeneratorsItems.SOLAR_PANEL, true)

        getOrCreateRawBuilder(IndustrialTags.Items.MACHINE_FRAME_PITY)
            .addElement(RagiumBlocks.Casings.BASIC)
        getOrCreateRawBuilder(IndustrialTags.Items.MACHINE_FRAME_SIMPLE)
            .addElement(RagiumBlocks.Casings.ADVANCED)
        getOrCreateRawBuilder(IndustrialTags.Items.MACHINE_FRAME_ADVANCED)
            .addElement(RagiumBlocks.Casings.ELITE)
        getOrCreateRawBuilder(IndustrialTags.Items.MACHINE_FRAME_SUPREME)
            .addElement(RagiumBlocks.Casings.ULTIMATE)
    }
}
