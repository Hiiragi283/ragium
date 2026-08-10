package hiiragi283.ragium

import hiiragi283.lib.mod.HTCommonMod
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.registries.NewRegistryEvent

@Mod(RagiumAPI.MOD_ID)
data object Ragium : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
    }

    override fun registerRegistries(event: NewRegistryEvent) {
        event.register(RagiumRegistries.ITEM_RESULT_TYPE)
    }
}
