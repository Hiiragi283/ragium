package hiiragi283.ragium.data.advancement

import hiiragi283.lib.HTConstants
import hiiragi283.lib.advancment.AdvancementKey
import hiiragi283.lib.data.advancement.HTAdvancementProvider
import hiiragi283.lib.data.advancement.builder.HTAdvancementBuilder
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.HTMaterialAccess
import hiiragi283.ragium.api.material.HTMaterialContents
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.common.advancment.RagiumAdvancementKeys
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.advancements.AdvancementType
import net.minecraft.core.ClientAsset
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumAdvancementProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTAdvancementProvider(packOutput, future, RagiumAPI.MOD_ID) {
    private fun createSimple(
        key: AdvancementKey,
        parentKey: AdvancementKey,
        item: HTSimpleDeferredItem,
        type: AdvancementType = AdvancementType.GOAL
    ) {
        HTAdvancementBuilder.create(key) {
            +parentKey
            display {
                +item
                this.type = type
            }
            inventory(getHasName(item)) { predicate { items { +item } } }
        }.save(exporter)
    }

    private fun createSimple(key: AdvancementKey, parentKey: AdvancementKey, item: HTSimpleDeferredBlockAndItem) {
        HTAdvancementBuilder.create(key) {
            +parentKey
            display { +item }
            inventory(getHasName(item)) { predicate { items { +item } } }
        }.save(exporter)
    }

    private fun createSimple(
        key: AdvancementKey,
        parentKey: AdvancementKey,
        part: HTItemPart,
        material: RagiumMaterial
    ) {
        val item: HTMaterialContents.ItemEntry =
            HTMaterialAccess.INSTANCE.getMaterialBlockOrItem(part, material) ?: return
        HTAdvancementBuilder.create(key) {
            +parentKey
            display { +item }
            inventory(getHasName(item)) { predicate { +holderSet(part.tagPrefix, material) } }
        }.save(exporter)
    }

    override fun exportValues() {
        // Root
        HTAdvancementBuilder.create(RagiumAdvancementKeys.ROOT) {
            display {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Mineral.RAGINITE)
                +ClientAsset.ResourceTexture(vanillaId(HTConstants.BLOCK, "smooth_stone"))
                showToast = false
                showChat = false
            }
            inventory(getHasName(Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES)) {
                predicate { +holderSet(Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES) }
            }
        }.save(exporter)
        createSimple(
            RagiumAdvancementKeys.SOOTY_IRON,
            RagiumAdvancementKeys.ROOT,
            HTItemPart.INGOT,
            RagiumMaterial.Metal.SOOTY_IRON
        )
        // Mechanical
        createSimple(
            RagiumAdvancementKeys.MECHANICAL_MACHINE_CASING,
            RagiumAdvancementKeys.SOOTY_IRON,
            RagiumItems.getCasing(HTMachineType.MECHANICAL),
            AdvancementType.GOAL
        )
        createSimple(
            RagiumAdvancementKeys.ASSEMBLER,
            RagiumAdvancementKeys.MECHANICAL_MACHINE_CASING,
            RagiumBlocks.ASSEMBLER
        )
        createSimple(
            RagiumAdvancementKeys.CRUSHER,
            RagiumAdvancementKeys.MECHANICAL_MACHINE_CASING,
            RagiumBlocks.CRUSHER
        )
        // Heat
        createSimple(
            RagiumAdvancementKeys.HEAT_MACHINE_CASING,
            RagiumAdvancementKeys.ASSEMBLER,
            RagiumItems.getCasing(HTMachineType.HEAT),
            AdvancementType.GOAL
        )
        createSimple(
            RagiumAdvancementKeys.FREEZER,
            RagiumAdvancementKeys.HEAT_MACHINE_CASING,
            RagiumBlocks.FREEZER
        )
        createSimple(
            RagiumAdvancementKeys.BLACK_STEEL,
            RagiumAdvancementKeys.FREEZER,
            HTItemPart.INGOT,
            RagiumMaterial.Metal.BLACK_STEEL
        )
        createSimple(
            RagiumAdvancementKeys.MELTER,
            RagiumAdvancementKeys.HEAT_MACHINE_CASING,
            RagiumBlocks.MELTER
        )
        // Chemical
        createSimple(
            RagiumAdvancementKeys.CHEMICAL_MACHINE_CASING,
            RagiumAdvancementKeys.FREEZER,
            RagiumItems.getCasing(HTMachineType.CHEMICAL),
            AdvancementType.GOAL
        )
        createSimple(
            RagiumAdvancementKeys.CHEMICAL_BATH,
            RagiumAdvancementKeys.CHEMICAL_MACHINE_CASING,
            RagiumBlocks.CHEMICAL_BATH
        )
    }

    override fun getName(): String = "Ragium Advancements"
}
