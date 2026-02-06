package hiiragi283.ragium.common.event

import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTBlockLootFactory
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTFluidMaterialProperty
import hiiragi283.core.api.material.property.HTMaterialLevel
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.material.property.addBlockPrefixes
import hiiragi283.core.api.material.property.addCustomName
import hiiragi283.core.api.material.property.addCustomOreLoot
import hiiragi283.core.api.material.property.addExtraOreResult
import hiiragi283.core.api.material.property.addItemPrefixes
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.material.property.setTextureSet
import hiiragi283.core.api.property.plusAssign
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.Tags

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumMaterialEventHandler {
    private val materialBlockSet: Set<HTTagPrefix> = buildSet {
        addAll(CommonTagPrefixes.ORES)
        add(CommonTagPrefixes.RAW_BLOCK)
    }

    @SubscribeEvent
    fun gatherAttributes(event: HTMaterialPropertyEvent) {
        // Minerals
        event.modify(RagiumMaterialKeys.RAGINITE) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)
            addBlockPrefixes(materialBlockSet)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.RAW, CommonTagPrefixes.CRUSHED_ORE)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(RagiumFluids.MOLTEN_RAGINITE))
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(3))

            setName("Raginite", "ラギナイト")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
            addCustomOreLoot(HTBlockLootFactory.createOre(CommonTagPrefixes.RAW))
        }
        // Gems
        event.modify(RagiumMaterialKeys.RAGI_CRYSTAL) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)

            addExtraOreResult(RagiumMaterialKeys.RAGINITE, 1 / 4f)

            setName("Ragi-Crystal", "ラギクリスタル")
            setTextureSet("diamond", HTMaterialTextureSet.SHINE)
            addCustomOreLoot(HTBlockLootFactory.createOre(CommonTagPrefixes.RAW))
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, RagiumMaterialKeys.RAGINITE.getId())
        }
        // Alloys
        val alloySet = setOf(
            CommonTagPrefixes.DUST,
            CommonTagPrefixes.INGOT,
            CommonTagPrefixes.NUGGET,
            CommonTagPrefixes.GEAR,
            CommonTagPrefixes.PLATE,
            CommonTagPrefixes.ROD,
            CommonTagPrefixes.WIRE,
        )
        event.modify(RagiumMaterialKeys.RAGI_ALLOY) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet)

            setName("Ragi-Alloy", "ラギ合金")
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, RagiumAPI.id("raginite"))
        }
        event.modify(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Advanced Ragi-Alloy", "発展ラギ合金")
        }
        // Others
        event.modify(RagiumMaterialKeys.MEAT) {
            setDefaultPart(Tags.Items.FOODS_RAW_MEAT, createItem(CommonTagPrefixes.INGOT, RagiumMaterialKeys.MEAT))
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MEAT))
            put(HTMaterialPropertyKeys.SMELTED_TO, RagiumMaterialKeys.COOKED_MEAT)

            setName("Meat", "肉")
            addCustomName(CommonTagPrefixes.DUST, "Minced Meat", "ひき肉")
        }
        event.modify(RagiumMaterialKeys.COOKED_MEAT) {
            setDefaultPart(Tags.Items.FOODS_COOKED_MEAT, createItem(CommonTagPrefixes.INGOT, RagiumMaterialKeys.COOKED_MEAT))
            addItemPrefixes(CommonTagPrefixes.INGOT)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Cooked Meat", "焼肉")
        }
    }

    @JvmStatic
    private fun createItem(prefix: HTTagPrefix, key: HTMaterialKey): HTItemHolderLike<*> = HTItemHolderLike.of(prefix.createId(key))
}
