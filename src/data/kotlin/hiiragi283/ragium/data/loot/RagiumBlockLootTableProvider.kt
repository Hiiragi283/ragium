package hiiragi283.ragium.data.loot

import hiiragi283.lib.data.loot.HTBlockLootTableProvider
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.HTOreBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

class RagiumBlockLootTableProvider(registries: HolderLookup.Provider) :
    HTBlockLootTableProvider(registries, RagiumAPI.MOD_ID, RagiumBlocks.REGISTER.asBlockSequence()) {
    override fun generate() {
        knownBlocks.forEach(::dropSelf)

        // Material
        registerOres(RagiumMaterial.Mineral.SULFUR, HTItemPart.DUST, UniformGenerator.between(2f, 5f))
        registerOres(RagiumMaterial.Mineral.NITER, HTItemPart.DUST, UniformGenerator.between(2f, 5f))

        RagiumBlocks.STORAGE_BLOCKS.values.forEach(::dropSelf)
        // Machine
        for (machine: HTSimpleDeferredBlockAndItem in RagiumBlocks.MACHINES.values) {
            add(machine, ::copyComponent)
        }
        // Storage
        for (tank: HTSimpleDeferredBlockAndItem in listOf(RagiumBlocks.TANK, RagiumBlocks.CREATIVE_TANK)) {
            add(tank) { block: Block -> copyComponent(block) { include(RagiumDataComponents.FLUID) } }
        }
    }

    private fun registerOres(material: RagiumMaterial, rawPart: HTItemPart, countProvider: NumberProvider? = null) {
        for (part: HTOreBlockPart in HTOreBlockPart.entries) {
            val block: HTSimpleDeferredBlockAndItem = RagiumBlocks.MATERIAL_ORES[part, material] ?: continue
            val drop: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[rawPart, material] ?: continue
            add(block) { block: Block ->
                val builder: LootPoolSingletonContainer.Builder<*> = LootItem.lootTableItem(drop)
                countProvider?.let(SetItemCountFunction::setCount)?.let(builder::apply)
                builder.apply(ApplyBonusCount.addOreBonusCount(fortune))
                this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, builder))
            }
        }
    }

    private inline fun copyComponent(
        block: Block,
        builderAction: CopyComponentsFunction.Builder.() -> Unit = {}
    ): LootTable.Builder = LootTable
        .lootTable()
        .withPool(
            applyExplosionCondition(
                block,
                LootPool
                    .lootPool()
                    .setRolls(ConstantValue.exactly(1f))
                    .add(
                        LootItem
                            .lootTableItem(block)
                            .apply(
                                CopyComponentsFunction
                                    .copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                                    .include(DataComponents.CUSTOM_NAME)
                                    .include(RagiumDataComponents.OWNER_ID)
                                    .apply(builderAction)
                            )
                    )
            )
        )
}
