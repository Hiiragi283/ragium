package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.advancements.HTBlockInteractionTrigger
import hiiragi283.ragium.api.data.interaction.HTBlockAction
import hiiragi283.ragium.api.data.interaction.HTBreakBlockAction
import hiiragi283.ragium.api.data.interaction.HTDropItemBlockAction
import hiiragi283.ragium.api.data.interaction.HTPlaySoundBlockAction
import hiiragi283.ragium.api.data.interaction.HTReplaceBlockAction
import hiiragi283.ragium.api.data.interaction.HTSpawnEntityBlockAction
import hiiragi283.ragium.api.extension.commonId
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.RegisterEvent

object RagiumMiscRegister {
    @JvmStatic
    fun onRegister(event: RegisterEvent) {
        event.register(Registries.TRIGGER_TYPE, ::advancementTriggers)
        event.register(RagiumRegistries.Keys.BLOCK_ACTION_SERIALIZERS, ::blockActionSerializers)
    }

    @JvmStatic
    private fun advancementTriggers(helper: RegisterEvent.RegisterHelper<CriterionTrigger<*>>) {
        helper.register(RagiumAPI.id("block_interaction"), HTBlockInteractionTrigger)
    }

    @JvmStatic
    private fun blockActionSerializers(helper: RegisterEvent.RegisterHelper<MapCodec<out HTBlockAction>>) {
        helper.register(commonId("break"), HTBreakBlockAction.CODEC)
        helper.register(commonId("drop_item"), HTDropItemBlockAction.CODEC)
        helper.register(commonId("play_sound"), HTPlaySoundBlockAction.CODEC)
        helper.register(commonId("replace"), HTReplaceBlockAction.CODEC)
        helper.register(commonId("spawn"), HTSpawnEntityBlockAction.CODEC)
    }
}
