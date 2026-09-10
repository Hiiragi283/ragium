package hiiragi283.ragium.data.loot

import hiiragi283.lib.data.loot.HTBlockLootTableProvider
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

class RagiumBlockLootTableProvider(registries: HolderLookup.Provider) :
    HTBlockLootTableProvider(registries, RagiumAPI.MOD_ID, RagiumBlocks.REGISTER.asBlockSequence()) {
    override fun generate() {
        knownBlocks.forEach(::dropSelf)

        // Material
        registerOres(RagiumMaterial.Mineral.SULFUR, HTItemPart.DUST, UniformGenerator.between(2f, 5f))

        for (part: HTBlockPart in setOf(HTBlockPart.STORAGE_BLOCK, HTBlockPart.RAW_STORAGE_BLOCK)) {
            RagiumBlocks.MATERIAL_BLOCKS.row(part).values.forEach { dropSelf(it.block) }
        }
    }

    private fun registerOres(material: RagiumMaterial, rawPart: HTItemPart, countProvider: NumberProvider? = null) {
        for ((part: HTBlockPart, blockItem: HTSimpleDeferredBlockAndItem) in RagiumBlocks.MATERIAL_BLOCKS.column(
            material
        )) {
            if (part == HTBlockPart.STORAGE_BLOCK || part == HTBlockPart.RAW_STORAGE_BLOCK) continue
            add(blockItem.block) { block: Block ->
                val builder: LootPoolSingletonContainer.Builder<*> =
                    LootItem.lootTableItem(RagiumItems.getOrThrow(rawPart, material))
                        .apply(ApplyBonusCount.addOreBonusCount(fortune))
                countProvider?.let(SetItemCountFunction::setCount)?.let(builder::apply)
                this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, builder))
            }
        }
    }
}
