package hiiragi283.ragium.api.block.type

import hiiragi283.ragium.api.block.attribute.HTBlockAttribute
import java.util.function.Function

typealias BlockAttributeMap = Map<Class<out HTBlockAttribute>, HTBlockAttribute>

typealias BlockAttributeMutableMap = MutableMap<Class<out HTBlockAttribute>, HTBlockAttribute>

/**
 * @see mekanism.common.content.blocktype.BlockType
 */
open class HTBlockType(private val attributeMap: BlockAttributeMap) {
    operator fun contains(clazz: Class<out HTBlockAttribute>): Boolean = clazz in attributeMap

    @Suppress("UNCHECKED_CAST")
    operator fun <T : HTBlockAttribute> get(clazz: Class<T>): T? = attributeMap[clazz] as? T

    inline fun <reified T : HTBlockAttribute> get(): T? = get(T::class.java)

    fun getAll(): Collection<HTBlockAttribute> = attributeMap.values

    //    Builder    //

    abstract class Builder<TYPE : HTBlockType, BUILDER : Builder<TYPE, BUILDER>>(
        protected val factory: Function<BlockAttributeMap, TYPE>,
    ) {
        companion object {
            @JvmStatic
            fun createSimple(): Impl<HTBlockType> = Impl(::HTBlockType)
        }

        private var hasBuilt = false
        private val attributeMap: BlockAttributeMutableMap = hashMapOf()

        protected fun checkHasBuilt() {
            check(!hasBuilt) { "Builder has already built" }
        }

        @Suppress("UNCHECKED_CAST")
        protected fun self(): BUILDER = this as BUILDER

        fun add(vararg attributes: HTBlockAttribute): BUILDER {
            for (attribute: HTBlockAttribute in attributes) {
                attributeMap[attribute::class.java] = attribute
            }
            return self()
        }

        fun remove(vararg classes: Class<out HTBlockAttribute>): BUILDER {
            for (clazz: Class<out HTBlockAttribute> in classes) {
                attributeMap.remove(clazz)
            }
            return self()
        }

        fun build(): TYPE {
            this.hasBuilt = true
            return factory.apply(attributeMap)
        }

        //    Impl    //

        class Impl<TYPE : HTBlockType>(factory: (BlockAttributeMap) -> TYPE) : Builder<TYPE, Impl<TYPE>>(factory)
    }
}
