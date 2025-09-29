package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.tag.RagiumModTags
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DamageTypes

class RagiumDamageTypeTagsProvider(context: HTDataGenContext) : HTTagsProvider<DamageType>(Registries.DAMAGE_TYPE, context) {
    override fun addTags(builder: HTTagBuilder<DamageType>) {
        builder.add(RagiumModTags.DamageTypes.IS_SONIC, DamageTypes.SONIC_BOOM)
    }
}
