package hiiragi283.ragium.data.server.loot

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
import net.neoforged.neoforge.common.loot.AddTableLootModifier
import net.neoforged.neoforge.common.loot.LootTableIdCondition
import java.util.concurrent.CompletableFuture

class RagiumGlobalLootProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    GlobalLootModifierProvider(output, registries, RagiumAPI.MOD_ID) {
    override fun start() {
        // Drops Ragi-Cherry from Cherry Leaves
        add(
            "drop_ragi_cherry",
            AddTableLootModifier(
                arrayOf(
                    LootTableIdCondition.builder(Blocks.CHERRY_LEAVES.lootTable.location()).build(),
                ),
                RagiumCustomLootProvider.DROP_RAGI_CHERRY,
            ),
        )
    }
}
