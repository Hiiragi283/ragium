package hiiragi283.ragium.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.RecipePrioritiesProvider

class RagiumRecipePriorityProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : RecipePrioritiesProvider(output, registries, RagiumAPI.MOD_ID) {
    override fun start() {
        add("${RagiumConstants.MELTING}/molten_glass_from_panes", 1)
    }
}
