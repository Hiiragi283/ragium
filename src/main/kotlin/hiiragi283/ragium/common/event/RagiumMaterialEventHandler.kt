package hiiragi283.ragium.common.event

import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.property.HTFluidMaterialProperty
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.material.property.HTSmeltingMaterialProperty
import hiiragi283.core.api.material.property.addCustomName
import hiiragi283.core.api.material.property.addDefaultPart
import hiiragi283.core.api.material.property.addName
import hiiragi283.core.api.material.property.addTextureSet
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.property.HTFormingRecipeFlag
import hiiragi283.ragium.api.material.property.RagiumMaterialPropertyKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumMaterialEventHandler {
    @SubscribeEvent
    fun gatherAttributes(event: HTMaterialPropertyEvent) {
        val isDataGen: Boolean = event.isDataGen
        // Minerals
        event.modify(RagiumMaterialKeys.RAGINITE) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(RagiumFluids.MOLTEN_RAGINITE))
            if (isDataGen) {
                addName("Raginite", "ラギナイト")
                addTextureSet("mineral", HTMaterialTextureSet.DULL)
            }
        }
        // Gems
        event.modify(RagiumMaterialKeys.RAGI_CRYSTAL) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Ragi-Crystal", "ラギクリスタル")
                addTextureSet("diamond", HTMaterialTextureSet.SHINE)
                put(HTMaterialPropertyKeys.TEXTURE_COLOR, RagiumAPI.id("raginite"))
            }
        }
        // Alloys
        event.modify(RagiumMaterialKeys.RAGI_ALLOY) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Ragi-Alloy", "ラギ合金")
                put(HTMaterialPropertyKeys.TEXTURE_COLOR, RagiumAPI.id("raginite"))
            }
        }
        event.modify(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Advanced Ragi-Alloy", "発展ラギ合金")
            }
        }
        // Others
        event.modify(RagiumMaterialKeys.MEAT) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MEAT))
            if (isDataGen) {
                addName("Meat", "肉")
                addCustomName(HCMaterialPrefixes.DUST, "Minced Meat", "ひき肉")
                put(
                    HTMaterialPropertyKeys.SMELTING,
                    HTSmeltingMaterialProperty.withSmoking(
                        RagiumItems.MATERIALS.getOrThrow(HCMaterialPrefixes.INGOT, RagiumMaterialKeys.COOKED_MEAT),
                    ),
                )
            }
        }
        event.modify(RagiumMaterialKeys.COOKED_MEAT) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Cooked Meat", "焼肉")
                put(
                    HTMaterialPropertyKeys.SMELTING,
                    HTSmeltingMaterialProperty.smeltingOnly(
                        HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.DUST, CommonMaterialKeys.ASH),
                    ),
                )
            }
        }

        // Existing
        event.modify(VanillaMaterialKeys.GLASS) {
            put(RagiumMaterialPropertyKeys.FORMING_RECIPE_FLAG, HTFormingRecipeFlag.solidifyOnly())
        }

        event.modify(CommonMaterialKeys.PLASTIC) {
            put(RagiumMaterialPropertyKeys.FORMING_RECIPE_FLAG, HTFormingRecipeFlag.solidifyOnly())
        }
        event.modify(CommonMaterialKeys.RUBBER) {
            put(RagiumMaterialPropertyKeys.FORMING_RECIPE_FLAG, HTFormingRecipeFlag.solidifyOnly())
        }
    }
}
