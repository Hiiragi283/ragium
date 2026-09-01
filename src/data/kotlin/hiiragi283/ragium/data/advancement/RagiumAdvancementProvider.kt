package hiiragi283.ragium.data.advancement

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.advancement.AdvancementKey
import hiiragi283.lib.data.advancement.HTAdvancementProvider
import hiiragi283.lib.data.advancement.builder.HTAdvancementBuilder
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.item.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.ClientAsset
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput

class RagiumAdvancementProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTAdvancementProvider(packOutput, future, RagiumAPI.MOD_ID) {
    private fun createSimple(key: AdvancementKey, parentKey: AdvancementKey, item: HTSimpleDeferredItem) {
        HTAdvancementBuilder.create(key) {
            +parentKey
            display { +item }
            inventory(getHasName(item)) { predicate { items { +item } } }
        }.save(exporter)
    }

    override fun buildAdvancements() {
        // Root
        HTAdvancementBuilder.create(RagiumAdvancementKeys.ROOT) {
            display {
                +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Metal.SOOTY_IRON)
                +ClientAsset.ResourceTexture(vanillaId(HTConstants.BLOCK, "smooth_stone"))
                showToast = false
                showChat = false
            }
            inventory(getHasName(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON)) {
                predicate { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON) }
            }
        }.save(exporter)
    }

    override fun getName(): String = "Ragium Advancements"
}
