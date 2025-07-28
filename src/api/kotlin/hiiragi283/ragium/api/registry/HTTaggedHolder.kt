package hiiragi283.ragium.api.registry

import net.minecraft.tags.TagKey

interface HTTaggedHolder<T : Any> {
    val tagKey: TagKey<T>
}
