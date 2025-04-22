package hiiragi283.ragium.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPropertyKeys
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.property.HTMutablePropertyMap
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.network.chat.Component
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Function

@HTAddon(RagiumAPI.MOD_ID)
object InternalRagiumAddon : RagiumAddon {
    override val priority: Int = 1000

    override fun onPrefixRegister(consumer: Consumer<HTTagPrefix>) {
        consumer.accept(HTTagPrefixes.DUST)
        consumer.accept(HTTagPrefixes.GEAR)
        consumer.accept(HTTagPrefixes.GEM)
        consumer.accept(HTTagPrefixes.INGOT)
        consumer.accept(HTTagPrefixes.NUGGET)
        consumer.accept(HTTagPrefixes.ORE)
        consumer.accept(HTTagPrefixes.PLATE)
        consumer.accept(HTTagPrefixes.RAW_MATERIAL)
        consumer.accept(HTTagPrefixes.ROD)
        consumer.accept(HTTagPrefixes.STORAGE_BLOCK)

        consumer.accept(HTTagPrefixes.DIRTY_DUST)
        consumer.accept(HTTagPrefixes.CLUMP)
        consumer.accept(HTTagPrefixes.SHARD)
        consumer.accept(HTTagPrefixes.CRYSTAL)
    }

    override fun onMaterialRegister(consumer: BiConsumer<HTMaterialKey, HTMaterialType>) {
        consumer.accept(CommonMaterials.ALUMINUM, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.ANTIMONY, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.ALUMINA, HTMaterialType.DEFAULT)
        consumer.accept(CommonMaterials.ASH, HTMaterialType.DEFAULT)
        consumer.accept(CommonMaterials.BAUXITE, HTMaterialType.MINERAL)
        consumer.accept(CommonMaterials.BERYLLIUM, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.BRASS, HTMaterialType.ALLOY)
        consumer.accept(CommonMaterials.BRONZE, HTMaterialType.ALLOY)
        consumer.accept(CommonMaterials.CADMIUM, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.CARBON, HTMaterialType.DEFAULT)
        consumer.accept(CommonMaterials.CHEESE, HTMaterialType.INGOT_LIKE)
        consumer.accept(CommonMaterials.CHOCOLATE, HTMaterialType.INGOT_LIKE)
        consumer.accept(CommonMaterials.CHROMIUM, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.COAL_COKE, HTMaterialType.GEM)
        consumer.accept(CommonMaterials.CONSTANTAN, HTMaterialType.ALLOY)
        consumer.accept(CommonMaterials.CRYOLITE, HTMaterialType.GEM)
        consumer.accept(CommonMaterials.ELECTRUM, HTMaterialType.ALLOY)
        consumer.accept(CommonMaterials.FLUORITE, HTMaterialType.GEM)
        consumer.accept(CommonMaterials.INVAR, HTMaterialType.ALLOY)
        consumer.accept(CommonMaterials.IRIDIUM, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.LEAD, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.NICKEL, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.NIOBIUM, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.OSMIUM, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.PERIDOT, HTMaterialType.GEM)
        consumer.accept(CommonMaterials.PLATINUM, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.PLUTONIUM, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.PYRITE, HTMaterialType.MINERAL)
        consumer.accept(CommonMaterials.RUBY, HTMaterialType.GEM)
        consumer.accept(CommonMaterials.SALT, HTMaterialType.MINERAL)
        consumer.accept(CommonMaterials.SALTPETER, HTMaterialType.MINERAL)
        consumer.accept(CommonMaterials.SAPPHIRE, HTMaterialType.GEM)
        consumer.accept(CommonMaterials.SILICON, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.SILVER, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.SOLDERING_ALLOY, HTMaterialType.ALLOY)
        consumer.accept(CommonMaterials.STAINLESS_STEEL, HTMaterialType.ALLOY)
        consumer.accept(CommonMaterials.STEEL, HTMaterialType.ALLOY)
        consumer.accept(CommonMaterials.SULFUR, HTMaterialType.MINERAL)
        consumer.accept(CommonMaterials.SUPERCONDUCTOR, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.TIN, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.TITANIUM, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.TUNGSTEN, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.URANIUM, HTMaterialType.METAL)
        consumer.accept(CommonMaterials.ZINC, HTMaterialType.METAL)

        consumer.accept(RagiumMaterials.ADVANCED_RAGI_ALLOY, HTMaterialType.ALLOY)
        consumer.accept(RagiumMaterials.AZURE_STEEL, HTMaterialType.ALLOY)
        consumer.accept(RagiumMaterials.CRIMSON_CRYSTAL, HTMaterialType.GEM)
        consumer.accept(RagiumMaterials.DEEP_STEEL, HTMaterialType.ALLOY)
        consumer.accept(RagiumMaterials.RAGI_ALLOY, HTMaterialType.ALLOY)
        consumer.accept(RagiumMaterials.RAGI_CRYSTAL, HTMaterialType.GEM)
        consumer.accept(RagiumMaterials.RAGINITE, HTMaterialType.MINERAL)
        consumer.accept(RagiumMaterials.WARPED_CRYSTAL, HTMaterialType.GEM)

        consumer.accept(VanillaMaterials.AMETHYST, HTMaterialType.GEM)
        consumer.accept(VanillaMaterials.CALCITE, HTMaterialType.DEFAULT)
        consumer.accept(VanillaMaterials.COAL, HTMaterialType.GEM)
        consumer.accept(VanillaMaterials.COPPER, HTMaterialType.METAL)
        consumer.accept(VanillaMaterials.DIAMOND, HTMaterialType.GEM)
        consumer.accept(VanillaMaterials.EMERALD, HTMaterialType.GEM)
        consumer.accept(VanillaMaterials.ENDER_PEARL, HTMaterialType.GEM)
        consumer.accept(VanillaMaterials.GLOWSTONE, HTMaterialType.DEFAULT)
        consumer.accept(VanillaMaterials.GOLD, HTMaterialType.METAL)
        consumer.accept(VanillaMaterials.IRON, HTMaterialType.METAL)
        consumer.accept(VanillaMaterials.LAPIS, HTMaterialType.GEM)
        consumer.accept(VanillaMaterials.NETHERITE, HTMaterialType.ALLOY)
        consumer.accept(VanillaMaterials.NETHERITE_SCRAP, HTMaterialType.GEM)
        consumer.accept(VanillaMaterials.OBSIDIAN, HTMaterialType.DEFAULT)
        consumer.accept(VanillaMaterials.QUARTZ, HTMaterialType.GEM)
        consumer.accept(VanillaMaterials.REDSTONE, HTMaterialType.MINERAL)
        consumer.accept(VanillaMaterials.WOOD, HTMaterialType.DEFAULT)

        consumer.accept(IntegrationMaterials.BLACK_QUARTZ, HTMaterialType.GEM)

        consumer.accept(IntegrationMaterials.CERTUS_QUARTZ, HTMaterialType.GEM)
        consumer.accept(IntegrationMaterials.FLUIX, HTMaterialType.GEM)

        consumer.accept(IntegrationMaterials.ANDESITE_ALLOY, HTMaterialType.ALLOY)
        consumer.accept(IntegrationMaterials.CARDBOARD, HTMaterialType.ALLOY)
        consumer.accept(IntegrationMaterials.ROSE_QUARTZ, HTMaterialType.GEM)

        consumer.accept(IntegrationMaterials.COPPER_ALLOY, HTMaterialType.ALLOY)
        consumer.accept(IntegrationMaterials.ENERGETIC_ALLOY, HTMaterialType.ALLOY)
        consumer.accept(IntegrationMaterials.VIBRANT_ALLOY, HTMaterialType.ALLOY)
        consumer.accept(IntegrationMaterials.REDSTONE_ALLOY, HTMaterialType.ALLOY)
        consumer.accept(IntegrationMaterials.CONDUCTIVE_ALLOY, HTMaterialType.ALLOY)
        consumer.accept(IntegrationMaterials.PULSATING_ALLOY, HTMaterialType.ALLOY)
        consumer.accept(IntegrationMaterials.DARK_STEEL, HTMaterialType.ALLOY)
        consumer.accept(IntegrationMaterials.SOULARIUM, HTMaterialType.ALLOY)
        consumer.accept(IntegrationMaterials.END_STEEL, HTMaterialType.ALLOY)
        consumer.accept(IntegrationMaterials.PULSATING_CRYSTAL, HTMaterialType.GEM)
        consumer.accept(IntegrationMaterials.VIBRANT_CRYSTAL, HTMaterialType.GEM)
        consumer.accept(IntegrationMaterials.ENDER_CRYSTAL, HTMaterialType.GEM)
        consumer.accept(IntegrationMaterials.ENTICING_CRYSTAL, HTMaterialType.GEM)
        consumer.accept(IntegrationMaterials.WEATHER_CRYSTAL, HTMaterialType.GEM)
        consumer.accept(IntegrationMaterials.PRESCIENT_CRYSTAL, HTMaterialType.GEM)

        consumer.accept(IntegrationMaterials.DARK_GEM, HTMaterialType.GEM)

        consumer.accept(IntegrationMaterials.HOP_GRAPHITE, HTMaterialType.ALLOY)

        consumer.accept(IntegrationMaterials.REFINED_GLOWSTONE, HTMaterialType.ALLOY)
        consumer.accept(IntegrationMaterials.REFINED_OBSIDIAN, HTMaterialType.ALLOY)

        consumer.accept(IntegrationMaterials.CARMINITE, HTMaterialType.GEM)
        consumer.accept(IntegrationMaterials.FIERY_METAL, HTMaterialType.ALLOY)
        consumer.accept(IntegrationMaterials.IRONWOOD, HTMaterialType.METAL)
        consumer.accept(IntegrationMaterials.KNIGHTMETAL, HTMaterialType.METAL)
        consumer.accept(IntegrationMaterials.STEELEAF, HTMaterialType.METAL)
    }

    override fun onMaterialSetup(getter: Function<HTMaterialKey, HTMutablePropertyMap>) {
        getter.apply(VanillaMaterials.WOOD)[HTMaterialPropertyKeys.getNameKey(HTTagPrefixes.DUST)] =
            Component.translatable(RagiumTranslationKeys.TEXT_SAWDUST)

        getter.apply(VanillaMaterials.LAPIS)[HTMaterialPropertyKeys.ORE_CRUSHED_COUNT] = 8
        getter.apply(VanillaMaterials.REDSTONE)[HTMaterialPropertyKeys.ORE_CRUSHED_COUNT] = 12
    }
}
