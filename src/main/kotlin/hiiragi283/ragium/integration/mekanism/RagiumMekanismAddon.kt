package hiiragi283.ragium.integration.mekanism

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.collection.HTTable
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumEssenceType
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumFoods
import hiiragi283.ragium.setup.RagiumItems
import mekanism.api.chemical.Chemical
import mekanism.common.registration.impl.ChemicalDeferredRegister
import mekanism.common.registration.impl.DeferredChemical
import mekanism.common.registries.MekanismItems
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.registries.DeferredHolder

@HTAddon(RagiumConst.MEKANISM)
object RagiumMekanismAddon : RagiumAddon {
    //    Chemical    //

    @JvmField
    val CHEMICAL_REGISTER = ChemicalDeferredRegister(RagiumAPI.MOD_ID)

    @JvmField
    val CHEMICAL_MAP: Map<HTMaterialType, DeferredChemical<Chemical>> = buildMap {
        for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
            this[essenceType] = CHEMICAL_REGISTER.registerInfuse(essenceType.serializedName, essenceType.color.rgb)
        }

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            this[data.material] = CHEMICAL_REGISTER.register(data.molten.getPath(), data.color)
        }
    }

    @JvmStatic
    fun getChemical(material: HTMaterialType): DeferredChemical<Chemical> =
        CHEMICAL_MAP[material] ?: error("Unknown chemical for ${material.serializedName}")

    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmField
    val MATERIAL_ITEMS: HTTable<HTMaterialVariant.ItemTag, HTMaterialType, HTDeferredItem<*>> = buildTable {
        // Enriched
        for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
            this[HTMekMaterialVariant.ENRICHED, essenceType] = ITEM_REGISTER.registerSimpleItem("enriched_${essenceType.serializedName}")
        }
    }

    @JvmStatic
    fun getEnriched(material: HTMaterialType): DeferredHolder<Item, *> = when (material) {
        HTVanillaMaterialType.COAL -> MekanismItems.ENRICHED_CARBON
        HTVanillaMaterialType.REDSTONE -> MekanismItems.ENRICHED_REDSTONE
        HTVanillaMaterialType.DIAMOND -> MekanismItems.ENRICHED_DIAMOND
        HTVanillaMaterialType.OBSIDIAN -> MekanismItems.ENRICHED_OBSIDIAN
        HTVanillaMaterialType.GOLD -> MekanismItems.ENRICHED_GOLD
        else -> MATERIAL_ITEMS[HTMekMaterialVariant.ENRICHED, material]
            ?: error("Unknown enriched item for ${material.serializedName}")
    }

    @JvmStatic
    fun getEnrichedStack(material: HTMaterialType): ItemStack = getEnriched(material).let(::ItemStack)

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        eventBus.addListener(::buildCreativeTabs)
        eventBus.addListener(::modifyComponent)

        CHEMICAL_REGISTER.addAlias(RagiumAPI.id("raginite"), RagiumAPI.id("ragium"))

        CHEMICAL_REGISTER.register(eventBus)
        ITEM_REGISTER.register(eventBus)
    }

    private fun modifyComponent(event: ModifyDefaultComponentsEvent) {
        event.modify(MekanismItems.YELLOW_CAKE_URANIUM) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, RagiumFoods.YELLOW_CAKE)
        }
    }

    private fun buildCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        if (RagiumCreativeTabs.INGREDIENTS.`is`(event.tabKey)) {
            for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
                event.insertAfter(
                    RagiumItems.getMaterial(essenceType.baseVariant, essenceType.parent).toStack(),
                    getEnrichedStack(essenceType),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                )
            }
        }
    }
}
