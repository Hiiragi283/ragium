package hiiragi283.lib.registry

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import net.neoforged.bus.api.IEventBus

private typealias PotionEffectProvider = () -> Array<out MobEffectInstance>

/**
 * [ポーション][Potion]を登録する[HTDeferredRegister]の補助クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTPotionContentRegister(modId: String) {
    private val register: HTDeferredRegister<Potion> = HTDeferredRegister(Registries.POTION, modId)

    /**
     * 登録された[HTPotionContent]の一覧
     */
    val entries: Set<HTPotionContent> field: MutableSet<HTPotionContent> = ObjectLinkedOpenHashSet()

    /**
     * 登録された[HTPotionContent]の一覧を取得します。
     */
    fun asSequence(): Sequence<HTPotionContent> = entries.asSequence()

    private val contentsCache: MutableMap<ResourceKey<Potion>, HTPotionContent> = Object2ObjectLinkedOpenHashMap()

    /**
     * [HTPotionContent]を取得します。
     * @param key 対応する液体の[ResourceKey]
     * @return 対応する[HTPotionContent]がない場合はnull`
     */
    operator fun get(key: ResourceKey<Potion>): HTPotionContent? = contentsCache[key]

    fun addAlias(from: String, to: String) {
        register.addAlias(from, to)
        register.addAlias("long_$from", "long_$to")
        register.addAlias("strong_$from", "strong_$to")
    }

    /**
     * [IEventBus]に登録します。
     */
    fun register(eventBus: IEventBus) {
        register.register(eventBus)
    }

    /**
     * 新しいポーションを登録します。
     * @param name ポーションのIDのパス
     * @param baseEffects 基本のポーション効果を提供するブロック
     * @param longEffects 延長されたポーション効果を提供するブロック
     * @param strongEffects 強化されたポーション効果を提供するブロック
     * @return 新しい[HTPotionContent]のインスタンス
     */
    fun registerPotion(
        name: String,
        baseEffects: PotionEffectProvider,
        longEffects: PotionEffectProvider,
        strongEffects: PotionEffectProvider? = null
    ): HTPotionContent {
        val baseHolder: HTSimpleDeferredHolder<Potion> = register.register(name) { _ -> Potion(name, *baseEffects()) }
        val longHolder: HTSimpleDeferredHolder<Potion> = register.register("long_$name") { _ ->
            Potion(name, *longEffects())
        }
        val strongHolder: HTSimpleDeferredHolder<Potion>? = strongEffects?.let {
            register.register("strong_$name") { _ -> Potion(name, *it()) }
        }
        val content = HTPotionContent(baseHolder, longHolder, strongHolder)
        entries += content
        contentsCache[baseHolder.key] = content
        contentsCache[longHolder.key] = content
        strongHolder?.key?.let { contentsCache[it] = content }
        return content
    }

    /**
     * 新しいポーションを登録します。
     * @param name ポーションのIDのパス
     * @param effect バフとなるエフェクト
     * @return 新しい[HTPotionContent]のインスタンス
     */
    fun registerBeneficial(name: String, effect: Holder<MobEffect>): HTPotionContent = registerPotion(
        name,
        { arrayOf(MobEffectInstance(effect, 3600)) },
        { arrayOf(MobEffectInstance(effect, 9600)) },
        { arrayOf(MobEffectInstance(effect, 1800, 1)) }
    )

    /**
     * 新しいポーションを登録します。
     * @param name ポーションのIDのパス
     * @param effect デバフとなるエフェクト
     * @return 新しい[HTPotionContent]のインスタンス
     */
    fun registerHarmful(name: String, effect: Holder<MobEffect>): HTPotionContent = registerPotion(
        name,
        { arrayOf(MobEffectInstance(effect, 900)) },
        { arrayOf(MobEffectInstance(effect, 1800)) },
        { arrayOf(MobEffectInstance(effect, 432, 1)) }
    )
}
