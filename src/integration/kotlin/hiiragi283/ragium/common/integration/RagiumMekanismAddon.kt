package hiiragi283.ragium.common.integration

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.common.material.HTMekMaterialVariant
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
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

object RagiumMekanismAddon : RagiumAddon {
    //    Chemical    //

    @JvmField
    val CHEMICAL_REGISTER = ChemicalDeferredRegister(RagiumAPI.MOD_ID)

    @JvmField
    val CHEMICAL_MAP: Map<HTMaterialType, DeferredChemical<Chemical>> = buildMap {
        for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
            this[essenceType] = CHEMICAL_REGISTER.registerInfuse(essenceType.materialName(), essenceType.color.rgb)
        }

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            this[data.material] = CHEMICAL_REGISTER.register(data.molten.getPath(), data.color)
        }
    }

    @JvmStatic
    fun getChemical(material: HTMaterialType): DeferredChemical<Chemical> =
        CHEMICAL_MAP[material] ?: error("Unknown chemical for ${material.materialName()}")

    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    val MATERIAL_ITEMS: ImmutableTable<HTMaterialVariant.ItemTag, HTMaterialType, HTDeferredItem<*>> = buildTable {
        // Enriched
        for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
            this[HTMekMaterialVariant.ENRICHED, essenceType] = ITEM_REGISTER.registerSimpleItem("enriched_${essenceType.materialName()}")
        }
    }

    @JvmStatic
    fun getEnriched(material: HTMaterialType): HTItemHolderLike = when (material) {
        HTVanillaMaterialType.COAL -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_CARBON)
        HTVanillaMaterialType.REDSTONE -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_REDSTONE)
        HTVanillaMaterialType.DIAMOND -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_DIAMOND)
        HTVanillaMaterialType.OBSIDIAN -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_OBSIDIAN)
        HTVanillaMaterialType.GOLD -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_GOLD)
        else -> MATERIAL_ITEMS[HTMekMaterialVariant.ENRICHED, material]
            ?: error("Unknown enriched item for ${material.materialName()}")
    }

    @JvmStatic
    fun getEnrichedStack(material: HTMaterialType, count: Int = 1): ItemStack = getEnriched(material).toStack(count)

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        CHEMICAL_REGISTER.addAlias(RagiumAPI.id("raginite"), RagiumAPI.id("ragium"))

        CHEMICAL_REGISTER.register(eventBus)
        ITEM_REGISTER.register(eventBus)
    }

    override fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        event.modify(MekanismItems.YELLOW_CAKE_URANIUM) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, RagiumFoods.YELLOW_CAKE)
        }
    }

    override fun buildCreativeTabs(helper: RagiumAddon.CreativeTabHelper) {
        helper.ifMatchTab(RagiumCreativeTabs.INGREDIENTS) { event: BuildCreativeModeTabContentsEvent ->
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
