package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.data.SpriteSourceProvider
import java.util.concurrent.CompletableFuture

class RagiumSpriteSourceProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    SpriteSourceProvider(output, lookupProvider, RagiumAPI.MOD_ID) {
    override fun gather() {}
}
