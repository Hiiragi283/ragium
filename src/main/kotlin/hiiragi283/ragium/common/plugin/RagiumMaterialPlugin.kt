package hiiragi283.ragium.common.plugin

import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartKey
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.part.property.addNamePattern
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialLevel
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.material.property.addBlockPrefixes
import hiiragi283.core.api.material.property.addItemPrefixes
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.material.property.setTextureSet
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.plugin.HTPlugin
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.property.RagiumMaterialPropertyKeys
import hiiragi283.ragium.api.material.property.setMolten
import hiiragi283.ragium.api.tag.RagiumTagPrefixes
import hiiragi283.ragium.common.data.RagiumDynamicServerResources
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.part.RagiumParts
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.fluids.FluidType

@HTPlugin
data object RagiumMaterialPlugin : HTMaterialPlugin {
    override val priority: Int = -1000

    override fun getId(): ResourceLocation = RagiumAPI.id("material_plugin")

    override fun registerPart(registrar: HTMaterialPlugin.PartRegistrar) {
        registrar.register(RagiumParts.PELLET, "%s_pellet") {
            put(HTPartPropertyKeys.TAG_PREFIX, RagiumTagPrefixes.PELLET)

            addNamePattern("%s Pellet", "%sペレット")
        }
    }

    override fun registerExistingItem(consumer: HTMaterialPlugin.ItemConsumer) {
        consumer.accept(CommonParts.DUST, RagiumMaterialKeys.MEAT, RagiumItems.MINCED_MEAT)
        consumer.accept(CommonParts.INGOT, RagiumMaterialKeys.MEAT, RagiumItems.MEAT_INGOT)

        consumer.accept(CommonParts.INGOT, RagiumMaterialKeys.COOKED_MEAT, RagiumItems.COOKED_MEAT_INGOT)
    }

    override fun modifyMaterial(provider: HTMaterialPlugin.MaterialProvider) {
        fuel(provider)
        mineral(provider)
        gem(provider)
        alloy(provider)
        other(provider)

        existing(provider)
    }

    private val materialBlockSet: Set<HTPartKey> = setOf(
        CommonParts.ORE,
        CommonParts.ORE_DEEPSLATE,
        CommonParts.ORE_NETHER,
        CommonParts.ORE_END,
        CommonParts.RAW_BLOCK,
    )

    @JvmStatic
    private fun fuel(provider: HTMaterialPlugin.MaterialProvider) {
        provider.getBuilder(RagiumMaterialKeys.PETROLEUM_COKE).apply {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(CommonParts.DUST, CommonParts.FUEL, CommonParts.TINY)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)

            setName("Petroleum Coke", "石油コークス")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 6)
        }
    }

    @JvmStatic
    private fun mineral(provider: HTMaterialPlugin.MaterialProvider) {
        provider.getBuilder(RagiumMaterialKeys.RAGINITE).apply {
            addBlockPrefixes(materialBlockSet)
            addItemPrefixes(CommonParts.DUST, CommonParts.RAW, CommonParts.CRUSHED_ORE)
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(3))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)
            setMolten(RagiumFluids.MOLTEN_RAGINITE)

            setName("Raginite", "ラギナイト")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
        }
        provider.getBuilder(RagiumMaterialKeys.BORAX).apply {
            addItemPrefixes(CommonParts.DUST)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)

            setName("Borax", "ホウ砂")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
        }
    }

    @JvmStatic
    private fun gem(provider: HTMaterialPlugin.MaterialProvider) {
        provider.getBuilder(RagiumMaterialKeys.RAGI_CRYSTAL).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(CommonParts.DUST, CommonParts.GEM)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)

            setName("Ragi-Crystal", "ラギクリスタル")
            setTextureSet("diamond", HTMaterialTextureSet.SHINE)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, RagiumMaterialKeys.RAGINITE.getId())
        }
    }

    @JvmStatic
    private fun alloy(provider: HTMaterialPlugin.MaterialProvider) {
        val alloySet: Set<HTPartKey> = setOf(
            CommonParts.DUST,
            CommonParts.INGOT,
            CommonParts.NUGGET,
            CommonParts.GEAR,
            CommonParts.PLATE,
            CommonParts.ROD,
            CommonParts.WIRE,
        )
        provider.getBuilder(RagiumMaterialKeys.RAGI_ALLOY).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(alloySet)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)

            setName("Ragi-Alloy", "ラギ合金")
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, RagiumAPI.id("raginite"))
        }
        provider.getBuilder(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(alloySet)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)

            setName("Advanced Ragi-Alloy", "発展ラギ合金")
        }
        provider.getBuilder(RagiumMaterialKeys.STAINLESS_STEEL).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(alloySet.minus(CommonParts.WIRE))
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.HIGH)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.HIGH)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)
            setMolten(RagiumFluids.MOLTEN_STAINLESS_STEEL)

            setName("Stainless Steel", "ステンレス鋼")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
    }

    @JvmStatic
    private fun other(provider: HTMaterialPlugin.MaterialProvider) {}

    @JvmStatic
    private fun existing(provider: HTMaterialPlugin.MaterialProvider) {
        provider.getBuilder(VanillaMaterialKeys.WOOD).addItemPrefixes(RagiumParts.PELLET)

        provider.getBuilder(CommonMaterialKeys.SILICON).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)

            addItemPrefixes(CommonParts.DUST, CommonParts.INGOT, CommonParts.PLATE)
        }
        // Molten Fluid
        provider.getBuilder(VanillaMaterialKeys.GLASS).apply {
            put(RagiumMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT, FluidType.BUCKET_VOLUME)
            setMolten(HCFluids.MOLTEN_GLASS)
        }

        provider.getBuilder(VanillaMaterialKeys.ENDER).setMolten(HCFluids.MOLTEN_ENDER)
        provider.getBuilder(VanillaMaterialKeys.BLAZE).setMolten(HCFluids.MOLTEN_BLAZE)

        provider.getBuilder(CommonMaterialKeys.CINNABAR)[RagiumMaterialPropertyKeys.MELT_TO] = RagiumFluids.MERCURY

        provider.getBuilder(HCMaterialKeys.CRIMSON_CRYSTAL).setMolten(HCFluids.MOLTEN_CRIMSON_CRYSTAL)
        provider.getBuilder(HCMaterialKeys.WARPED_CRYSTAL).setMolten(HCFluids.MOLTEN_WARPED_CRYSTAL)
        provider.getBuilder(HCMaterialKeys.ELDRITCH).setMolten(HCFluids.MOLTEN_ELDRITCH)
        // Matter Value
        provider.getBuilder(VanillaMaterialKeys.AMETHYST)[RagiumMaterialPropertyKeys.MATTER_VALUE] = 32

        provider.getBuilder(VanillaMaterialKeys.COPPER)[RagiumMaterialPropertyKeys.MATTER_VALUE] = 256 / 2
        provider.getBuilder(VanillaMaterialKeys.IRON)[RagiumMaterialPropertyKeys.MATTER_VALUE] = 256
        provider.getBuilder(VanillaMaterialKeys.GOLD)[RagiumMaterialPropertyKeys.MATTER_VALUE] = 256 * 8

        provider.getBuilder(CommonMaterialKeys.TIN)[RagiumMaterialPropertyKeys.MATTER_VALUE] = 256
        provider.getBuilder(CommonMaterialKeys.SILVER)[RagiumMaterialPropertyKeys.MATTER_VALUE] = 256 * 8

        provider.getBuilder(CommonMaterialKeys.LEAD)[RagiumMaterialPropertyKeys.MATTER_VALUE] = 256 * 2
        provider.getBuilder(CommonMaterialKeys.URANIUM)[RagiumMaterialPropertyKeys.MATTER_VALUE] = 4096
    }

    override fun registerServerResources() {
        RagiumDynamicServerResources.initialize()
    }
}
