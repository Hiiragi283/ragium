package hiiragi283.ragium.data.tag

import hiiragi283.lib.data.tag.HTTagsProvider
import hiiragi283.lib.registry.toLike
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTMaterial
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

class RagiumBlockTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTTagsProvider<Block>(output, Registries.BLOCK, lookupProvider, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        setOf(
            HTMaterial.Mineral.GLOWSTONE to Blocks.GLOWSTONE,
            HTMaterial.Gem.QUARTZ to Blocks.QUARTZ_BLOCK,
            HTMaterial.Gem.AMETHYST to Blocks.AMETHYST_BLOCK,
        ).forEach { (material: HTMaterial, block: Block) -> tags(CommonTagPrefixes.STORAGE_BLOCK, material).add(block.toLike()) }
    }
}
