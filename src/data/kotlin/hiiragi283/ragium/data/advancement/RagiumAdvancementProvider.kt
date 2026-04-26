package hiiragi283.ragium.data.advancement

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.advancement.HTAdvancementKey
import hiiragi283.core.api.data.advancement.HTSubAdvancementProvider
import hiiragi283.core.api.data.advancement.builder.HTAdvancementBuilder
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.registry.HTDeferredBlockAndItem
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup

data object RagiumAdvancementProvider : HTSubAdvancementProvider() {
    @JvmStatic
    private fun getItem(part: HTPartLike, material: HTMaterialLike): HTSimpleItemHolderLike = HiiragiCoreAccess.INSTANCE
        .registeredContents
        .items
        .getOrThrow(part, material)

    @JvmStatic
    private fun createSimple(key: HTAdvancementKey, parentKey: HTAdvancementKey, block: HTDeferredBlockAndItem<*, *>) {
        createSimple(key, parentKey, block.itemHolder)
    }

    @JvmStatic
    private fun createSimple(key: HTAdvancementKey, parentKey: HTAdvancementKey, item: HTItemHolderLike<*>) {
        HTAdvancementBuilder.create(output, key) {
            parent = parentKey
            display {
                iconStack += item
            }
            criteria["has_${item.path}"] = { predicates += { of(item) } }
        }
    }

    private fun ItemPredicate.Builder.of(prefix: HTTagPrefix, material: HTMaterialLike): ItemPredicate.Builder =
        this.of(prefix.itemTagKey(material))

    override fun generate(registries: HolderLookup.Provider) {
        HTAdvancementBuilder.create(output, RagiumAdvancementKeys.ROOT) {
            display {
                iconStack += getItem(CommonParts.DUST, RagiumMaterialKeys.RAGINITE)
                backGround = HTConst.MINECRAFT.toId(HTConst.TEXTURES, HTConst.BLOCK, "smooth_stone.png")
                showToast = false
                showChat = false
            }
            criteria["has_raginite"] = { predicates += { of(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE) } }
        }

        basic()
        advanced()
        elite()
        ultimate()
    }

    @JvmStatic
    private fun basic() {
        HTAdvancementBuilder.create(output, RagiumAdvancementKeys.RAGI_ALLOY) {
            parent = RagiumAdvancementKeys.ROOT
            display {
                iconStack += getItem(CommonParts.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
                type = AdvancementType.GOAL
            }
            criteria["has_ragi_alloy"] = { predicates += { of(CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY) } }
        }

        createSimple(RagiumAdvancementKeys.ALLOY_SMELTER, RagiumAdvancementKeys.RAGI_ALLOY, RagiumBlocks.ALLOY_SMELTER)
    }

    @JvmStatic
    private fun advanced() {
        HTAdvancementBuilder.create(output, RagiumAdvancementKeys.ADVANCED_RAGI_ALLOY) {
            parent = RagiumAdvancementKeys.RAGI_ALLOY
            display {
                iconStack += getItem(CommonParts.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
            }
            criteria["has_adv_ragi_alloy"] = { predicates += { of(CommonTagPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) } }
        }

        HTAdvancementBuilder.create(output, RagiumAdvancementKeys.THERMOMETER) {
            parent = RagiumAdvancementKeys.ALLOY_SMELTER
            display {
                iconStack += RagiumItems.THERMOMETER
                type = AdvancementType.GOAL
            }
            criteria["has_thermometer"] = { predicates += { of(RagiumItems.THERMOMETER) } }
        }
        createSimple(RagiumAdvancementKeys.REFINERY, RagiumAdvancementKeys.THERMOMETER, RagiumBlocks.REFINERY)
        HTAdvancementBuilder.create(output, RagiumAdvancementKeys.PLASTIC) {
            parent = RagiumAdvancementKeys.REFINERY
            display {
                iconStack += getItem(CommonParts.PLATE, CommonMaterialKeys.PLASTIC)
            }
            criteria["has_plastic"] = { predicates += { of(HiiragiCoreTags.Items.PLASTICS) } }
        }
        HTAdvancementBuilder.create(output, RagiumAdvancementKeys.REFINED_SILICON) {
            parent = RagiumAdvancementKeys.REFINERY
            display {
                iconStack += getItem(CommonParts.DUST, CommonMaterialKeys.SILICON)
            }
            criteria["has_silicon"] = { predicates += { of(CommonTagPrefixes.DUST, CommonMaterialKeys.SILICON) } }
        }

        createSimple(RagiumAdvancementKeys.PYROLYZER, RagiumAdvancementKeys.THERMOMETER, RagiumBlocks.PYROLYZER)
        HTAdvancementBuilder.create(output, RagiumAdvancementKeys.CRIMSON_CRYSTAL) {
            parent = RagiumAdvancementKeys.PYROLYZER
            display {
                iconStack += getItem(CommonParts.GEM, HCMaterialKeys.CRIMSON_CRYSTAL)
            }
            criteria["has_crimson_crystal"] = { predicates += { of(CommonTagPrefixes.GEM, HCMaterialKeys.CRIMSON_CRYSTAL) } }
        }
        HTAdvancementBuilder.create(output, RagiumAdvancementKeys.WARPED_CRYSTAL) {
            parent = RagiumAdvancementKeys.PYROLYZER
            display {
                iconStack += getItem(CommonParts.GEM, HCMaterialKeys.WARPED_CRYSTAL)
            }
            criteria["has_warped_crystal"] = { predicates += { of(CommonTagPrefixes.GEM, HCMaterialKeys.WARPED_CRYSTAL) } }
        }
    }

    @JvmStatic
    private fun elite() {
        HTAdvancementBuilder.create(output, RagiumAdvancementKeys.RAGI_CRYSTAL) {
            parent = RagiumAdvancementKeys.ADVANCED_RAGI_ALLOY
            display {
                iconStack += getItem(CommonParts.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            }
            criteria["has_ragi_crystal"] = { predicates += { of(CommonTagPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL) } }
        }
        HTAdvancementBuilder.create(output, RagiumAdvancementKeys.STAINLESS_STEEL) {
            parent = RagiumAdvancementKeys.RAGI_CRYSTAL
            display {
                iconStack += getItem(CommonParts.INGOT, RagiumMaterialKeys.STAINLESS_STEEL)
            }
            criteria["has_stainless_steel"] = { predicates += { of(CommonTagPrefixes.INGOT, RagiumMaterialKeys.STAINLESS_STEEL) } }
        }

        HTAdvancementBuilder.create(output, RagiumAdvancementKeys.ELECTRIC_CIRCUIT) {
            parent = RagiumAdvancementKeys.REFINED_SILICON
            display {
                iconStack += RagiumItems.ELECTRIC_CIRCUIT
                type = AdvancementType.GOAL
            }
            criteria["has_electric_circuit"] = { predicates += { of(RagiumItems.ELECTRIC_CIRCUIT) } }
        }
        createSimple(RagiumAdvancementKeys.BREWERY, RagiumAdvancementKeys.ELECTRIC_CIRCUIT, RagiumBlocks.BREWERY)
        createSimple(RagiumAdvancementKeys.MIXER, RagiumAdvancementKeys.ELECTRIC_CIRCUIT, RagiumBlocks.MIXER)
    }

    @JvmStatic
    private fun ultimate() {}
}
