package hiiragi283.ragium.client.key

import com.mojang.blaze3d.platform.InputConstants
import hiiragi283.ragium.api.text.HTHasTranslationKey
import hiiragi283.ragium.client.text.RagiumClientTranslation
import net.minecraft.client.KeyMapping
import net.neoforged.neoforge.client.settings.IKeyConflictContext
import net.neoforged.neoforge.client.settings.KeyConflictContext
import net.neoforged.neoforge.client.settings.KeyModifier

/**
 * @see mekanism.client.key.MekKeyBindingBuilder
 */
class HTKeyMappingBuilder {
    private lateinit var description: String
    var keyConflictContext: IKeyConflictContext = KeyConflictContext.UNIVERSAL
    var keyModifier: KeyModifier = KeyModifier.NONE
    private lateinit var key: InputConstants.Key
    var category: String = RagiumClientTranslation.KEY_CATEGORY.translationKey

    fun description(translationKey: HTHasTranslationKey): HTKeyMappingBuilder = description(translationKey.translationKey)

    fun description(description: String): HTKeyMappingBuilder = apply {
        this.description = description
    }

    fun conflictInGame(): HTKeyMappingBuilder = conflictContext(KeyConflictContext.IN_GAME)

    fun conflictInGui(): HTKeyMappingBuilder = conflictContext(KeyConflictContext.GUI)

    fun conflictContext(keyConflictContext: KeyConflictContext): HTKeyMappingBuilder = apply {
        this.keyConflictContext = keyConflictContext
    }

    fun modifier(keyModifier: KeyModifier): HTKeyMappingBuilder = apply {
        this.keyModifier = keyModifier
    }

    fun keyCode(key: Int): HTKeyMappingBuilder = keyCode(InputConstants.Type.KEYSYM, key)

    fun keyCode(type: InputConstants.Type, key: Int): HTKeyMappingBuilder = keyCode(type.getOrCreate(key))

    fun keyCode(key: InputConstants.Key): HTKeyMappingBuilder = apply {
        this.key = key
    }

    fun category(translationKey: HTHasTranslationKey): HTKeyMappingBuilder = category(translationKey.translationKey)

    fun category(category: String): HTKeyMappingBuilder = apply {
        this.category = category
    }

    fun build(): KeyMapping = KeyMapping(
        description,
        keyConflictContext,
        keyModifier,
        key,
        category,
    )
}
