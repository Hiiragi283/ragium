package hiiragi283.ragium.data.loot

import hiiragi283.core.api.data.loot.HTBlockLootTableProvider
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.common.block.HTBlockWithEntity
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.storage.HTBatteryBlock
import hiiragi283.ragium.common.block.storage.HTCrateBlock
import hiiragi283.ragium.common.block.storage.HTStorageBlock
import hiiragi283.ragium.common.block.storage.HTTankBlock
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

class RagiumBlockLootProvider(registries: HolderLookup.Provider) : HTBlockLootTableProvider(registries, RagiumAPI.MOD_ID, RagiumBlocks.REGISTER.asBlockSequence()) {
    override fun generate() {
        RagiumBlocks.REGISTER
            .asBlockSequence()
            .map(SupplierWithId<Block>::get)
            .forEach { block: Block ->
                add(
                    block,
                    if (block is HTBlockWithEntity) {
                        copyComponent(block) {
                            include(DataComponents.CUSTOM_NAME)
                            include(DataComponents.ENCHANTMENTS)
                            include(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
                            when (block) {
                                is HTStorageBlock -> include(RagiumDataComponents.CAPACITY_SCALE)
                            }
                            when (block) {
                                is HTBatteryBlock -> include(HCDataComponents.ENERGY)
                                is HTCrateBlock -> include(DataComponents.CONTAINER)
                                is HTTankBlock -> include(HCDataComponents.FLUID)
                            }
                        }
                    } else {
                        createSingleItemTable(block)
                    },
                )
            }
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
