package hiiragi283.ragium.common.event

import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.material.property.HTFluidMaterialProperty
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTSmeltingMaterialProperty
import hiiragi283.core.api.material.property.addColor
import hiiragi283.core.api.material.property.addDefaultPart
import hiiragi283.core.api.material.property.addName
import hiiragi283.core.api.material.property.addTemplate
import hiiragi283.core.common.data.texture.HCTextureTemplates
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.texture.RagiumMaterialPalette
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
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
                addColor(RagiumMaterialPalette.RAGINITE)
                addTemplate(HCTextureTemplates.DUST_DULL)
            }
        }
        // Gems
        event.modify(RagiumMaterialKeys.RAGI_CRYSTAL) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Ragi-Crystal", "ラギクリスタル")
                addColor(RagiumMaterialPalette.RAGINITE)
                addTemplate(HCTextureTemplates.GEM_DIAMOND)
            }
        }
        // Alloys
        event.modify(RagiumMaterialKeys.RAGI_ALLOY) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Ragi-Alloy", "ラギ合金")
                addColor(RagiumMaterialPalette.RAGINITE)
                addTemplate(HCTextureTemplates.METAL)
            }
        }
        event.modify(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Advanced Ragi-Alloy", "発展ラギ合金")
                addColor(RagiumMaterialPalette.ADVANCED_RAGI_ALLOY)
                addTemplate(HCTextureTemplates.METAL)
            }
        }
        // Others
        event.modify(RagiumMaterialKeys.MEAT) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MEAT))
            if (isDataGen) {
                addName("Meat", "肉")
                put(
                    HTMaterialPropertyKeys.SMELTING,
                    HTSmeltingMaterialProperty.withSmoking(HCMaterialPrefixes.INGOT, RagiumMaterialKeys.COOKED_MEAT),
                )
            }
        }
        event.modify(RagiumMaterialKeys.COOKED_MEAT) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Cooked Meat", "焼肉")
                put(
                    HTMaterialPropertyKeys.SMELTING,
                    HTSmeltingMaterialProperty.smeltingOnly(HCMaterialPrefixes.DUST, CommonMaterialKeys.ASH),
                )
            }
        }
    }
}
