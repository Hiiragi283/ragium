package hiiragi283.ragium.data

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumEntityTypes
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class RagiumEntityLootProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    SimpleFabricLootTableProvider(output, registryLookup, LootContextTypes.ENTITY) {
    override fun accept(consumer: BiConsumer<RegistryKey<LootTable>, LootTable.Builder>) {
        consumer.accept(
            RagiumEntityTypes.OBLIVION_CUBE.lootTableId,
            LootTable
                .Builder()
                .pool(
                    LootPool
                        .builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .with(
                            ItemEntry
                                .builder(RagiumContents.Misc.OBLIVION_CRYSTAL)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))),
                        ),
                ),
        )
    }
}
