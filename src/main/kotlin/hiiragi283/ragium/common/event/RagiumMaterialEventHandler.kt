package hiiragi283.ragium.common.event

import hiiragi283.core.api.event.HTMaterialDefinitionEvent
import hiiragi283.core.api.material.addColor
import hiiragi283.core.api.material.addDefaultPrefix
import hiiragi283.core.api.material.addName
import hiiragi283.core.api.material.attribute.HTSmeltingMaterialAttribute
import hiiragi283.core.common.data.texture.HCTextureTemplates
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.texture.RagiumMaterialPalette
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumMaterialEventHandler {
    @SubscribeEvent
    fun gatherAttributes(event: HTMaterialDefinitionEvent) {
        val isDataGen: Boolean = event.isDataGen
        // Minerals
        event.modify(RagiumMaterialKeys.RAGINITE) {
            addDefaultPrefix(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Raginite", "ラギナイト")
                addColor(RagiumMaterialPalette.RAGINITE)
                add(HCTextureTemplates.DUST_DULL)
            }
        }
        // Gems
        event.modify(RagiumMaterialKeys.RAGI_CRYSTAL) {
            addDefaultPrefix(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Ragi-Crystal", "ラギクリスタル")
                addColor(RagiumMaterialPalette.RAGINITE)
                add(HCTextureTemplates.GEM_DIAMOND)
            }
        }
        // Alloys
        event.modify(RagiumMaterialKeys.RAGI_ALLOY) {
            addDefaultPrefix(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Ragi-Alloy", "ラギ合金")
                addColor(RagiumMaterialPalette.RAGINITE)
                add(HCTextureTemplates.METAL)
            }
        }
        event.modify(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) {
            addDefaultPrefix(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Advanced Ragi-Alloy", "発展ラギ合金")
                addColor(RagiumMaterialPalette.ADVANCED_RAGI_ALLOY)
                add(HCTextureTemplates.METAL)
            }
        }
        // Others
        event.modify(RagiumMaterialKeys.MEAT) {
            addDefaultPrefix(HCMaterialPrefixes.INGOT)
            add(HTSmeltingMaterialAttribute.withSmoking(RagiumMaterialKeys.COOKED_MEAT))
            if (isDataGen) {
                addName("Meat", "肉")
            }
        }
        event.modify(RagiumMaterialKeys.COOKED_MEAT) {
            addDefaultPrefix(HCMaterialPrefixes.INGOT)
            add(HTSmeltingMaterialAttribute.disable())
            if (isDataGen) {
                addName("Cooked Meat", "焼肉")
            }
        }
    }
}
