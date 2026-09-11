package hiiragi283.ragium.data.loot

import hiiragi283.lib.data.loot.HTBlockLootTableProvider
import hiiragi283.lib.registry.HTSimpleDeferredBlock
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.HTMaterialAccess
import hiiragi283.ragium.api.material.HTOreBlockPart
import hiiragi283.ragium.api.material.HTStorageBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.block.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.ItemLike
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

        for (part: HTStorageBlockPart in HTStorageBlockPart.entries) {
            RagiumBlocks.MATERIAL_BLOCKS.row(part).values.forEach { dropSelf(it.block) }
        }
    }

    private fun registerOres(material: RagiumMaterial, rawPart: HTItemPart, countProvider: NumberProvider? = null) {
        for (part: HTOreBlockPart in HTOreBlockPart.entries) {
            val block: HTSimpleDeferredBlock = RagiumBlocks.MATERIAL_BLOCKS[part, material]?.block ?: continue
            val drop: ItemLike = HTMaterialAccess.INSTANCE.getMaterialItem(rawPart, material) ?: continue
            add(block) { block: Block ->
                val builder: LootPoolSingletonContainer.Builder<*> = LootItem.lootTableItem(drop)
                countProvider?.let(SetItemCountFunction::setCount)?.let(builder::apply)
                builder.apply(ApplyBonusCount.addOreBonusCount(fortune))
                this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, builder))
            }
        }
    }
}
