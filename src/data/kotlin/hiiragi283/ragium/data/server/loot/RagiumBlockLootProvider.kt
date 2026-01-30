package hiiragi283.ragium.data.server.loot

import hiiragi283.core.api.data.loot.HTBlockLootTableProvider
import hiiragi283.core.common.block.HTBlockWithEntity
import hiiragi283.core.common.registry.HTDeferredOnlyBlock
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.HTImitationSpawnerBlock
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue

class RagiumBlockLootProvider(registries: HolderLookup.Provider) : HTBlockLootTableProvider(RagiumAPI.MOD_ID, registries) {
    override fun generate() {
        RagiumBlocks.REGISTER
            .asBlockSequence()
            .map(HTDeferredOnlyBlock<*>::get)
            .forEach { block: Block ->
                add(
                    block,
                    if (block is HTBlockWithEntity) {
                        copyComponent(block) {
                            include(DataComponents.CUSTOM_NAME)
                            include(DataComponents.ENCHANTMENTS)
                            include(DataComponents.HIDE_ADDITIONAL_TOOLTIP)

                            include(RagiumDataComponents.MACHINE_UPGRADES)

                            include(HCDataComponents.ENERGY)
                            include(HCDataComponents.FLUID)
                            include(HCDataComponents.ITEM)
                            when (block) {
                                is HTImitationSpawnerBlock -> include(RagiumDataComponents.SPAWNER_MOB)
                            }
                        }
                    } else {
                        createSingleItemTable(block)
                    },
                )
            }

        registerMaterials()
    }

    private fun copyComponent(block: Block, builderAction: CopyComponentsFunction.Builder.() -> Unit): LootTable.Builder = LootTable
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
                                    .copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                    .apply(builderAction),
                            ),
                    ),
            ),
        )
}
