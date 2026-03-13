package hiiragi283.ragium.common.plugin

import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.part.property.addNamePattern
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialLevel
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.material.property.addBlockPrefixes
import hiiragi283.core.api.material.property.addCustomName
import hiiragi283.core.api.material.property.addFluidPrefixes
import hiiragi283.core.api.material.property.addItemPrefixes
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.material.property.setTextureSet
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.plugin.HTPlugin
import hiiragi283.core.api.property.add
import hiiragi283.core.api.property.plusAssign
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.property.RagiumMaterialPropertyKeys
import hiiragi283.ragium.api.tag.RagiumTagPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.part.RagiumParts
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.Tags

@HTPlugin
data object RagiumMaterialPlugin : HTMaterialPlugin {
    override val priority: Int = -1000

    override fun getId(): ResourceLocation = RagiumAPI.id("material_plugin")

    override fun registerPart(registrar: HTMaterialPlugin.PartRegistrar) {
        RagiumParts.pellet = registrar.register("pellet", "%s_pellet") {
            put(HTPartPropertyKeys.TAG_PREFIX, RagiumTagPrefixes.PELLET)

            addNamePattern("%s Pellet", "%sペレット")
        }
    }

    override fun modifyMaterial(provider: HTMaterialPlugin.MaterialProvider) {
        fuel(provider)
        mineral(provider)
        gem(provider)
        alloy(provider)
        other(provider)

        existing(provider)
    }

    private val materialBlockSet: Set<HTPartLike> = setOf(
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

            setName("Petroleum Coke", "石油コークス")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 12)
        }
    }

    @JvmStatic
    private fun mineral(provider: HTMaterialPlugin.MaterialProvider) {
        provider.getBuilder(RagiumMaterialKeys.RAGINITE).apply {
            addBlockPrefixes(materialBlockSet)
            addFluidPrefixes(HTFluidPart.MOLTEN)
            addItemPrefixes(CommonParts.DUST, CommonParts.RAW, CommonParts.CRUSHED_ORE)
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(3))

            setName("Raginite", "ラギナイト")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
        }
    }

    @JvmStatic
    private fun gem(provider: HTMaterialPlugin.MaterialProvider) {
        provider.getBuilder(RagiumMaterialKeys.RAGI_CRYSTAL).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(CommonParts.DUST, CommonParts.GEM)

            setName("Ragi-Crystal", "ラギクリスタル")
            setTextureSet("diamond", HTMaterialTextureSet.SHINE)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, RagiumMaterialKeys.RAGINITE.getId())
        }
    }

    @JvmStatic
    private fun alloy(provider: HTMaterialPlugin.MaterialProvider) {
        val alloySet: Set<HTPartLike> = setOf(
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

            setName("Ragi-Alloy", "ラギ合金")
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, RagiumAPI.id("raginite"))
        }
        provider.getBuilder(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(alloySet)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Advanced Ragi-Alloy", "発展ラギ合金")
        }
        provider.getBuilder(RagiumMaterialKeys.STAINLESS_STEEL).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonParts.BLOCK)
            addFluidPrefixes(HTFluidPart.MOLTEN)
            addItemPrefixes(alloySet.minus(CommonParts.WIRE))
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.HIGH)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.HIGH)
            add(HTMaterialPropertyKeys.DISABLE_SMELTING)

            setName("Stainless Steel", "ステンレス鋼")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
    }

    @JvmStatic
    private fun other(provider: HTMaterialPlugin.MaterialProvider) {
        provider.getBuilder(RagiumMaterialKeys.MEAT).apply {
            setDefaultPart(Tags.Items.FOODS_RAW_MEAT, createItem(CommonParts.INGOT, RagiumMaterialKeys.MEAT))
            addItemPrefixes(CommonParts.DUST, CommonParts.INGOT)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.SMELTED_TO, RagiumMaterialKeys.COOKED_MEAT)

            setName("Meat", "肉")
            addCustomName(CommonParts.DUST, "Minced Meat", "ひき肉")
        }
        provider.getBuilder(RagiumMaterialKeys.COOKED_MEAT).apply {
            setDefaultPart(Tags.Items.FOODS_COOKED_MEAT, createItem(CommonParts.INGOT, RagiumMaterialKeys.COOKED_MEAT))
            addItemPrefixes(CommonParts.INGOT)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Cooked Meat", "焼肉")
        }
    }

    @JvmStatic
    private fun existing(provider: HTMaterialPlugin.MaterialProvider) {
        provider.getBuilder(VanillaMaterialKeys.WOOD).addItemPrefixes(RagiumParts.pellet)

        provider.getBuilder(CommonMaterialKeys.SILICON).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)

            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(CommonParts.DUST, CommonParts.INGOT, CommonParts.PLATE)
        }
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

    @JvmStatic
    private fun createItem(part: HTPartLike, key: HTMaterialKey): HTSimpleItemHolderLike = part.createId(key).toItemLike()
}
