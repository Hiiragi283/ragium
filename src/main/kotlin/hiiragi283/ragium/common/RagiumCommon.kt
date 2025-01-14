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
                )
            }
        }

        eventBus.addListener(::commonSetup)
        eventBus.addListener(::sendMessage)
        eventBus.addListener(HTMaterialRegistryImpl::processMessage)

        RagiumComponentTypes.REGISTER.register(eventBus)

        InternalRagiumAPI.collectPlugins()
        InternalRagiumAPI.registerMachines()

        RagiumFluids.TYPE_REGISTER.register(eventBus)
        RagiumFluids.REGISTER.register(eventBus)
        RagiumBlocks.REGISTER.register(eventBus)
        RagiumItems.REGISTER.register(eventBus)

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
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.DEEP_STEEL, HTMaterialType.ALLOY)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.NETHERITE, HTMaterialType.ALLOY)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.RAGI_ALLOY, HTMaterialType.ALLOY)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.RAGI_STEEL, HTMaterialType.ALLOY)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.REFINED_RAGI_STEEL, HTMaterialType.ALLOY)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.STEEL, HTMaterialType.ALLOY)

        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.BRASS, HTMaterialType.ALLOY)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.BRONZE, HTMaterialType.ALLOY)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.ELECTRUM, HTMaterialType.ALLOY)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.INVAR, HTMaterialType.ALLOY)
        // Dust
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.ALKALI, HTMaterialType.DUST)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.ASH, HTMaterialType.DUST)
        // Gem
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.AMETHYST, HTMaterialType.GEM)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.CINNABAR, HTMaterialType.GEM)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.COAL, HTMaterialType.GEM)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.CRYOLITE, HTMaterialType.GEM)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.DIAMOND, HTMaterialType.GEM)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.EMERALD, HTMaterialType.GEM)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.FLUORITE, HTMaterialType.GEM)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.LAPIS, HTMaterialType.GEM)

        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.NETHERITE_SCRAP, HTMaterialType.GEM)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.PERIDOT, HTMaterialType.GEM)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.QUARTZ, HTMaterialType.GEM)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.RAGI_CRYSTAL, HTMaterialType.GEM)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.RUBY, HTMaterialType.GEM)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.SAPPHIRE, HTMaterialType.GEM)
        // Metal
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.ALUMINUM, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.COPPER, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.GOLD, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.IRON, HTMaterialType.METAL)

        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.RAGIUM, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.ECHORIUM, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.FIERIUM, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.DRAGONIUM, HTMaterialType.METAL)

        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.IRIDIUM, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.LEAD, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.NICKEL, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.PLATINUM, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.PLUTONIUM, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.SILVER, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.TIN, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.TITANIUM, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.TUNGSTEN, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.URANIUM, HTMaterialType.METAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.ZINC, HTMaterialType.METAL)
        // Mineral
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.BAUXITE, HTMaterialType.MINERAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.CRUDE_RAGINITE, HTMaterialType.MINERAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.NITER, HTMaterialType.MINERAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.RAGINITE, HTMaterialType.MINERAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.REDSTONE, HTMaterialType.MINERAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.SALT, HTMaterialType.MINERAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.SULFUR, HTMaterialType.MINERAL)

        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.GALENA, HTMaterialType.MINERAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.PYRITE, HTMaterialType.MINERAL)
        RagiumIMC.sendNewMaterialIMC(RagiumMaterialKeys.SPHALERITE, HTMaterialType.MINERAL)

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
