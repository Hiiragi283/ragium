package hiiragi283.ragium.data.advancement

import hiiragi283.lib.HTConstants
import hiiragi283.lib.advancment.AdvancementKey
import hiiragi283.lib.data.HTSingleRegistryBootstrap
import hiiragi283.lib.data.advancement.HTAdvancementBuilder
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.HTSimpleValueWithKey
import hiiragi283.lib.resource.debugPath
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.advancment.RagiumAdvancementKeys
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementType
import net.minecraft.core.ClientAsset
import net.minecraft.resources.Identifier
import net.minecraft.tags.BlockItemTagId
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.Tags

data object RagiumAdvancementProvider : HTSingleRegistryBootstrap<Advancement>(RagiumAPI.MOD_ID) {
    override fun register() {
        // Root
        HTAdvancementBuilder.create(RagiumAdvancementKeys.ROOT) {
            display {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Mineral.RAGINITE)
                +ClientAsset.ResourceTexture(vanillaId(HTConstants.BLOCK, "smooth_stone"))
                showToast = false
                showChat = false
            }
            inventory(getHasName(Tags.BlockItems.PLAYER_WORKSTATIONS_CRAFTING_TABLES)) {
                predicate { +itemSet(Tags.BlockItems.PLAYER_WORKSTATIONS_CRAFTING_TABLES) }
            }
        }.save(context)
        createSimple(
            RagiumAdvancementKeys.SOOTY_IRON,
            RagiumAdvancementKeys.ROOT,
            HTItemPart.INGOT,
            RagiumMaterial.Alloy.SOOTY_IRON
        )
        // Mechanical
        createSimple(
            RagiumAdvancementKeys.MECHANICAL_MACHINE_PARTS,
            RagiumAdvancementKeys.SOOTY_IRON,
            RagiumItems.getParts(HTMachineType.MECHANICAL),
            AdvancementType.GOAL
        )
        createSimple(
            RagiumAdvancementKeys.ASSEMBLER,
            RagiumAdvancementKeys.MECHANICAL_MACHINE_PARTS,
            RagiumBlocks.ASSEMBLER
        )
        createSimple(
            RagiumAdvancementKeys.CRUSHER,
            RagiumAdvancementKeys.MECHANICAL_MACHINE_PARTS,
            RagiumBlocks.CRUSHER
        )
        createSimple(
            RagiumAdvancementKeys.UPGRADE_STATION,
            RagiumAdvancementKeys.SOOTY_IRON,
            RagiumBlocks.UPGRADE_STATION,
            AdvancementType.GOAL
        )
        // Heat
        createSimple(
            RagiumAdvancementKeys.HEAT_MACHINE_PARTS,
            RagiumAdvancementKeys.ASSEMBLER,
            RagiumItems.getParts(HTMachineType.HEAT),
            AdvancementType.GOAL
        )
        createSimple(
            RagiumAdvancementKeys.ALLOY_SMELTER,
            RagiumAdvancementKeys.HEAT_MACHINE_PARTS,
            RagiumBlocks.ALLOY_SMELTER
        )
        createSimple(
            RagiumAdvancementKeys.BLACK_STEEL,
            RagiumAdvancementKeys.ALLOY_SMELTER,
            HTItemPart.INGOT,
            RagiumMaterial.Alloy.BLACK_STEEL
        )
        createSimple(
            RagiumAdvancementKeys.FREEZER,
            RagiumAdvancementKeys.HEAT_MACHINE_PARTS,
            RagiumBlocks.FREEZER
        )
        createSimple(
            RagiumAdvancementKeys.MELTER,
            RagiumAdvancementKeys.HEAT_MACHINE_PARTS,
            RagiumBlocks.MELTER
        )
        // Chemical
        createSimple(
            RagiumAdvancementKeys.CHEMICAL_MACHINE_PARTS,
            RagiumAdvancementKeys.BLACK_STEEL,
            RagiumItems.getParts(HTMachineType.CHEMICAL),
            AdvancementType.GOAL
        )
        createSimple(
            RagiumAdvancementKeys.CHEMICAL_BATH,
            RagiumAdvancementKeys.CHEMICAL_MACHINE_PARTS,
            RagiumBlocks.CHEMICAL_BATH
        )
    }

    //    Extensions    //

    fun getHasName(id: Identifier): String = "has_${id.debugPath}"

    fun getHasName(value: HTSimpleValueWithKey<*>): String = getHasName(value.id())

    fun getHasName(tagKey: TagKey<*>): String = getHasName(tagKey.location())

    fun getHasName(tagKey: BlockItemTagId): String = getHasName(tagKey.item())

    fun getHasName(prefix: HTTagPrefix, material: HTMaterialLike): String = getHasName(prefix.itemTagKey(material))

    private fun createSimple(
        key: AdvancementKey,
        parentKey: AdvancementKey,
        item: HTSimpleDeferredItem,
        type: AdvancementType = AdvancementType.TASK
    ) {
        HTAdvancementBuilder.create(key) {
            +parentKey
            display {
                +item
                this.type = type
            }
            inventory(getHasName(item)) { predicate { items { +item } } }
        }.save(context)
    }

    private fun createSimple(
        key: AdvancementKey,
        parentKey: AdvancementKey,
        item: HTSimpleDeferredBlockAndItem,
        type: AdvancementType = AdvancementType.TASK
    ) {
        HTAdvancementBuilder.create(key) {
            +parentKey
            display {
                +item
                this.type = type
            }
            inventory(getHasName(item)) { predicate { items { +item } } }
        }.save(context)
    }

    private fun createSimple(
        key: AdvancementKey,
        parentKey: AdvancementKey,
        part: HTItemPart,
        material: RagiumMaterial
    ) {
        val item: HTSimpleDeferredItem = RagiumItems.getOrThrow(part, material)
        HTAdvancementBuilder.create(key) {
            +parentKey
            display { +item }
            inventory(getHasName(item)) { predicate { +holderSet(part.tagPrefix, material) } }
        }.save(context)
    }
}
