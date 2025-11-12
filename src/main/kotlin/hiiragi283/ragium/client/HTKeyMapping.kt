package hiiragi283.ragium.client

import com.mojang.blaze3d.platform.InputConstants
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.toDescriptionKey
import hiiragi283.ragium.api.text.HTHasTranslationKey
import hiiragi283.ragium.client.text.RagiumClientTranslation
import net.minecraft.client.KeyMapping
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.settings.IKeyConflictContext

@OnlyIn(Dist.CLIENT)
class HTKeyMapping(
    name: String,
    keyConflict: IKeyConflictContext,
    inputType: InputConstants.Type,
    keyCode: Int,
) : KeyMapping(
        RagiumAPI.id(name).toDescriptionKey("key", "desc"),
        keyConflict,
        inputType,
        keyCode,
        RagiumClientTranslation.KEY_CATEGORY.translationKey,
    ),
    HTHasTranslationKey {
    override val translationKey: String = this.name
}
