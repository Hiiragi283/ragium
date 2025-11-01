package hiiragi283.ragium.common.integration

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.common.material.RagiumEssenceType
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.setup.MekanismMaterialPrefixes
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumFoods
import hiiragi283.ragium.setup.RagiumItems
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
    val CHEMICAL_MAP: Map<HTMaterialKey, DeferredChemical<*>> = buildMap {
        for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
            val key: HTMaterialKey = essenceType.asMaterialKey()
            this[key] = CHEMICAL_REGISTER.registerInfuse(key.name, essenceType.color.rgb)
        }

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            this[data.asMaterialKey()] = CHEMICAL_REGISTER.register(data.molten.getPath(), data.color)
        }
    }

    @JvmStatic
    fun getChemical(material: HTMaterialLike): DeferredChemical<*> =
        CHEMICAL_MAP[material.asMaterialKey()] ?: error("Unknown chemical for ${material.asMaterialName()}")

    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    val MATERIAL_ITEMS: ImmutableTable<HTMaterialPrefix, HTMaterialKey, HTSimpleDeferredItem> = buildTable {
        // Enriched
        for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
            val key: HTMaterialKey = essenceType.asMaterialKey()
            this[MekanismMaterialPrefixes.ENRICHED, key] = ITEM_REGISTER.registerSimpleItem("enriched_${key.name}")
        }
    }

    @JvmStatic
    fun getEnriched(material: HTMaterialLike): HTItemHolderLike = when (val key: HTMaterialKey = material.asMaterialKey()) {
        VanillaMaterialKeys.COAL -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_CARBON)
        VanillaMaterialKeys.REDSTONE -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_REDSTONE)
        VanillaMaterialKeys.DIAMOND -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_DIAMOND)
        VanillaMaterialKeys.OBSIDIAN -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_OBSIDIAN)
        VanillaMaterialKeys.GOLD -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_GOLD)
        else -> MATERIAL_ITEMS[MekanismMaterialPrefixes.ENRICHED, key] ?: error("Unknown enriched item for ${key.name}")
    }

    @JvmStatic
    fun getEnrichedStack(material: HTMaterialLike, count: Int = 1): ItemStack = getEnriched(material.asMaterialKey()).toStack(count)

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        CHEMICAL_REGISTER.addAlias(RagiumAPI.id("raginite"), RagiumAPI.id("ragium"))

        CHEMICAL_REGISTER.register(eventBus)
        ITEM_REGISTER.register(eventBus)

        MekanismMaterialPrefixes.REGISTER.register(eventBus)
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
                    RagiumItems.getMaterial(essenceType.basePrefix, essenceType.parent).toStack(),
                    getEnrichedStack(essenceType),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                )
            }
        }
    }
}
