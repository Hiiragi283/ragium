package hiiragi283.ragium.common

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.RagiumIMC
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.internal.DefaultMachinePlugin
import hiiragi283.ragium.common.internal.DefaultMaterialPlugin
import hiiragi283.ragium.common.internal.HTMaterialRegistryImpl
import hiiragi283.ragium.common.internal.InternalRagiumAPI
import hiiragi283.ragium.integration.mek.RagiumMekPlugin
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent
import org.slf4j.Logger

@Mod(RagiumAPI.MOD_ID)
class RagiumCommon(eventBus: IEventBus, container: ModContainer) {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        container.registerExtensionPoint(RagiumPlugin.Provider::class.java) {
            RagiumPlugin.Provider {
                listOf(
                    DefaultMachinePlugin,
                    DefaultMaterialPlugin,
                    RagiumMekPlugin,
                )
            }
        }

        eventBus.addListener(::commonSetup)
        eventBus.addListener(::sendMessage)
        eventBus.addListener(HTMaterialRegistryImpl::processMessage)

        RagiumComponentTypes.REGISTER.register(eventBus)

        InternalRagiumAPI.collectPlugins()
        InternalRagiumAPI.registerMachines()

        RagiumFluids.register(eventBus)
        RagiumBlocks.register(eventBus)
        RagiumItems.register(eventBus)

        RagiumBlockEntityTypes.REGISTER.register(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)
        RagiumRecipes.SERIALIZER.register(eventBus)
        RagiumRecipes.TYPE.register(eventBus)

        container.registerConfig(ModConfig.Type.STARTUP, RagiumConfig.SPEC)

        LOGGER.info("Ragium loaded!")
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        LOGGER.info("Loaded common setup!")
    }

    private fun sendMessage(event: InterModEnqueueEvent) {
        // Alloy
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.DEEP_STEEL, HTMaterialType.ALLOY)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.NETHERITE, HTMaterialType.ALLOY)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.RAGI_ALLOY, HTMaterialType.ALLOY)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.RAGI_STEEL, HTMaterialType.ALLOY)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.REFINED_RAGI_STEEL, HTMaterialType.ALLOY)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.STEEL, HTMaterialType.ALLOY)

        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.BRASS, HTMaterialType.ALLOY)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.BRONZE, HTMaterialType.ALLOY)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.ELECTRUM, HTMaterialType.ALLOY)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.INVAR, HTMaterialType.ALLOY)
        // Dust
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.ALKALI, HTMaterialType.DUST)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.ASH, HTMaterialType.DUST)
        // Gem
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.AMETHYST, HTMaterialType.GEM)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.CINNABAR, HTMaterialType.GEM)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.COAL, HTMaterialType.GEM)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.CRYOLITE, HTMaterialType.GEM)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.DIAMOND, HTMaterialType.GEM)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.EMERALD, HTMaterialType.GEM)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.FLUORITE, HTMaterialType.GEM)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.LAPIS, HTMaterialType.GEM)

        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.NETHERITE_SCRAP, HTMaterialType.GEM)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.PERIDOT, HTMaterialType.GEM)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.QUARTZ, HTMaterialType.GEM)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.RAGI_CRYSTAL, HTMaterialType.GEM)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.RUBY, HTMaterialType.GEM)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.SAPPHIRE, HTMaterialType.GEM)
        // Metal
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.ALUMINUM, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.COPPER, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.GOLD, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.IRON, HTMaterialType.METAL)

        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.RAGIUM, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.ECHORIUM, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.FIERIUM, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.DRAGONIUM, HTMaterialType.METAL)

        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.IRIDIUM, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.LEAD, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.NICKEL, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.PLATINUM, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.PLUTONIUM, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.SILVER, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.TIN, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.TITANIUM, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.TUNGSTEN, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.URANIUM, HTMaterialType.METAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.ZINC, HTMaterialType.METAL)
        // Mineral
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.BAUXITE, HTMaterialType.MINERAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.CRUDE_RAGINITE, HTMaterialType.MINERAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.NITER, HTMaterialType.MINERAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.RAGINITE, HTMaterialType.MINERAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.REDSTONE, HTMaterialType.MINERAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.SALT, HTMaterialType.MINERAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.SULFUR, HTMaterialType.MINERAL)

        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.GALENA, HTMaterialType.MINERAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.PYRITE, HTMaterialType.MINERAL)
        RagiumIMC.sendMaterialIMC(RagiumMaterialKeys.SPHALERITE, HTMaterialType.MINERAL)

        fun <T> bindContents(contents: List<T>) where T : ItemLike, T : HTMaterialProvider {
            contents.forEach { RagiumIMC.sendMaterialItemIMC(it.tagPrefix, it.material, it) }
        }

        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.COAL, Items.DEEPSLATE_COAL_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.COPPER, Items.DEEPSLATE_COPPER_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.DIAMOND, Items.DEEPSLATE_DIAMOND_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.EMERALD, Items.DEEPSLATE_EMERALD_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.GOLD, Items.DEEPSLATE_GOLD_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.IRON, Items.DEEPSLATE_IRON_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.LAPIS, Items.DEEPSLATE_LAPIS_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.REDSTONE, Items.DEEPSLATE_REDSTONE_ORE)

        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.DUST, RagiumMaterialKeys.REDSTONE, Items.REDSTONE)

        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.GEM, RagiumMaterialKeys.AMETHYST, Items.AMETHYST_SHARD)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.GEM, RagiumMaterialKeys.COAL, Items.COAL)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.GEM, RagiumMaterialKeys.DIAMOND, Items.DIAMOND)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.GEM, RagiumMaterialKeys.EMERALD, Items.EMERALD)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.GEM, RagiumMaterialKeys.LAPIS, Items.LAPIS_LAZULI)
        // RagiumIMC.sendMaterialItemIMC(HTTagPrefix.GEM, RagiumMaterialKeys.NETHER_STAR, Items.NETHER_STAR)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.GEM, RagiumMaterialKeys.NETHERITE_SCRAP, Items.NETHERITE_SCRAP)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.GEM, RagiumMaterialKeys.QUARTZ, Items.QUARTZ)

        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER, Items.COPPER_INGOT)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.INGOT, RagiumMaterialKeys.GOLD, Items.GOLD_INGOT)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON, Items.IRON_INGOT)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.INGOT, RagiumMaterialKeys.NETHERITE, Items.NETHERITE_INGOT)

        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.NUGGET, RagiumMaterialKeys.GOLD, Items.GOLD_NUGGET)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.NUGGET, RagiumMaterialKeys.IRON, Items.IRON_NUGGET)

        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.COAL, Items.COAL_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.COPPER, Items.COPPER_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.DIAMOND, Items.DIAMOND_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.EMERALD, Items.EMERALD_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.GOLD, Items.GOLD_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.IRON, Items.IRON_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.LAPIS, Items.LAPIS_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.NETHERITE_SCRAP, Items.ANCIENT_DEBRIS)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.QUARTZ, Items.NETHER_QUARTZ_ORE)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, RagiumMaterialKeys.REDSTONE, Items.REDSTONE_ORE)

        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.COPPER, Items.RAW_COPPER)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.GOLD, Items.RAW_GOLD)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.IRON, Items.RAW_IRON)

        // RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ROD, RagiumMaterialKeys.WOOD, Items.STICK)

        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.COAL, Items.COAL_BLOCK)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.COPPER, Items.COPPER_BLOCK)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.DIAMOND, Items.DIAMOND_BLOCK)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.EMERALD, Items.EMERALD_BLOCK)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.GOLD, Items.GOLD_BLOCK)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.IRON, Items.IRON_BLOCK)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.LAPIS, Items.LAPIS_BLOCK)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.NETHERITE, Items.NETHERITE_BLOCK)

        // bindContents(RagiumBlocks.Ores.entries)
        bindContents(RagiumBlocks.StorageBlocks.entries)
        bindContents(RagiumItems.Dusts.entries)
        bindContents(RagiumItems.Gears.entries)
        bindContents(RagiumItems.Gems.entries)
        bindContents(RagiumItems.Ingots.entries)
        // bindContents(RagiumItems.Plates.entries)
        bindContents(RagiumItems.RawMaterials.entries)

        LOGGER.info("Sent IMC Messages!")
    }
}
