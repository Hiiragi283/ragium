package hiiragi283.ragium.common.unused

import hiiragi283.ragium.api.data.HTLangType
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.util.HTTable
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementDisplay
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.item.ItemConvertible
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import java.util.*
import java.util.function.Consumer

class HTAdvancementRegister(private val modId: String, private val consumer: Consumer<AdvancementEntry>) {
    companion object {
        @JvmStatic
        fun makeTitleKey(id: Identifier): String = Util.createTranslationKey("advancement", id)

        @JvmStatic
        fun makeDescKey(id: Identifier): String = "${makeTitleKey(id)}.description"
    }

    private val entryCache: MutableMap<Identifier, AdvancementEntry> = mutableMapOf()
    private val titleCache: HTTable.Mutable<String, HTLangType, String> = mutableTableOf()
    private val descriptionCache: HTTable.Mutable<String, HTLangType, String> = mutableTableOf()

    fun createRoot(
        name: String,
        icon: ItemConvertible,
        backGround: Identifier,
        action: Advancement.Builder.() -> Unit,
    ): Builder {
        val id: Identifier = Identifier.of(modId, name)
        val builder: Advancement.Builder = Advancement.Builder
            .create()
            .display(
                icon,
                Text.translatable(makeTitleKey(id)),
                Text.translatable(makeDescKey(id)),
                backGround,
                AdvancementFrame.TASK,
                false,
                false,
                false,
            ).apply(action)
        return Builder(builder, id, this)
    }

    fun createChild(
        name: String,
        parent: AdvancementEntry,
        icon: ItemConvertible,
        frame: AdvancementFrame = AdvancementFrame.TASK,
        showToast: Boolean = true,
        chat: Boolean = true,
        hidden: Boolean = false,
        action: Advancement.Builder.() -> Unit,
    ): Builder {
        val id: Identifier = Identifier.of(modId, name)
        val builder: Advancement.Builder = Advancement.Builder
            .create()
            .parent(parent)
            .display(
                icon,
                Text.translatable(makeTitleKey(id)),
                Text.translatable(makeDescKey(id)),
                null,
                frame,
                showToast,
                chat,
                hidden,
            ).apply(action)
        return Builder(builder, id, this)
    }

    fun createChild(
        name: String,
        parent: AdvancementEntry,
        key: HTMachineKey,
        tier: HTMachineTier,
        frame: AdvancementFrame = AdvancementFrame.TASK,
        showToast: Boolean = true,
        chat: Boolean = true,
        hidden: Boolean = false,
        action: Advancement.Builder.() -> Unit,
    ): Builder {
        val id: Identifier = Identifier.of(modId, name)
        val builder: Advancement.Builder = Advancement.Builder
            .create()
            .parent(parent)
            .display(
                AdvancementDisplay(
                    key.createItemStack(tier),
                    Text.translatable(makeTitleKey(id)),
                    Text.translatable(makeDescKey(id)),
                    Optional.empty(),
                    frame,
                    showToast,
                    chat,
                    hidden,
                ),
            ).apply(action)
        return Builder(builder, id, this)
    }

    //    Data Gen    //

    fun generateLang(type: HTLangType, builder: TranslationBuilder) {
        titleCache.forEach { (key: String, type1: HTLangType, value: String) ->
            if (type == type1) builder.add(key, value)
        }
        descriptionCache.forEach { (key: String, type1: HTLangType, value: String) ->
            if (type == type1) builder.add(key, value)
        }
    }

    //    Builder    //

    class Builder(val builder: Advancement.Builder, val id: Identifier, val register: HTAdvancementRegister) {
        private val translationKey: String = Util.createTranslationKey("advancement", id)

        //    Translation    //

        fun putTitle(type: HTLangType, value: String): Builder = apply {
            register.titleCache.put(translationKey, type, value)
        }

        fun putEnglish(value: String): Builder = putTitle(HTLangType.EN_US, value)

        fun putJapanese(value: String): Builder = putTitle(HTLangType.JA_JP, value)

        fun putDescription(type: HTLangType, value: String): Builder = apply {
            register.descriptionCache.put("$translationKey.description", type, value)
        }

        fun putEnglishDesc(value: String): Builder = putDescription(HTLangType.EN_US, value)

        fun putJapaneseDesc(value: String): Builder = putDescription(HTLangType.JA_JP, value)

        fun build(): AdvancementEntry = builder.build(register.consumer, id.toString()).apply {
            register.entryCache[id] = this
        }
    }
}
