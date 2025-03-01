package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.event.HTRegisterMaterialEvent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import net.neoforged.fml.ModLoader
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

internal object HTMaterialRegistryImpl : HTMaterialRegistry {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmStatic
    private lateinit var typeMap: Map<HTMaterialKey, HTMaterialType>

    //    HTMaterialRegistry    //

    override val keys: Set<HTMaterialKey> get() = typeMap.keys

    override fun getType(key: HTMaterialKey): HTMaterialType = typeMap[key] ?: error("Unknown material key: $key")

    //    Init    //

    init {
        MOD_BUS.addListener(::registerMaterial)
    }

    @JvmStatic
    fun initRegistry() {
        registerMaterials()
        // modifyProperties()
        LOGGER.info("Loaded material registry!")
    }

    @JvmStatic
    private fun registerMaterials() {
        LOGGER.info("Invoke material events...")
        val typeCache: MutableMap<HTMaterialKey, HTMaterialType> = mutableMapOf()

        ModLoader.postEvent(
            HTRegisterMaterialEvent { key: HTMaterialKey, type: HTMaterialType ->
                check(typeCache.put(key, type) == null) { "Duplicated material registration: ${key.name}" }
            },
        )

        this.typeMap = typeCache
        LOGGER.info("Registered new materials!")
    }

    @JvmStatic
    private fun registerMaterial(event: HTRegisterMaterialEvent) {
        event.register(CommonMaterials.ALUMINUM, HTMaterialType.METAL)
        event.register(CommonMaterials.ANTIMONY, HTMaterialType.METAL)
        event.register(CommonMaterials.BERYLLIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.ASH, HTMaterialType.DUST)
        event.register(CommonMaterials.BAUXITE, HTMaterialType.MINERAL)
        event.register(CommonMaterials.BRASS, HTMaterialType.ALLOY)
        event.register(CommonMaterials.BRONZE, HTMaterialType.ALLOY)
        event.register(CommonMaterials.CADMIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.CALCITE, HTMaterialType.DUST)
        event.register(CommonMaterials.CARBON, HTMaterialType.DUST)
        event.register(CommonMaterials.CHROMIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.COAL_COKE, HTMaterialType.GEM)
        event.register(CommonMaterials.CONSTANTAN, HTMaterialType.ALLOY)
        event.register(CommonMaterials.CRYOLITE, HTMaterialType.GEM)
        event.register(CommonMaterials.ELECTRUM, HTMaterialType.ALLOY)
        event.register(CommonMaterials.FLUORITE, HTMaterialType.GEM)
        event.register(CommonMaterials.INVAR, HTMaterialType.ALLOY)
        event.register(CommonMaterials.IRIDIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.LEAD, HTMaterialType.METAL)
        event.register(CommonMaterials.NICKEL, HTMaterialType.METAL)
        event.register(CommonMaterials.NIOBIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.OSMIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.PERIDOT, HTMaterialType.GEM)
        event.register(CommonMaterials.PLATINUM, HTMaterialType.METAL)
        event.register(CommonMaterials.PLUTONIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.PYRITE, HTMaterialType.MINERAL)
        event.register(CommonMaterials.RUBY, HTMaterialType.GEM)
        event.register(CommonMaterials.SALT, HTMaterialType.MINERAL)
        event.register(CommonMaterials.SALTPETER, HTMaterialType.MINERAL)
        event.register(CommonMaterials.SAPPHIRE, HTMaterialType.GEM)
        event.register(CommonMaterials.SILICON, HTMaterialType.METAL)
        event.register(CommonMaterials.SILVER, HTMaterialType.METAL)
        event.register(CommonMaterials.SOLDERING_ALLOY, HTMaterialType.ALLOY)
        event.register(CommonMaterials.STAINLESS_STEEL, HTMaterialType.ALLOY)
        event.register(CommonMaterials.STEEL, HTMaterialType.ALLOY)
        event.register(CommonMaterials.SULFUR, HTMaterialType.MINERAL)
        event.register(CommonMaterials.SUPERCONDUCTOR, HTMaterialType.METAL)
        event.register(CommonMaterials.TIN, HTMaterialType.METAL)
        event.register(CommonMaterials.TITANIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.TUNGSTEN, HTMaterialType.METAL)
        event.register(CommonMaterials.URANIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.WOOD, HTMaterialType.DUST)
        event.register(CommonMaterials.ZINC, HTMaterialType.METAL)

        event.register(RagiumMaterials.CRIMSON_CRYSTAL, HTMaterialType.GEM)
        event.register(RagiumMaterials.DEEP_STEEL, HTMaterialType.ALLOY)
        event.register(RagiumMaterials.DURALUMIN, HTMaterialType.ALLOY)
        event.register(RagiumMaterials.EMBER_ALLOY, HTMaterialType.ALLOY)
        event.register(RagiumMaterials.FIERY_COAL, HTMaterialType.GEM)
        event.register(RagiumMaterials.RAGI_ALLOY, HTMaterialType.ALLOY)
        event.register(RagiumMaterials.RAGI_CRYSTAL, HTMaterialType.GEM)
        event.register(RagiumMaterials.RAGINITE, HTMaterialType.MINERAL)
        event.register(RagiumMaterials.RAGIUM, HTMaterialType.METAL)
        event.register(RagiumMaterials.WARPED_CRYSTAL, HTMaterialType.GEM)

        event.register(VanillaMaterials.AMETHYST, HTMaterialType.GEM)
        event.register(VanillaMaterials.COAL, HTMaterialType.GEM)
        event.register(VanillaMaterials.COPPER, HTMaterialType.METAL)
        event.register(VanillaMaterials.DIAMOND, HTMaterialType.GEM)
        event.register(VanillaMaterials.EMERALD, HTMaterialType.GEM)
        event.register(VanillaMaterials.GLOWSTONE, HTMaterialType.DUST)
        event.register(VanillaMaterials.GOLD, HTMaterialType.METAL)
        event.register(VanillaMaterials.IRON, HTMaterialType.METAL)
        event.register(VanillaMaterials.LAPIS, HTMaterialType.GEM)
        event.register(VanillaMaterials.NETHERITE, HTMaterialType.ALLOY)
        event.register(VanillaMaterials.NETHERITE_SCRAP, HTMaterialType.GEM)
        event.register(VanillaMaterials.OBSIDIAN, HTMaterialType.DUST)
        event.register(VanillaMaterials.QUARTZ, HTMaterialType.GEM)
        event.register(VanillaMaterials.REDSTONE, HTMaterialType.MINERAL)

        event.register(IntegrationMaterials.BLACK_QUARTZ, HTMaterialType.GEM)

        event.register(IntegrationMaterials.ANDESITE_ALLOY, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.CARDBOARD, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.ROSE_QUARTZ, HTMaterialType.GEM)

        event.register(IntegrationMaterials.COPPER_ALLOY, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.ENERGETIC_ALLOY, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.VIBRANT_ALLOY, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.REDSTONE_ALLOY, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.CONDUCTIVE_ALLOY, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.PULSATING_ALLOY, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.DARK_STEEL, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.SOULARIUM, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.END_STEEL, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.PULSATING_CRYSTAL, HTMaterialType.GEM)
        event.register(IntegrationMaterials.VIBRANT_CRYSTAL, HTMaterialType.GEM)
        event.register(IntegrationMaterials.ENDER_CRYSTAL, HTMaterialType.GEM)
        event.register(IntegrationMaterials.ENTICING_CRYSTAL, HTMaterialType.GEM)
        event.register(IntegrationMaterials.WEATHER_CRYSTAL, HTMaterialType.GEM)
        event.register(IntegrationMaterials.PRESCIENT_CRYSTAL, HTMaterialType.GEM)

        event.register(IntegrationMaterials.DARK_GEM, HTMaterialType.GEM)

        event.register(IntegrationMaterials.REFINED_GLOWSTONE, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.REFINED_OBSIDIAN, HTMaterialType.ALLOY)

        event.register(IntegrationMaterials.CARMINITE, HTMaterialType.GEM)
        event.register(IntegrationMaterials.FIERY_METAL, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.IRONWOOD, HTMaterialType.METAL)
        event.register(IntegrationMaterials.KNIGHTMETAL, HTMaterialType.METAL)
        event.register(IntegrationMaterials.STEELEAF, HTMaterialType.METAL)
    }
}
